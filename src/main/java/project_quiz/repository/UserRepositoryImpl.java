package project_quiz.repository;

import project_quiz.config.DB;
import project_quiz.models.User;
import java.sql.*;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

  // Utility buat maping result jadi object
  private User mapResultSetToUser(ResultSet rs) throws SQLException {
    User user = new User();
    user.setId(rs.getString("id"));
    user.setName(rs.getString("name"));
    user.setEmail(rs.getString("email"));
    user.setPassword(rs.getString("password"));
    user.setRole(rs.getString("role"));
    return user;
  }

  @Override
  public Optional<User> findByEmailAndPassword(String email, String password) {
    String SQL = "SELECT * FROM \"user\" WHERE email = ? AND password = ?";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, email);
      stmt.setString(2, password);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapResultSetToUser(rs));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat login: " + e.getMessage());
    }
    return Optional.empty();
  }

  @Override
  public User save(User user) {
    String SQL = "INSERT INTO \"user\" (name, email, password, role) VALUES (?, ?, ?, ?) RETURNING \"id\", role";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, user.getName());
      stmt.setString(2, user.getEmail());
      stmt.setString(3, user.getPassword());
      stmt.setString(4, user.getRole() != null ? user.getRole() : "STUDENT");

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          user.setId(rs.getString("id"));
          user.setRole(rs.getString("role"));
          return user;
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat menyimpan user baru: " + e.getMessage());
    }
    return null;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    String SQL = "SELECT * FROM \"user\" WHERE email = ?";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, email);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapResultSetToUser(rs));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error mencari user by email: " + e.getMessage());
    }
    return Optional.empty();
  }

  @Override
  public User updateName(User user) {
    String SQL = "UPDATE \"user\" SET name = ? WHERE \"id\" = ? RETURNING *";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, user.getName());
      stmt.setString(2, user.getId());

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return mapResultSetToUser(rs);
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat mengupdate name: " + e.getMessage());
    }
    return null;
  }

  @Override
  public User updatePassword(User user) {
    String SQL = "UPDATE \"user\" SET password = ? WHERE \"id\" = ? RETURNING *";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, user.getPassword());
      stmt.setString(2, user.getId());

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return mapResultSetToUser(rs);
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat mengupdate password: " + e.getMessage());
    }
    return null;
  }

  @Override
  public Optional<User> findById(String id) {
    String SQL = "SELECT * FROM \"user\" WHERE \"id\" = ?";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {
      stmt.setObject(1, id, Types.OTHER);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapResultSetToUser(rs));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error mencari user by ID: " + e.getMessage());
    }
    return Optional.empty();
  }

}