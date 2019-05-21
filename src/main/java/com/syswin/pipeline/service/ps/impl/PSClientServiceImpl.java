package com.syswin.pipeline.service.ps.impl;

import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.syswin.pipeline.service.message.ChatMessageHandler;
import com.syswin.pipeline.service.ps.ChatMsg;
import com.syswin.pipeline.service.ps.ChatMsgPacket;
import com.syswin.pipeline.service.ps.PSClientService;
import com.syswin.pipeline.service.ps.util.CollectionUtil;
import com.syswin.pipeline.service.ps.util.FastJsonUtil;
import com.syswin.pipeline.service.ps.util.LogUtil;
import com.syswin.pipeline.service.ps.util.StringUtil;
import com.syswin.pipeline.utils.CacheUtil;
import com.syswin.sub.api.db.model.Publisher;
import com.syswin.temail.kms.vault.*;
import com.syswin.temail.ps.client.Header;
import com.syswin.temail.ps.client.Message;
import com.syswin.temail.ps.client.PsClient;
import com.syswin.temail.ps.client.PsClientBuilder;
import com.syswin.temail.ps.common.entity.SignatureAlgorithm;
import com.syswin.temail.ps.common.packet.KeyAwarePacketSigner;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import static com.syswin.pipeline.service.ps.impl.AppConstant.*;

/**
 * 参考文档：
 * http://wiki.syswin.com/pages/viewpage.action?pageId=32012902  PsClient使用文档
 * http://wiki.syswin.com/pages/viewpage.action?pageId=31993678 1.2  1.3  1.1指令
 * http://wiki.syswin.com/pages/viewpage.action?pageId=33708322 输入板应用
 * <p>
 * 以及参考齐定的 log-collect-app项目
 * Created by 115477 on 2018/12/17.
 */
@Service
public class PSClientServiceImpl implements PSClientService {
	private final static Logger logger = LoggerFactory.getLogger(PSClientServiceImpl.class);

	private String deviceId;

	private String defaultHost;

	private int defaultPort;

	private String kmsServer;

	private String registerServer;

	private String publishServer;

	private String appTemail;

	private String appPK;

	private String tenantId;

	private KeyAwareAsymmetricCipher cipher;

	private PublicKeyCipher publicKeyCipher;

	private KeyAwareVault keyAwareVault;

	private PsClient psClient;


	private Environment environment;

	@Autowired
	private ChatMessageHandler chatMessageHandler;

	@Autowired
	private com.syswin.sub.api.PublisherService subPublisherService;

	@Value("${app.pipeline.userId}")
	private String from;

	//初始化客户端服务
	public PSClientServiceImpl(Environment environment) {
		this.environment = environment;
	}

	//	@PostConstruct
	public void init() {
		//默认参数初始化
		this.deviceId = environment.getProperty(DEVICE_ID);
		this.defaultHost = environment.getProperty(DEFAULT_HOST);
		this.defaultPort = environment.getProperty(DEFAULT_PORT, Integer.class);
		this.kmsServer = environment.getProperty(KMS_SERVER);
		this.registerServer = environment.getProperty(AUTH_SERVER) + environment.getProperty(REGISTER_SERVER);
		this.publishServer = environment.getProperty(AUTH_SERVER) + environment.getProperty(PUBLISH_SERVER);
		this.appTemail = environment.getProperty(USER_ID);
		this.tenantId = environment.getProperty(TENANT_ID);

		//获取公钥
		this.appPK = initPubKey();
		logger.info("========init publick success==========");

		//初始化psClient
		this.psClient = new PsClientBuilder(deviceId).defaultHost(defaultHost).defaultPort(defaultPort)
						.signer(new KeyAwarePacketSigner(VaultKeeper.keyAwareVault(this.kmsServer, tenantId)))
						.build();
//        Boolean result = registerPubKey();

		List<String[]> appTemailAndPKList = new ArrayList<>();
		//初始化a.piper
		appTemailAndPKList.add(new String[]{appTemail, appPK});
		appTemailAndPKList.addAll(getAppTemailAndPKList());

		try {
			for (String[] appTemailAndPK : appTemailAndPKList) {
				String thisAppTemail = appTemailAndPK[0];
				String thisAppPK = appTemailAndPK[1];


				clientLogin(thisAppTemail, thisAppPK);
			}
		} catch (Exception e) {
			logger.error("psClient 执行 login 发生错误 ", e);
		}
	}

