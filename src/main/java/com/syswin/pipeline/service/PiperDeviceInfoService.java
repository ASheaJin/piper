package com.syswin.pipeline.service;

import com.syswin.pipeline.db.model.DeviceInfo;
import com.syswin.pipeline.db.model.DeviceInfoExample;
import com.syswin.pipeline.db.repository.DeviceInfoRepository;
import com.syswin.pipeline.psservice.olderps.Env;
import com.syswin.pipeline.sop.EnableCache;
import com.syswin.pipeline.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:lhz
 * @date:2019/4/11 17:03
 */
@Service
public class PiperDeviceInfoService {

	@Autowired
	DeviceInfoRepository deviceInfoRepository;

	@EnableCache(keyPrefix = "lang_",
					fieldKey = "#userId", cacheOperation = EnableCache.CacheOperation.UPDATE)
	public String insertOrupdate(String userId, Env appEnv) {

		if (appEnv == null || StringUtils.isNullOrEmpty(appEnv.getLanguage())) {
			return "zh";
		}

		DeviceInfoExample deviceInfoExample = new DeviceInfoExample();
		DeviceInfoExample.Criteria criteria = deviceInfoExample.createCriteria();
		criteria.andUseridEqualTo(userId);
		List<DeviceInfo> deviceInfos = deviceInfoRepository.selectByExample(deviceInfoExample);
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setLanguage(appEnv.getLanguage());
		deviceInfo.setUserid(userId);
		deviceInfo.setAppversion(appEnv.getVersion());
		deviceInfo.setBuild(appEnv.getBuild());
		deviceInfo.setOsVersion(appEnv.getOs_version());
		deviceInfo.setPlatform(appEnv.getPlatform());
		deviceInfo.setModuleversion(appEnv.getModuleVersion());
		if (deviceInfos.size() > 0) {
			deviceInfoRepository.updateByExampleSelective(deviceInfo, deviceInfoExample);
		} else {
			deviceInfoRepository.insert(deviceInfo);
		}

		return appEnv.getLanguage();
	}

	@EnableCache(keyPrefix = "lang_",
					fieldKey = "#userId", cacheOperation = EnableCache.CacheOperation.UPDATE)
	public String insertOrupdate(String userId, String lang, String platform, String moduleVersion, String os_version, String appversion, String build) {
		DeviceInfoExample deviceInfoExample = new DeviceInfoExample();
		DeviceInfoExample.Criteria criteria = deviceInfoExample.createCriteria();
		criteria.andUseridEqualTo(userId);
		List<DeviceInfo> deviceInfos = deviceInfoRepository.selectByExample(deviceInfoExample);
		DeviceInfo deviceInfo = null;
		if (deviceInfos.size() > 0) {
			deviceInfo = deviceInfos.get(0);
			if (!lang.equals(deviceInfo.getLanguage())) {
				deviceInfo.setLanguage(lang);
				deviceInfoRepository.updateByExampleSelective(deviceInfo, deviceInfoExample);
			}
			return lang;
		}
		deviceInfo = new DeviceInfo();
		deviceInfo.setLanguage(lang);
		deviceInfo.setUserid(userId);
		deviceInfo.setAppversion(appversion);
		deviceInfo.setBuild(build);
		deviceInfo.setOsVersion(os_version);
		deviceInfo.setPlatform(platform);
		deviceInfo.setModuleversion(moduleVersion);
		deviceInfoRepository.insert(deviceInfo);
		return lang;
	}


	@EnableCache(keyPrefix = "lang_",
					fieldKey = "#userId", cacheOperation = EnableCache.CacheOperation.QUERY)
	public String getLang(String userId) {
		DeviceInfoExample deviceInfoExample = new DeviceInfoExample();
		DeviceInfoExample.Criteria criteria = deviceInfoExample.createCriteria();
		criteria.andUseridEqualTo(userId);
		List<DeviceInfo> deviceInfos = deviceInfoRepository.selectByExample(deviceInfoExample);
		if (deviceInfos.size() > 0) {
			return deviceInfos.get(0).getLanguage();
		}
		return "zh";
	}


	public DeviceInfo getDeviceInfo(String userId) {
		DeviceInfoExample deviceInfoExample = new DeviceInfoExample();
		DeviceInfoExample.Criteria criteria = deviceInfoExample.createCriteria();
		criteria.andUseridEqualTo(userId);
		List<DeviceInfo> deviceInfos = deviceInfoRepository.selectByExample(deviceInfoExample);
		if (deviceInfos.size() > 0) {
			return deviceInfos.get(0);
		}
		return null;
	}
}
