package com.bancoreal.model;

import java.time.LocalDateTime;

import org.json.JSONObject;

public class Transaction {
    private long transactionId;
    private double amount;
    private TransactionType transactionType;
    private LocalDateTime date;
    private Currency currency;
    private Account account;

    public Transaction(long transactionId, double amount, TransactionType transactionType, LocalDateTime date, Currency currency, Account account) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.date = date;
        this.currency = currency;
        this.account = account;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("transactionId", this.transactionId);
        json.put("amount", this.amount);
        json.put("transactionType", this.transactionType.toString());
        json.put("transactionDate", this.date.toString());
        json.put("currency", this.currency.toString());
        
        if (this.account != null) {
            json.put("accountId", this.account.getAccountId());
        } else {
            json.put("accountId", JSONObject.NULL);
        }

        return json;
    }
}