	/**
	 * 注册temail会话的处理逻辑
	 *
	 * @param temail
	 */
	@Override
	public void loginTemail(String temail) {
		String pk = initPubKey(temail);
		clientLogin(temail, pk);
	}


	private void clientLogin(String thisAppTemail, String thisAppPK) {
//		logger.error("登录", thisAppTemail+":"+thisAppPK);
		psClient.login(thisAppTemail, thisAppPK, m -> {
			String payloadStr = StringUtil.byte2Str(m.getPayload());
			MessageResult messageResult = FastJsonUtil.parseObject(payloadStr, MessageResult.class);
			Message msg = psClient.unpack(messageResult.getMessage());
			logger.info("msg:" + FastJsonUtil.toJson(msg));
			String msgStr = null;
			if (msg.getHeader().getDataEncryptionMethod() != 0) {
				msgStr = this.cipher.decrypt(thisAppTemail, StringUtil.byte2Str(msg.getPayload()));
			} else {
				msgStr = StringUtil.byte2Str(msg.getPayload());
			}
			ChatMsg chatMsg = FastJsonUtil.parseObject(msgStr, ChatMsg.class);
			logger.info("chatMsg:" + FastJsonUtil.toJson(chatMsg));
			//输入板应用处理
			chatMessageHandler.handle(chatMsg, reverseHeader(msg.getHeader()));
			CacheUtil.put(msg.getHeader().getReceiver(), msg.getHeader().getReceiverPK());
			//会话式应用处理
//			publisherSecService.monitorORG(msg.getHeader().getReceiver(), msg.getHeader().getSender(), chatMsg);

		});
	}

	public void destory() {
		try {
			if (psClient != null) {
				psClient.logout(appTemail, appPK);
			}
		} catch (Exception e) {
			logger.error("psClient 执行logout发生错误 ", e);
		}
	}


	private Header reverseHeader(final Header header) {
		String to = header.getSender();
		String toPK = header.getSenderPK();// to other
		header.setSender(header.getReceiver());
		header.setSenderPK(header.getReceiverPK());
		header.setReceiver(to);
		header.setReceiverPK(toPK);
		return header;
	}


	//获取公钥
	private String initPubKey() {
		String pubKey;
		keyAwareVault = VaultKeeper.keyAwareVault(kmsServer, tenantId);
		this.cipher = keyAwareVault.asymmetricCipher(CipherAlgorithm.ECDSA);
		this.publicKeyCipher = VaultKeeper.vault().publicKeyCipher(CipherAlgorithm.ECDSA);
		Optional<String> recordKey = null;
		try {
			recordKey = cipher.publicKey(appTemail);
		} catch (Exception e) {
			logger.info("生成公钥失败", e);
		}

		if (recordKey.isPresent()) {
			pubKey = recordKey.get();
		} else {
			pubKey = cipher.register(appTemail);
		}
		return pubKey;
	}

	//获取公钥
	private String initPubKey(String temail) {
		String pubKey;
		keyAwareVault = VaultKeeper.keyAwareVault(kmsServer, tenantId);
		this.cipher = keyAwareVault.asymmetricCipher(CipherAlgorithm.ECDSA);
		this.publicKeyCipher = VaultKeeper.vault().publicKeyCipher(CipherAlgorithm.ECDSA);
		Optional<String> recordKey = null;
		try {
			recordKey = cipher.publicKey(temail);
		} catch (Exception e) {
			logger.info("生成公钥失败", e);
		}

		if (recordKey.isPresent()) {
			pubKey = recordKey.get();
		} else {
			pubKey = cipher.register(temail);
		}
		return pubKey;
	}

	//通过 kms获取公钥
	private String getPubKey(String temail) {
		String pubKey = null;
		keyAwareVault = VaultKeeper.keyAwareVault(kmsServer, tenantId);
		this.cipher = keyAwareVault.asymmetricCipher(CipherAlgorithm.ECDSA);
		this.publicKeyCipher = VaultKeeper.vault().publicKeyCipher(CipherAlgorithm.ECDSA);
		Optional<String> recordKey = null;
		try {
			recordKey = cipher.publicKey(temail);
		} catch (Exception e) {
			logger.info("生成公钥失败", e);
		}

		if (recordKey.isPresent()) {
			pubKey = recordKey.get();
		}
		return pubKey;
	}

