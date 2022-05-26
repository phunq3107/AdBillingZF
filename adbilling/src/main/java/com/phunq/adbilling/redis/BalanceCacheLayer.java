package com.phunq.adbilling.redis;

import com.phunq.adbilling.AccountRepository;
import com.phunq.adbilling.AdBillingServiceImpl;
import com.phunq.adbilling.entity.Account;
import com.phunq.rpc.adbilling.AdBillingService;
import lombok.AllArgsConstructor;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.redisson.api.MapOptions;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Collection;
import java.util.Map;

@Component
@AllArgsConstructor
public class BalanceCacheLayer {

    public static String CACHE_NAME = "redis-cache-balance";

    private final RedissonClient redissonClient;
    private final AdBillingServiceImpl adBillingService;

    @Bean(name = "balanceRMapCache")
    public RMapCache<Long, Long> balanceRMapCache() {
        return redissonClient.getMapCache(
                CACHE_NAME,
                MapOptions.<Long, Long>defaults()
                        .loader(getMapLoader())
                        .writer(getMapWriter())
                        .writeMode(MapOptions.WriteMode.WRITE_THROUGH)
        );

    }

    private MapWriter<Long, Long> getMapWriter() {
        return new MapWriter<>() {
            @Override
            public void write(Map<Long, Long> map) {
                adBillingService.updateUsersBalance(map);
            }

            @Override
            public void delete(Collection<Long> collection) {

            }
        };
    }

    private MapLoader<Long, Long> getMapLoader() {
        return new MapLoader<>() {
            @Override
            public Long load(Long uid) {
                return adBillingService.getBalanceById(uid);
            }

            @Override
            public Iterable<Long> loadAllKeys() {
                return null;
            }
        };
    }


}
