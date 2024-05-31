package com.bancoreal.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.JSONObject;

public abstract class Account {
    private String accountId;
    private double balance;
    private AccountStatus accountStatus;
    private Client client;
    private List<Transaction> transactions;

    private static final long MIN_ACCOUNT_NUMBER = 1000000000L;
    private static final long MAX_ACCOUNT_NUMBER = 9999999999L;

    public Account(Client client) {
        this.accountId = generateAccountId();
        this.balance = 0;
        this.accountStatus = AccountStatus.OPEN;
        this.client = client;
        this.transactions = new ArrayList<>();
    }

    public Account(double balance, AccountStatus accountStatus, Client client) {
        this.accountId = generateAccountId();
        this.balance = balance;
        this.accountStatus = accountStatus;
        this.client = client;
        this.transactions = new ArrayList<>();
    }

    public Account(String accountId, double balance, AccountStatus accountStatus, Client client) {
        this.accountId = accountId;
        this.balance = balance;
        this.accountStatus = accountStatus;
        this.client = client;
        this.transactions = new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void sendMoney(double amount, Currency currency, Account recipientAccount) {
        double amountInCOP = convertCurrency(amount, currency);
        double commission = chargeCommission(amountInCOP);
        
        if (amountInCOP + commission > this.balance) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar esta operación.");
        }
        
        this.balance -= (amountInCOP + commission);
    }

    public void receiveMoney(double amount, Currency currency) {
        double amountInCOP = convertCurrency(amount, currency);
        this.balance += amountInCOP;
    }

    public void withdraw(double amount, Currency currency) {
        double amountInCOP = convertCurrency(amount, currency);
        double commission = chargeCommission(amountInCOP);
        
        if (amountInCOP + commission > this.balance) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar esta operación.");
        }
        
        this.balance -= (amountInCOP + commission);
    }

    public double convertCurrency(double amount, Currency currencyOrigin) {
        switch (currencyOrigin) {
            case USD:
                return amount * 3800;
            case EUR:
                return amount * 4200;
            default:
                return amount;
        }
    }

    private String generateAccountId() {
        Random random = new Random();
        long accountId = MIN_ACCOUNT_NUMBER + ((long)(random.nextDouble() * (MAX_ACCOUNT_NUMBER - MIN_ACCOUNT_NUMBER)));
        return String.valueOf(accountId);
    }

    public long generateTransactionId() {
        return System.currentTimeMillis();
    }

    public abstract double chargeCommission(double amountInCOP);

    public abstract void payMaintenance();

    public static void chargeMaintenance(List<Account> accounts) {
        LocalDateTime today = LocalDateTime.now();
        if (today.getDayOfMonth() == 1) {
            for (Account account : accounts) {
                account.payMaintenance();
            }
        }
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("accountId", this.accountId);
        json.put("balance", this.balance);
        json.put("accountStatus", this.accountStatus);
        json.put("client", this.client.toJson());
        json.put("accountType", this.getClass());
        json.put("transactions", this.transactions.stream().map(Transaction::toJson).collect(Collectors.toList()));
        return json;
    }
}