	private Message sendMessageProxy(Message msg) {
		LogUtil.logCdtpMsgIn(msg, s -> logger.debug(s));
		Message result = psClient.sendMessage(msg);
		LogUtil.logCdtpMsgOut(result, s -> logger.debug(s));
		return result;
	}


	//初始化header
	private Header initHeader(final short comId, String sender, String senderPK, String receiver, String receiverPK) {
		return initHeader(msgCMS, comId, sender, senderPK, receiver, receiverPK);
	}

	//初始化header
	private Header initHeader(final short comSpaceId, final short comId, String sender, String senderPK, String receiver, String receiverPK) {
		Header header = new Header();
		header.setCommandSpace(comSpaceId);
		header.setCommand(comId);
		header.setSignatureAlgorithm(SignatureAlgorithm.ECC512_CODE);
		header.setDataEncryptionMethod(EnumEncryptionMethod.NO.getCode());
		header.setTimestamp(System.currentTimeMillis());
		header.setPacketId(UUID.randomUUID().toString());
		header.setSender(sender);
		header.setSenderPK(senderPK);
		header.setReceiver(receiver);
		header.setReceiverPK(receiverPK);

		return header;
	}

	//单聊会话列表和详情的消息体
	private <T> Message initQueryMessage(Header header, T payload) {
		Map<String, Object> lastParam = CollectionUtil.fastMap("query", payload);
		Message message = new Message();
		message.setHeader(header);
		byte[] data = FastJsonUtil.toJson(lastParam).getBytes(Charset.forName("UTF-8"));
		message.setPayload(data);

		return message;
	}

	//单聊的消息体
	private <T> Message initChatMessage(Header header, T payload) {
		Message message = new Message();
		message.setHeader(header);

		String payloadStr = FastJsonUtil.toJson(payload);
		if (header.getDataEncryptionMethod() != EnumEncryptionMethod.NO.getCode()) {
			//加密
			payloadStr = this.cipher.encrypt(payloadStr, header.getReceiverPK());
		}
		byte[] data = payloadStr.getBytes(Charset.forName("UTF-8"));
		message.setPayload(data);
		return message;
	}


//	/**
//	 * @param payload 解析保存数据
//	 * @return
//	 */
//	private Map<String, MessageResult> getMsgInfoParam(String payload) {
//		final Map<String, MessageResult> msgMap = new HashMap<>();
//		List<FromSeq> newFromSquece = new ArrayList<>();
//		List<MsgListOut> msgList = FastJsonUtil.parseArray(payload, MsgListOut.class);
//
//		//遍历解析数据
//		msgList.forEach(msg -> {
//			MessageResult result = msg.getLastMsg();
//			if (!MessageResult.isNull(result)) {
//				newFromSquece.add(new FromSeq(result.getFrom(), result.getSeqId()));
//				//设置默认取全部
//				result.setPageSize(result.getSeqId());
//				msgMap.put(result.getFrom(), result);
//			}
//		});
//		if (msgMap.isEmpty()) {
//			return msgMap;
//		}
//		//获取数据库的记录
//		List<FromSeq> fromSqeuces = fromSeqRepository.getMaxSeqId(new ArrayList<>(msgMap.keySet()));
////        List<FromSeq> fromSqeuces = new ArrayList<>();
//		fromSqeuces.forEach(fromSqeuce -> {
//			if (fromSqeuce == null) {
//				return;
//			}
//			String from = fromSqeuce.getFromTemail();
//			MessageResult getMsg = msgMap.get(from);
//
//			Long pageSize = getMsg.getSeqId() - fromSqeuce.getSeqId();
//			if (0 != pageSize) {
//				getMsg.setPageSize(pageSize);
//			} else {
//				msgMap.remove(from);
//			}
//		});
//		//保存新的seqId到数据库
//		fromSeqRepository.saveFromSeq(newFromSquece);
//		return msgMap;
//	}

