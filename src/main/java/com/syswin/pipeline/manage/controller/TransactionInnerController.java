package com.syswin.pipeline.manage.controller;

import com.syswin.pipeline.db.model.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 发券
 * Created by 115477 on 2018/11/28.
 */
@Controller
@RequestMapping("/manage/transaction")
//@Api(value = "transaction", tags = "transaction")
public class TransactionInnerController extends BaseAction<Transaction> {

}
