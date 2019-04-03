package com.syswin.pipeline.service;

import com.syswin.pipeline.db.model.SpiderToken;
import com.syswin.pipeline.db.model.SpiderTokenExample;
import com.syswin.pipeline.db.repository.SpiderTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:lhz
 * @date:2019/3/6 13:51
 */
@Service
public class SpiderTokenService {
	@Autowired
	SpiderTokenRepository spiderTokenRepository;


	public SpiderToken getSpiderToken(String token, String ptemail) {
		SpiderTokenExample spiderTokenExample = new SpiderTokenExample();
		SpiderTokenExample.Criteria criteria = spiderTokenExample.createCriteria();
		criteria.andPtemailEqualTo(ptemail).andTokenEqualTo(token);
		List<SpiderToken> tokenList = spiderTokenRepository.selectByExample(spiderTokenExample);
		if (tokenList.size() == 0) {
			return null;
		}
		return tokenList.get(0);
	}

}
