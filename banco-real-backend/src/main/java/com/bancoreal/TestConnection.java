package com.bancoreal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
  public static void main(String[] args) {
    String URL_DB = "jdbc:mariadb://localhost:3306/bancoreal";
    String USER_DB = "root";
    String PASSWORD_DB = "_mariana07";
    
    try {
      Class.forName("org.mariadb.jdbc.Driver");
      Connection connection = DriverManager.getConnection(URL_DB, USER_DB, PASSWORD_DB);
      System.out.println("Conexión exitosa: " + connection);
      connection.close();
    } catch (ClassNotFoundException e) {
      System.out.println("Error: MariaDB JDBC Driver no encontrado.");
    } catch (SQLException e) {
      System.out.println("Error al conectar a la base de datos: " + e.getMessage());
    }
  }
}