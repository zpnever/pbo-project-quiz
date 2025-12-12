package project_quiz.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
  private static final String URL = "jdbc:postgresql://ep-long-dream-a1n2zl0q-pooler.ap-southeast-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_zCQAHa0P8eIZ&sslmode=require&channelBinding=require";

  public static Connection getConnection() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(URL);
    } catch (SQLException e) {
      System.err.println("Database connection failed!");
      e.printStackTrace();
      return null;
    }
    return conn;
  }
}