	/**
	 * 获取消息详情（1，3）
	 *
	 * @param payload
	 * @param chatMsgPacket
	 * @return
	 */
	private void getMsgDetail(String payload, ChatMsgPacket chatMsgPacket) {
		List<ChatMsg> infoList = new ArrayList<>();
		chatMsgPacket.setChatMsgs(infoList);

		List<MessageResult> resultList = FastJsonUtil.parseArray(payload, MessageResult.class);
		resultList.forEach(msgResult -> {
			//2.unpack
			Message cdtpMsg = psClient.unpack(msgResult.getMessage());
			//3.解密数据
			Header chatHeader = cdtpMsg.getHeader();
			String realMsg = StringUtil.byte2Str(cdtpMsg.getPayload());
			chatMsgPacket.setSender(chatHeader.getSender());
			chatMsgPacket.setSenderPK(chatHeader.getSenderPK());
			chatMsgPacket.setReceiver(chatHeader.getReceiver());
			chatMsgPacket.setReceiverPK(chatHeader.getReceiverPK());

			//流程处理
			if (chatHeader.getDataEncryptionMethod() == 4) {
				try {
					realMsg = cipher.decrypt(appTemail, realMsg);

					ChatMsg chatMsgDetail = FastJsonUtil.parseObject(realMsg, ChatMsg.class);
					infoList.add(chatMsgDetail);
				} catch (Exception e) {
					logger.info("decrypt fail", e);
					return;
				}
			} else {

			}
		});
	}


