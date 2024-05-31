package com.bancoreal.model;

import java.time.LocalDateTime;

public class SavingAccount extends Account {
    private static final double MAINTENANCE_FEE = 0.025;
    private static final double COMMISSION_RATE = 0.002;

    public SavingAccount(Client client) {
        super(client);
    }

    public SavingAccount(String accountId, double balance, AccountStatus accountStatus, Client client) {
        super(accountId, balance, accountStatus, client);
    }
    
    public long generateTransactionId() {
        return System.currentTimeMillis();
    }

    @Override
    public double chargeCommission(double amountInCOP) {
        double commission = amountInCOP * COMMISSION_RATE;
        if (amountInCOP < 50000) {
            return amountInCOP -= 100;
        } else {
            return commission;
        }
    }

    @Override
    public void payMaintenance() {
        double commission = getBalance() * MAINTENANCE_FEE;
        Transaction transaction = new Transaction(generateTransactionId(), commission, TransactionType.COMMISSION, LocalDateTime.now(), Currency.COP, this);
        addTransaction(transaction);
        setBalance(getBalance() - commission);
    }
}
