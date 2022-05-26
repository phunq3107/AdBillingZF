namespace java com.phunq.rpc.adbilling

exception UserNotExist{
    1: i64 userId
}

exception NotEnoughMoney{
    1: i64 userId,
    2: i64 currentBalance,
    3: i64 requiredAmount
}

service AdBillingService{
    i64 getBalance(
        1: i64 userId
    ) throws (1: UserNotExist e1),


    void credit(
        1: i64 userId,
        2: i64 amount
    ) throws (1: UserNotExist e1),


    void debit(
        1: i64 fromUser,
        2: i64 toUser,
        3: i64 amount
    )  throws (1: UserNotExist e1, 2: NotEnoughMoney e2)

}

