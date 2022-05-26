package com.phunq.adbilling;

import com.phunq.rpc.adbilling.NotEnoughMoney;
import com.phunq.rpc.adbilling.UserNotExist;
import lombok.AllArgsConstructor;
import org.apache.thrift.TException;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Profile("api")
@RestController
@RequestMapping("/api/v1/adbilling")
@AllArgsConstructor
public class AdBillingController {

    private static final Map<String, List<Long>> map = new HashMap<>(){{
        put("Get balance", new ArrayList<>());
        put("Credit", new ArrayList<>());
        put("Debit", new ArrayList<>());
    }};

    private final AdBillingServiceI adBillingService;

    @GetMapping("/statistical")
    public Map<String, List<Long>> statistical() {
        return map;
    }


    @GetMapping("/balance/{userId}")
    public Long getBalance(@PathVariable Long userId) throws Exception {
        long start = System.currentTimeMillis();
        try {
            return adBillingService.getBalance(userId);
        } finally {
            map.get("Get balance").add(System.currentTimeMillis()-start);
        }
    }


    @PostMapping("/credit")
    public void credit(@RequestBody CreditRequest request) throws Exception {
        long start = System.currentTimeMillis();
        try {
            adBillingService.credit(request.userId(), request.amount());
        } finally {
            map.get("Credit").add(System.currentTimeMillis()-start);
        }

    }

    @PostMapping("/debit")
    public void debit(@RequestBody DebitRequest request) throws Exception {
        long start = System.currentTimeMillis();
        try {
            adBillingService.debit(request.fromUser(), request.toUser(), request.amount());
        } finally {
            map.get("Debit").add(System.currentTimeMillis()-start);        }
    }

    @ExceptionHandler(UserNotExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleUserNotExitException(UserNotExist e) {
        return new HashMap<>() {{
            put("type", "UserNotExist");
            put("userId", e.getUserId());
        }};
    }

    @ExceptionHandler(NotEnoughMoney.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleNotEnoughMoney(NotEnoughMoney e) {
        return new HashMap<>() {{
            put("type", "NotEnoughMoney");
            put("userId", e.getUserId());
            put("currentBalance", e.getCurrentBalance());
            put("requireAmount", e.getRequiredAmount());
        }};
    }
}

record CreditRequest(
        Long userId,
        Long amount
) {
}

record DebitRequest(
        Long fromUser,
        Long toUser,
        Long amount
) {
}
