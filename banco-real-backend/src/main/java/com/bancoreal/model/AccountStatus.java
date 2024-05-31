package com.bancoreal.model;

public enum AccountStatus {
    OPEN("OPEN"),
    CANCELED("CANCELED"),
    EMBARGOED("EMBARGOED");

    private final String status;

    AccountStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static AccountStatus fromString(String status) {
        for (AccountStatus accountStatus : AccountStatus.values()) {
            if (accountStatus.status.equalsIgnoreCase(status)) {
                return accountStatus;
            }
        }
        throw new IllegalArgumentException("Estado de cuenta no válido: " + status);
    }
}
