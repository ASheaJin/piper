package com.syswin.pipeline.service;

import com.syswin.pipeline.db.model.Card;
import com.syswin.pipeline.db.repository.CardRepository;
import com.syswin.pipeline.utils.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 115477 on 2018/11/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CardServiceTest {

	@Autowired
	private CardRepository cardRepository;

	@Test
	public void insert() {
		Card card = new Card();
		card.setCardId(1l+"");
//        card.setCardId(idWorker.nextId());
		card.setCardNo("1111");
		card.setUserId("111@temail.com");
		cardRepository.insert(card);
	}

	public static void main(String[] args) {

//        Card card = new Card();
//        card.setCardId(1l);
//        card.setCardNo("1111");
//        card.setUserId("111@temail.com");
//        cardRepository.update(card);
		String comBairuserIds = "jsdjk, , sdjk11 dsjk ;; ,  dsjkds1 , sdjk";
//        String regex = "\\s+\\,*\\;*|\\s+\\;*\\,*|\\,+\\s*\\;*|\\,+\\;*\\s*|\\;+\\s*\\,*|\\;+\\s*\\,*";
		String regex = "\\s+|\\,+|\\;+";
		String[] arr = comBairuserIds.split(regex);

		for (String t : arr) {
			if (!StringUtils.isNullOrEmpty(t.trim())) {
				System.out.println(t);
			}
		}
	}

	@Test
	public void selectbyId() {

	}

	@Test
	public void select() {

		System.out.println("Account :" + cardRepository.select().toString());
	}
}