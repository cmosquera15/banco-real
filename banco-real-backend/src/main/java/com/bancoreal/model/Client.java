package com.bancoreal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class Client {
    private String firstName;
    private String lastName;
    private String clientId;
    private String user;
    private String securityKey;
    private List<Account> accounts;

    public Client() {
        this.accounts = new ArrayList<>();
    }

    public Client(String clientId) {
        this.clientId = clientId;
    }

    public Client(String firstName, String lastName, String clientId, String user, String securityKey) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientId = clientId;
        this.user = user;
        this.securityKey = securityKey;
        this.accounts = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("firstName", this.firstName);
        json.put("lastName", this.lastName);
        json.put("clientId", this.clientId);
        json.put("user", this.user);
        if (this.accounts != null) {
            json.put("accounts", this.accounts.stream().map(Account::toJson).collect(Collectors.toList()));
        } else {
            json.put("accounts", new ArrayList<>());
        }
        return json;
    }
}
