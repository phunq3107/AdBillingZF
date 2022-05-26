package com.phunq.adbilling;

import com.phunq.rpc.adbilling.AdBillingService;

import java.util.Map;

public interface AdBillingServiceI extends AdBillingService.Iface {
    public Long getBalanceById(Long uid);

    public void updateUsersBalance(Map<Long, Long> map);
}
