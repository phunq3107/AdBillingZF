package com.phunq.adbilling;

import com.phunq.adbilling.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Modifying
    @Query("update Account set balance=:newBalance where uid=:uid")
    void updateBalance(@Param("uid") Long uid, @Param("newBalance") Long newBalance);

    @Query("select balance from Account where uid=:uid")
    Optional<Long> getBalanceById(@Param("uid") Long uid);
}
