package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.Transaction;
import com.syswin.sub.api.db.repository.BaseRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 115477 on 2018/11/27.
 */
@Repository
public interface TransactionRepository extends BaseRepository<Transaction> {

    Integer updateTransactionStatus(@Param("transactionId") Long transactionId, @Param("status") int status);

    List<Transaction> selectByUserId(@Param("userId") String userId);

    List<Transaction> selectByIdTypeTime(@Param("userId") String userId, @Param("type") int type, @Param("startTime") int startTime, @Param("endTime") int endTime, @Param("start") int start, @Param("end") int end);
}
