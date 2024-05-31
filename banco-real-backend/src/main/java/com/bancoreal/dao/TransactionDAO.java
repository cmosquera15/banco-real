package com.bancoreal.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bancoreal.model.Transaction;
import com.bancoreal.model.TransactionType;
import com.bancoreal.model.Currency;

public class TransactionDAO {
    private String URL_DB = "jdbc:mariadb://localhost:3306/bancoreal";
    private String USER_DB = "root";
    private String PASSWORD_DB = "_mariana07";

    private static final String INSERT_TRANSACTION = "INSERT INTO transactions (transaction_id, amount, transaction_type, transaction_date, currency, account_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_TRANSACTIONS_BY_ACCOUNT_ID = "SELECT * FROM transactions WHERE account_id = ?";
    private static final String SELECT_TRANSACTION_ID = "SELECT * FROM transactions WHERE transaction_id = ?";
    private static final String SELECT_ALL = "SELECT * FROM transactions";

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("Conectando a la base de datos...");
            connection = DriverManager.getConnection(URL_DB, USER_DB, PASSWORD_DB);
            System.out.println(connection);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: MariaDB JDBC Driver no encontrado.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public void insertTransaction(Transaction newTransaction) {
        try (
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION)) {
            preparedStatement.setLong(1, newTransaction.getTransactionId());
            preparedStatement.setDouble(2, newTransaction.getAmount());
            preparedStatement.setString(3, newTransaction.getTransactionType().name());
            preparedStatement.setTimestamp(4, java.sql.Timestamp.valueOf(newTransaction.getDate()));
            preparedStatement.setString(5, newTransaction.getCurrency().name());
            preparedStatement.setString(6, newTransaction.getAccount().getAccountId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al insertar una transacción: " + e.getMessage());
        }
    }

    public List<Transaction> selectTransactionsByAccountId(String accountId) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRANSACTIONS_BY_ACCOUNT_ID)) {
            preparedStatement.setString(1, accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                    resultSet.getLong("transaction_id"),
                    resultSet.getDouble("amount"),
                    TransactionType.valueOf(resultSet.getString("transaction_type")),
                    resultSet.getTimestamp("transaction_date").toLocalDateTime(),
                    Currency.valueOf(resultSet.getString("currency")),
                    new AccountDAO().selectAccountById(resultSet.getString("account_id"))
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.out.println("Error al seleccionar transacciones por ID de cuenta: " + e.getMessage());
        }
        return transactions;
    }

    public Transaction selectTransactionById(long transactionId) {
        Transaction transaction = null;
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRANSACTION_ID)) {
            preparedStatement.setLong(1, transactionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                transaction = new Transaction(
                    resultSet.getLong("transaction_id"),
                    resultSet.getDouble("amount"),
                    TransactionType.valueOf(resultSet.getString("transaction_type")),
                    resultSet.getTimestamp("transaction_date").toLocalDateTime(),
                    Currency.valueOf(resultSet.getString("currency")),
                    new AccountDAO().selectAccountById(resultSet.getString("account_id"))
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al seleccionar una transacción por id: " + e.getMessage());
        }
        return transaction;
    }

    public List<Transaction> selectAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                Transaction transaction = new Transaction(
                    resultSet.getLong("transaction_id"),
                    resultSet.getDouble("amount"),
                    TransactionType.valueOf(resultSet.getString("transaction_type")),
                    resultSet.getTimestamp("transaction_date").toLocalDateTime(),
                    Currency.valueOf(resultSet.getString("currency")),
                    new AccountDAO().selectAccountById(resultSet.getString("account_id"))
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.out.println("Error al seleccionar todas las transacciones: " + e.getMessage());
        }
        return transactions;
    }
}
