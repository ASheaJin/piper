package com.syswin.pipeline.db.repository;

import com.syswin.pipeline.db.model.Account;
import com.syswin.sub.api.db.repository.BaseRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 115477 on 2018/11/27.
 */
@Repository
public interface AccountRepository extends BaseRepository<Account> {
    
    Account selectByUserId(@Param("userId") String userId);

    List<Account> selectByUserIdlike(@Param("userId") String userId, @Param("start") int start, @Param("end") int end);

    int updateAccountBalance(Account account);
}