	@Override
	public String getTemailPublicKey(String temail) {
		String publicKey = CacheUtil.get(temail);
		//去缓存
		if (StringUtil.isNotEmpty(publicKey)) {
			return publicKey;
		}
		//从kms中拉取
		publicKey = getPubKey(temail);
		if (StringUtil.isNotEmpty(publicKey)) {
			CacheUtil.put(temail, publicKey);
			return publicKey;
		}
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(publishServer + "/" + temail);

			response = httpclient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();

			if (HttpStatus.SC_OK == statusCode) {
				String entityJson = EntityUtils.toString(response.getEntity());
				Type type = new TypeReference<TemailPubKeyOut<TemailPubKeyData>>() {
				}.getType();
				TemailPubKeyOut<TemailPubKeyData> out = FastJsonUtil.fromJson(entityJson, type);

				publicKey = out.getData().getPubKey();
				//加缓存
				if (StringUtil.isNotEmpty(publicKey)) {
					CacheUtil.put(temail, publicKey);
				} else {
					//从本地密机获取秘邮
					publicKey = getTemailTestPublicKey(temail);
				}
				if (StringUtils.isEmpty(publicKey)) {
					publicKey = registerPub(temail);
				}
				return publicKey;
			}
			return null;
		} catch (Exception e) {
			logger.error("获取公钥发生异常", e);
			return null;
		} finally {
			closeHttpClient(httpclient, response);
		}
	}


	@Override
	public String getTemailTestPublicKey(String temail) {
		String publicKey = CacheUtil.get(temail);
		//去缓存
		if (StringUtil.isNotEmpty(publicKey)) {
			return publicKey;
		}
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String authServerStr = environment.getProperty("url.temail-auth.server");

		try {
			HttpGet httpGet = new HttpGet(authServerStr + "/publish/temails/" + temail);

			response = httpclient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();

			if (HttpStatus.SC_OK == statusCode) {
				String entityJson = EntityUtils.toString(response.getEntity());
				Type type = new TypeReference<TemailPubKeyOut<TemailPubKeyData>>() {
				}.getType();
				TemailPubKeyOut<TemailPubKeyData> out = FastJsonUtil.fromJson(entityJson, type);
				publicKey = out.getData().getPubKey();
				//加缓存
				if (StringUtil.isNotEmpty(publicKey)) {
					CacheUtil.put(temail, publicKey);
				}
				return publicKey;
			}
			return null;
		} catch (Exception e) {
			logger.error("获取公钥发生异常", e);
			return null;
		} finally {
			closeHttpClient(httpclient, response);
		}
	}

	private void closeHttpClient(CloseableHttpClient httpclient, CloseableHttpResponse response) {
		try {
			httpclient.close();
			if (response != null) {
				response.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public String registerPub(String temail) {
		KeyAwareVault keyAwareVault = VaultKeeper.keyAwareVault(kmsServer, tenantId);

		KeyAwareAsymmetricCipher cipher = keyAwareVault.asymmetricCipher(CipherAlgorithm.ECDSA);
//        PublicKeyCipher publicKeyCipher = VaultKeeper.vault().publicKeyCipher(CipherAlgorithm.ECDSA);
		String pubKey;
		Optional<String> recordKey = null;
		try {
			recordKey = cipher.publicKey(temail);
		} catch (Exception e) {
			logger.info("生成公钥失败", e);
		}

		if (recordKey.isPresent()) {
			pubKey = recordKey.get();
		} else {
			pubKey = cipher.register(temail);
		}
		return pubKey;
	}

	@Override
	public String registerTemail(String temail) {
		KeyAwareVault keyAwareVault = VaultKeeper.keyAwareVault(kmsServer, tenantId);

		KeyAwareAsymmetricCipher cipher = keyAwareVault.asymmetricCipher(CipherAlgorithm.ECDSA);
//        PublicKeyCipher publicKeyCipher = VaultKeeper.vault().publicKeyCipher(CipherAlgorithm.ECDSA);
		String pubKey;
		Optional<String> recordKey = null;
		try {
			recordKey = cipher.publicKey(temail);
		} catch (Exception e) {
			logger.info("生成公钥失败", e);
		}

		if (recordKey.isPresent()) {
			pubKey = recordKey.get();
		} else {
			pubKey = cipher.register(temail);
		}

		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			HttpPost httpPost = new HttpPost(registerServer);
			List<NameValuePair> nvps = new ArrayList<>();
			nvps.add(new BasicNameValuePair("PUBLIC_KEY", pubKey));
			nvps.add(new BasicNameValuePair("TeMail", temail));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			response = httpclient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();

			if (HttpStatus.SC_CREATED == statusCode || HttpStatus.SC_CONFLICT == statusCode) {
				return pubKey;
			}
			return null;
		} catch (Exception e) {
			logger.error("注册公钥发生异常", e);

			return null;
		} finally {
			closeHttpClient(httpclient, response);
		}
	}

	/**
	 * 发送文本的消息
	 *
	 * @param content   内容
	 * @param to        发给谁
	 * @param deloytime 延时时间
	 */
	public void sendTextmessage(String content, String to, int deloytime) {
		Map<String, String> contentMap = new HashMap<>();
		contentMap.put("text", content);
		String contentJson = new Gson().toJson(contentMap);
		ChatMsg chatMsg = new ChatMsg(contentJson, 1, from, to, deloytime);
		String publickey = getTemailPublicKey(to);
		sendChatMessage(chatMsg, to, publickey);
	}


	@Override
	public boolean sendChatMessage(ChatMsg msg, String toTemail, String toTemailPK) {
		return sendChatMessage(msg, toTemail, toTemailPK, appTemail, appPK);
	}

	@Override
	public boolean sendCardMessage(ChatMsg msg, String from, String fromPK, String to, String toTemailPK) {
		return sendChatMessage(msg, to, toTemailPK, from, fromPK);
	}

	@Override
	public Boolean sendChatMessage(ChatMsg msg, String toTemail, String toTemailPK, String sendTemail, String sendPK) {
		Header chatMsgHeader = initHeader(singleChatComId, sendTemail, sendPK, toTemail, toTemailPK);
		ExtraData extraData = new ExtraData(sendTemail, toTemail,
						UUID.randomUUID().toString(), 1);
		chatMsgHeader.setExtraData(FastJsonUtil.toJson(extraData));

		Message chatMsg = initChatMessage(chatMsgHeader, msg);

		Message resultMsg = this.sendMessageProxy(chatMsg);
		return resultMsg != null;
	}


	@Override
	public Message sendCdtpRequestFromPiper(final short comSpaceId, final short comId, Object payload) {
		Header header = initHeader(comSpaceId, comId, appTemail, appPK, null, null);
		Message msg = initChatMessage(header, payload);
		Message resultMsg = this.sendMessageProxy(msg);
		return resultMsg;
	}

	@Override
	public Message sendCdtpRequest(final short comSpaceId, final short comId, Object payload, String toTemail, String toTemailPK, String sendTemail, String sendPK) {
		Header header = initHeader(comSpaceId, comId, sendTemail, sendPK, toTemail, toTemailPK);
		Message msg = initChatMessage(header, payload);
		Message resultMsg = this.sendMessageProxy(msg);
		return resultMsg;
	}


	/**
	 * 获得所有出版社的账号和公钥
	 *
	 * @return
	 */
	private List<String[]> getAppTemailAndPKList() {
		List<Publisher> publisherList = subPublisherService.select();
		return publisherList.stream().map(publisher -> {
			String temail = publisher.getPtemail();
			String pk = initPubKey(temail);

			String[] s = {temail, pk};
			return s;
		}).collect(Collectors.toList());

	}
}
