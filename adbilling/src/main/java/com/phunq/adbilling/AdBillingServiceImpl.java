package com.phunq.adbilling;

import com.phunq.adbilling.kafka.KafkaService;
import com.phunq.adbilling.kafka.KafkaTopicConfig;
import com.phunq.adbilling.kafka.TransactionHistoryMessage;
import com.phunq.rpc.adbilling.NotEnoughMoney;
import com.phunq.rpc.adbilling.UserNotExist;
import org.apache.thrift.TException;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class AdBillingServiceImpl implements AdBillingServiceI {

    private final RMapCache<Long, Long> balanceCache;
    private final RedissonClient redissonClient;
    private final AccountRepository accountRepository;

    private final KafkaService kafkaService;


    public AdBillingServiceImpl(
            @Lazy @Qualifier("balanceRMapCache") RMapCache<Long, Long> balanceCache,
            RedissonClient redissonClient,
            AccountRepository accountRepository,
            KafkaService kafkaService
    ) {
        this.balanceCache = balanceCache;
        this.redissonClient = redissonClient;
        this.accountRepository = accountRepository;
        this.kafkaService = kafkaService;
    }

    public Long getBalanceByIdInCache(Long userId) throws UserNotExist {
        return Optional.ofNullable(balanceCache.get(userId))
                .orElseThrow(UserNotExist::new);
    }

    @Override
    public long getBalance(long userId) throws UserNotExist, TException {
        return getBalanceByIdInCache(userId);
    }

    @Override
    public void credit(long userId, long amount) throws UserNotExist, TException {
        RLock lock = redissonClient.getLock(String.valueOf(userId));
        lock.lock();
        try {
            Long balance = getBalanceByIdInCache(userId);
            balanceCache.put(userId, balance + amount);
            kafkaService.sendCreditMessage(userId, amount, balance + amount);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void debit(long fromUser, long toUser, long amount) throws UserNotExist, NotEnoughMoney, TException {
        RLock lock = redissonClient.getLock(String.valueOf(fromUser));
        lock.lock();
        try {
            Long balance = getBalanceByIdInCache(fromUser);
            getBalanceByIdInCache(toUser);
            if (amount > balance) {
                throw new NotEnoughMoney(fromUser, balance, amount);
            }
            balanceCache.put(fromUser, balance - amount);
            kafkaService.sendDebitMessage(fromUser, toUser, amount, balance - amount);
            // release lock as soon as possible to prevent deadlock
            lock.unlock();
            credit(toUser, amount);
        } finally {
            if (lock.isHeldByCurrentThread() && lock.isLocked())
                lock.unlock();
        }
    }

    @Transactional
    public Long getBalanceById(Long uid) {
        return accountRepository.getBalanceById(uid).orElse(null);
    }

    @Transactional
    public void updateUsersBalance(Map<Long, Long> map) {
        map.forEach(accountRepository::updateBalance);
    }


}
