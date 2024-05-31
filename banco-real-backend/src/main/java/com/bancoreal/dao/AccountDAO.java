package com.bancoreal.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.bancoreal.dto.TransferRequest;
import com.bancoreal.dto.WithdrawRequest;
import com.bancoreal.model.Account;
import com.bancoreal.model.CurrentAccount;
import com.bancoreal.model.SavingAccount;
import com.bancoreal.model.SupremeAccount;
import com.bancoreal.model.Transaction;
import com.bancoreal.model.AccountStatus;
import com.bancoreal.model.Client;
import com.bancoreal.model.TransactionType;
import com.bancoreal.model.Currency;

public class AccountDAO {
    private String URL_DB = "jdbc:mariadb://localhost:3306/bancoreal";
    private String USER_DB = "root";
    private String PASSWORD_DB = "_mariana07";

    private static final String INSERT_ACCOUNT = "INSERT INTO accounts (account_id, balance, account_status, client_id, account_type) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ACCOUNT_BY_ID = "SELECT * FROM accounts WHERE account_id = ?";
    private static final String SELECT_ACCOUNTS_BY_CLIENT_ID = "SELECT * FROM accounts WHERE client_id = ?";
    private static final String SELECT_ALL = "SELECT * FROM accounts";
    private static final String UPDATE_ACCOUNT = "UPDATE accounts SET balance = ?, account_status = ?, client_id = ?, account_type = ? WHERE account_id = ?";
    private static final String DELETE_ACCOUNT_BY_ID = "DELETE FROM accounts WHERE account_id = ?";

    TransactionDAO transactionDAO = new TransactionDAO();

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(URL_DB, USER_DB, PASSWORD_DB);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: MariaDB JDBC Driver no encontrado.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public void insertAccount(Account newAccount) {
        try (
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ACCOUNT)) {
            preparedStatement.setString(1, newAccount.getAccountId());
            preparedStatement.setDouble(2, newAccount.getBalance());
            preparedStatement.setString(3, newAccount.getAccountStatus().getStatus());
            preparedStatement.setString(4, newAccount.getClient().getClientId());
            preparedStatement.setString(5, newAccount.getClass().getSimpleName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al insertar una cuenta: " + e.getMessage());
        }
    }

