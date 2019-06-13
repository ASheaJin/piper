package com.syswin.pipeline.service.org.impl;

import com.alibaba.fastjson.TypeReference;
import com.syswin.pipeline.service.org.EmployeeOut;
import com.syswin.pipeline.service.org.IOrgService;
import com.syswin.pipeline.service.org.OrgOut;
import com.syswin.pipeline.psservice.olderps.PSClientService;
import com.syswin.pipeline.utils.FastJsonUtil;
import com.syswin.pipeline.utils.StringUtil;
import com.syswin.temail.ps.client.Message;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 115477 on 2019/2/20.
 */
@Service
public class OrgServiceImpl implements IOrgService {

	private final static Logger logger = LoggerFactory.getLogger(OrgServiceImpl.class);

	private static final short CMD_SPACE_ORG = 8;
	private static final short CMD_GET = 3;

	@Autowired
	private PSClientService psClientService;


	@Override
	public OrgOut getOrgByVersion(String fromTemail, long version) {
		OrgRequest req = new OrgRequest(fromTemail, "1", version);

		OrgResponse resp = reqestByHttp(req);

		List<OrgDataTemail> adds = resp.getData().getCipherContacts().get("add");

		return buildOrgTree(adds);
	}

	private static OrgOut buildOrgTree(List<OrgDataTemail> odts) {
		OrgOut rootOrg = createOrg("");
		String rootOrgName = null;

		for (OrgDataTemail odt : odts) {
			List<String> orgs = odt.getOrgs();
			OrgOut currOrg = rootOrg;
			for (int i = 0; i < orgs.size(); i++) {
				String orgName = orgs.get(i);
				if (i == 0) {
					if (rootOrgName == null) {
						rootOrgName = orgName;
					} else if (!rootOrgName.equals(orgName)) {
						break;
					}
				} else {
					//从第2个元素开始
					List<OrgOut> subOrgs = currOrg.getSubOrg();
					currOrg = findOrgFromList(orgName, subOrgs);
				}

				//如果是最后一级组织，则放入员工放入employee列表
				if (i == orgs.size() - 1) {
					currOrg.getEmployees().add(new EmployeeOut(odt.getCard().getName(), odt.getTemail(), odt.getCard().getTitle()));
				}
			}
		}
		rootOrg.setName(rootOrgName);
		return rootOrg;
	}

	private static OrgOut findOrgFromList(String orgName, List<OrgOut> subOrgs) {
		for (OrgOut org : subOrgs) {
			if (org.getName().equals(orgName)) {
				return org;
			}
		}
		OrgOut newOrg = createOrg(orgName);
		subOrgs.add(newOrg);
		return newOrg;
	}

	private static OrgOut createOrg(String name) {
		OrgOut orgOut = new OrgOut();
		orgOut.setName(name);
		orgOut.setSubOrg(new ArrayList<>());
		orgOut.setEmployees(new ArrayList<>());
		return orgOut;
	}


	private String orgServer = "http://192.168.1.115:8081";

	private OrgResponse reqestByHttp(OrgRequest req) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			String url = orgServer + "/api/getOrgContact";
			Type paramType = new TypeReference<Map<String, String>>() {
			}.getType();
			Map<String, String> paramsMap = FastJsonUtil.fromJson(FastJsonUtil.toJson(req), paramType);
			url = attachParam(url, paramsMap);

			HttpGet httpGet = new HttpGet(url);

			response = httpclient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();

			if (HttpStatus.SC_OK == statusCode) {
				String entityJson = EntityUtils.toString(response.getEntity());
				Type type = new TypeReference<OrgResponse>() {
				}.getType();
				OrgResponse out = FastJsonUtil.fromJson(entityJson, type);
				return out;
			}
			return null;
		} catch (Exception e) {
			logger.error("请求组织发生异常", e);
			return null;
		} finally {
			closeHttpClient(httpclient, response);
		}
	}

	private static String attachParam(String url, Map<String, String> paramsMap) {
		if (paramsMap != null && !paramsMap.isEmpty()) {
			StringBuilder result = new StringBuilder();
			Iterator var3 = paramsMap.keySet().iterator();

			while (var3.hasNext()) {
				String key = (String) var3.next();
				String encodedName = encodeFormFields(key, "UTF-8");
				String encodedValue = encodeFormFields((String) paramsMap.get(key), "UTF-8");
				if (result.length() > 0) {
					result.append("&");
				}

				result.append(encodedName);
				if (encodedValue != null) {
					result.append("=");
					result.append(encodedValue);
				}
			}

			return url + (url.contains("?") ? "&" : "?") + result.toString();
		} else {
			return url;
		}
	}

	private static String encodeFormFields(String s, String charset) {
		try {
			return URLEncoder.encode(s, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return s;
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

	public static void main(String[] args) {
		OrgOut out = new OrgServiceImpl().getOrgByVersion("11@msgseal.com", 0);
		System.out.println(out);
	}
}
