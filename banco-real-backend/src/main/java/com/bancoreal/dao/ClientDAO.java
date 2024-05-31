package com.bancoreal.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bancoreal.model.Client;

public class ClientDAO {
  private String URL_DB = "jdbc:mariadb://localhost:3306/bancoreal";
  private String USER_DB = "root";
  private String PASSWORD_DB = "_mariana07";

  private static final String INSERT_CLIENT = "INSERT INTO clients (first_name, last_name, client_id, user, security_key) VALUES (?, ?, ?, ?, ?)";
  private static final String SELECT_CLIENT_BY_ID = "SELECT * FROM clients WHERE client_id = ?";
  private static final String SELECT_CLIENT_BY_USER = "SELECT * FROM clients WHERE user = ?";
  private static final String SELECT_ALL_CLIENTS = "SELECT * FROM clients";

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

  public void insertClient(Client newClient) {
      try (
          Connection connection = getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CLIENT)) {
          preparedStatement.setString(1, newClient.getFirstName());
          preparedStatement.setString(2, newClient.getLastName());
          preparedStatement.setString(3, newClient.getClientId());
          preparedStatement.setString(4, newClient.getUser());
          preparedStatement.setString(5, newClient.getSecurityKey());
          preparedStatement.executeUpdate();
      } catch (SQLException e) {
          System.out.println("Error al insertar un cliente: " + e.getMessage());
      }
  }

  public Client selectClientById(String clientId) {
      Client client = null;
      try (Connection connection = getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CLIENT_BY_ID)) {
          preparedStatement.setString(1, clientId);
          ResultSet resultSet = preparedStatement.executeQuery();

          if (resultSet.next()) {
              client = new Client(
                      resultSet.getString("first_name"),
                      resultSet.getString("last_name"),
                      resultSet.getString("client_id"),
                      resultSet.getString("user"),
                      resultSet.getString("security_key")
              );
          }
      } catch (SQLException e) {
          System.out.println("Error al seleccionar un cliente por id: " + e.getMessage());
      }
      return client;
  }

  public Client selectClientByUser(String user) {
      Client client = null;
      try (Connection connection = getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CLIENT_BY_USER)) {
          preparedStatement.setString(1, user);
          ResultSet resultSet = preparedStatement.executeQuery();

          if (resultSet.next()) {
              client = new Client(
                      resultSet.getString("first_name"),
                      resultSet.getString("last_name"),
                      resultSet.getString("client_id"),
                      resultSet.getString("user"),
                      resultSet.getString("security_key")
              );
          }
      } catch (SQLException e) {
          System.out.println("Error al seleccionar un cliente por usuario: " + e.getMessage());
      }
      return client;
  }

  public List<Client> selectAllClients() {
      List<Client> clients = new ArrayList<>();
      try (Connection connection = getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CLIENTS)) {
          ResultSet resultSet = preparedStatement.executeQuery();
          
          while (resultSet.next()) {
              Client client = new Client(
                      resultSet.getString("first_name"),
                      resultSet.getString("last_name"),
                      resultSet.getString("client_id"),
                      resultSet.getString("user"),
                      resultSet.getString("security_key")
              );

              clients.add(client);
          }
      } catch (SQLException e) {
          System.out.println("Error al seleccionar todos los clientes: " + e.getMessage());
      }
      return clients;
  }
}