    public Account selectAccountById(String accountId) {
        Account account = null;
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_BY_ID)) {
            preparedStatement.setString(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String accountType = resultSet.getString("account_type");
                AccountStatus status = AccountStatus.fromString(resultSet.getString("account_status"));
                Client client = new Client(resultSet.getString("client_id"));

                switch (accountType) {
                    case "CurrentAccount":
                        account = new CurrentAccount(resultSet.getString("account_id"), resultSet.getDouble("balance"), status, client);
                        break;
                    case "SavingAccount":
                        account = new SavingAccount(resultSet.getString("account_id"), resultSet.getDouble("balance"), status, client);
                    case "SupremeAccount":
                        account = new SupremeAccount(resultSet.getString("account_id"), resultSet.getDouble("balance"), status, client);
                        break;
                    default:
                        throw new SQLException("Tipo de cuenta desconocido: " + accountType);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al seleccionar una cuenta por id: " + e.getMessage());
        }
        return account;
    }

    public List<Account> selectAccountsByClientId(String clientId) {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACCOUNTS_BY_CLIENT_ID)) {
            preparedStatement.setString(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                String accountType = resultSet.getString("account_type");
                AccountStatus status = AccountStatus.fromString(resultSet.getString("account_status"));
                Client client = new Client(resultSet.getString("client_id"));
                Account account = null;
    
                switch (accountType) {
                    case "CurrentAccount":
                        account = new CurrentAccount(resultSet.getString("account_id"), resultSet.getDouble("balance"), status, client);
                        break;
                    case "SavingAccount":
                        account = new SavingAccount(resultSet.getString("account_id"), resultSet.getDouble("balance"), status, client);
                        break;
                    case "SupremeAccount":
                        account = new SupremeAccount(resultSet.getString("account_id"), resultSet.getDouble("balance"), status, client);
                        break;
                    default:
                        throw new SQLException("Tipo de cuenta desconocido: " + accountType);
                }
    
                if (account != null) {
                    List<Transaction> transactions = selectTransactionsByAccountId(account.getAccountId());
                    account.setTransactions(transactions);
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al seleccionar cuentas por cliente id: " + e.getMessage());
        }
        return accounts;
    }

    public List<Transaction> selectTransactionsByAccountId(String accountId) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM transactions WHERE account_id = ?")) {
            preparedStatement.setString(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                        resultSet.getLong("transaction_id"),
                        resultSet.getDouble("amount"),
                        TransactionType.valueOf(resultSet.getString("transaction_type")),
                        resultSet.getTimestamp("transaction_date").toLocalDateTime(),
                        Currency.valueOf(resultSet.getString("currency")),
                        null
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.out.println("Error al seleccionar transacciones por id de cuenta: " + e.getMessage());
        }
        return transactions;
    }

    public List<Account> selectAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                String accountType = resultSet.getString("account_type");
                AccountStatus status = AccountStatus.fromString(resultSet.getString("account_status"));
                Client client = new Client(resultSet.getString("client_id"));
                Account account = null;

                switch (accountType) {
                    case "CurrentAccount":
                        account = new CurrentAccount(resultSet.getString("account_id"), resultSet.getDouble("balance"), status, client);
                        break;
                    case "SavingAccount":
                        account = new SavingAccount(resultSet.getString("account_id"), resultSet.getDouble("balance"), status, client);
                        break;
                    case "SupremeAccount":
                        account = new SupremeAccount(resultSet.getString("account_id"), resultSet.getDouble("balance"), status, client);
                        break;
                    default:
                        throw new SQLException("Tipo de cuenta desconocido: " + accountType);
                }

                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println("Error al seleccionar todas las cuentas: " + e.getMessage());
        }
        return accounts;
    }

    public void updateAccount(Account account) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT)) {
            preparedStatement.setDouble(1, account.getBalance());
            preparedStatement.setString(2, account.getAccountStatus().getStatus());
            preparedStatement.setString(3, account.getClient().getClientId());
            preparedStatement.setString(4, account.getClass().getSimpleName());
            preparedStatement.setString(5, account.getAccountId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar una cuenta: " + e.getMessage());
        }
    }

    public void deleteAccountById(String accountId) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ACCOUNT_BY_ID)) {
            preparedStatement.setString(1, accountId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar una cuenta: " + e.getMessage());
        }
    }

    public boolean transferMoney(TransferRequest transferRequest) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            String senderAccountId = transferRequest.getSenderAccountId();
            String recipientAccountId = transferRequest.getRecipientAccountId();
            double amount = transferRequest.getAmount();
            
            Currency currency = Currency.valueOf(transferRequest.getCurrency().toUpperCase());

            Account senderAccount = selectAccountById(senderAccountId);
            Account recipientAccount = selectAccountById(recipientAccountId);

            if (senderAccount == null || recipientAccount == null) {
                connection.rollback();
                return false;
            }

            try {
                senderAccount.sendMoney(amount, currency, recipientAccount);
                recipientAccount.receiveMoney(amount, currency);

                System.out.println("Transacción enviada por el remitente creada.");
                Transaction transferOut = new Transaction(senderAccount.generateTransactionId(), amount, TransactionType.TRANSFER, LocalDateTime.now(), currency, senderAccount);
                senderAccount.addTransaction(transferOut);

                System.out.println("Transacción recibida por el destinatario creada.");
                Transaction transferIn = new Transaction(recipientAccount.generateTransactionId(), amount, TransactionType.RECEIPT, LocalDateTime.now(), currency, recipientAccount);
                recipientAccount.addTransaction(transferIn);

                transactionDAO.insertTransaction(transferOut);
                transactionDAO.insertTransaction(transferIn);
                System.out.println("Transacciones insertadas en la base de datos.");

                updateAccount(senderAccount);
                updateAccount(recipientAccount);
                System.out.println("Cuentas actualizadas.");

                connection.commit();
                return true;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean withdrawMoney(WithdrawRequest withdrawRequest) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            String accountId = withdrawRequest.getAccountId();
            double amount = withdrawRequest.getAmount();
            Currency currency = Currency.valueOf(withdrawRequest.getCurrency().toUpperCase());

            Account account = selectAccountById(accountId);

            if (account == null) {
                connection.rollback();
                return false;
            }

            try {
                account.withdraw(amount, currency);

                System.out.println("Retiro creado.");
                Transaction withdraw = new Transaction(account.generateTransactionId(), amount, TransactionType.WITHDRAW, LocalDateTime.now(), currency, account);
                account.addTransaction(withdraw);

                transactionDAO.insertTransaction(withdraw);
                System.out.println("Transacción insertada.");

                updateAccount(account);
                System.out.println("Cuenta actualizada.");

                connection.commit();
                return true;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
