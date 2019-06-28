package com.syswin.pipeline.psservice;

import com.syswin.pipeline.app.controller.PiperTokenController;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.ps.sdk.common.ActionItem;
import com.syswin.ps.sdk.common.CommonMsg;
import com.syswin.ps.sdk.handler.PsClientKeeper;
import com.syswin.ps.sdk.msgType.BaseMsgType;
import com.syswin.ps.sdk.sender.AbstractMsgSender;
import com.syswin.ps.sdk.service.SingleChatService;
import com.syswin.ps.sdk.showType.TextShow;
import com.syswin.temail.ps.client.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author:lhz
 * @date:2019/5/25 13:38
 */
@Service
public class MessegerSenderService extends AbstractMsgSender {
	private static final Logger logger = LoggerFactory.getLogger(MessegerSenderService.class);
	@Autowired
	SingleChatService singleChatService;

	@Override
	public void sendMsg(String from, String to, BaseMsgType baseMsgType) {
		String msgId = UUID.randomUUID().toString();
		Header header = psClientService.header(from, to, msgId);
		CommonMsg commonMsg = new CommonMsg(header, baseMsgType.getShowType(), baseMsgType.getShowContent());
		singleChatService.sendMsg(commonMsg);
	}


	public void sendImage(String from, String to, String url, String fileName) throws IOException {
		super.sendImage(from, to, getImageFromNetByUrl(url), fileName);
	}

	/**
	 * 读取 本地文件，转为字节数组
	 *
	 * @param strUrl 本地文件路径
	 * @return
	 * @throws IOException
	 */
	public static byte[] getImageFromNetByUrl(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
			byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
			return btImg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} //由于读取需要一定时间，所以不能单纯往字节数组里读，所以需要判断是否读完

	public static byte[] readInputStream(InputStream inStream) throws Exception { //存放读取的所有的字节数组
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	public void sendComplexMsg(String from, String to, String title, String text, String imageUrl, String infoTile, String infoUrl) {
//		MsgHeader msgHeader= PsClientKeeper.msgHeader();
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isEmpty(title)) {
			throw new RuntimeException("title不能为空");
		}
		map.put("title", title);
		if (!StringUtil.isEmpty(imageUrl)) {
			map.put("imageUrl", imageUrl);
		}
		if (!StringUtil.isEmpty(text)) {
			map.put("text", text);
		}

		List<ActionItem> infoList = Stream.of(new ActionItem(infoTile, infoUrl)
		).collect(Collectors.toList());

		TextShow show = new TextShow(1, map, infoList);
		logger.debug("fromTemail, orderUserId, show" + from + to + show);
		PsClientKeeper.newInstance().sendMsg(from, to, show);
	}
}
