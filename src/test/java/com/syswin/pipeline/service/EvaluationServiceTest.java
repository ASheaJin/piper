package com.syswin.pipeline.service;

import com.syswin.pipeline.db.model.Evaluation;
import com.syswin.pipeline.db.repository.EvaluationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 115477 on 2018/11/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EvaluationServiceTest {
    @Value("app.pipeline.userId")
    private String from;
    @Autowired
    private EvaluationRepository evaluationRepository;

    @Test
    public void insert() {
        Evaluation evaluation = new Evaluation();
        evaluation.setContentId(1l+"");
        evaluation.setEvaluationId(1l+"");
        evaluation.setLevel(1);
        evaluation.setUserId("1112@temail,com");
        evaluationRepository.insert(evaluation);
    }

    @Test
    public void update() {
//        Evaluation evaluation = new Evaluation();
//        evaluation.setContentId(1l);
//        evaluation.setEvaluationId(1l);
//        evaluation.setLevel(1);
//        evaluation.setUserId("1112@temail,com");
//        evaluationRepository.update(evaluation);
    }

    @Test
    public void selectbyId() {

        System.out.println("Account :" +from);
    }

    @Test
    public void select() {

        System.out.println("Account :" + evaluationRepository.select().toString());
    }
}