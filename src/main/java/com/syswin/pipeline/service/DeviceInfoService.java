package com.syswin.pipeline.service;

import com.syswin.pipeline.db.model.DeviceInfo;
import com.syswin.pipeline.db.model.DeviceInfoExample;
import com.syswin.pipeline.db.repository.DeviceInfoRepository;
import com.syswin.pipeline.sop.EnableCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:lhz
 * @date:2019/4/11 17:03
 */
@Service
public class DeviceInfoService {

	@Autowired
	DeviceInfoRepository deviceInfoRepository;

	@EnableCacheService(keyPrefix = "lang_",
					fieldKey = "#userId", cacheOperation = EnableCacheService.CacheOperation.UPDATE)
	public String insertOrupdate(String userId, String lang, String platform, String appversion, String build) {
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
		deviceInfoRepository.insert(deviceInfo);

		return lang;
	}

	@EnableCacheService(keyPrefix = "lang_",
					fieldKey = "#userId", cacheOperation = EnableCacheService.CacheOperation.QUERY)
	public String getLang(String userId) {
		DeviceInfoExample deviceInfoExample = new DeviceInfoExample();
		DeviceInfoExample.Criteria criteria = deviceInfoExample.createCriteria();
		criteria.andUseridEqualTo(userId);
		List<DeviceInfo> deviceInfos = deviceInfoRepository.selectByExample(deviceInfoExample);
		String lang = "zh";
		if (deviceInfos.size() > 0) {
			lang = deviceInfos.get(0).getLanguage();
		}
		return lang;
	}

}
