package project_quiz.repository;

import project_quiz.config.DB;
import project_quiz.models.Kuis;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KuisRepositoryImpl implements KuisRepository {

  // Mappnig kuis ke object
  private Kuis mapResultSetToKuis(ResultSet rs) throws SQLException {
    Kuis kuis = new Kuis();
    kuis.setId(rs.getString("id"));
    kuis.setTitle(rs.getString("title"));
    kuis.setSubject(rs.getString("subject"));
    kuis.setDescription(rs.getString("description"));
    // Mapping Date SQL ke LocalDate Java
    kuis.setStartTime(rs.getDate("start_time") != null ? rs.getDate("start_time").toLocalDate() : null);
    kuis.setEndTime(rs.getDate("end_time") != null ? rs.getDate("end_time").toLocalDate() : null);
    kuis.setIdCreator(rs.getString("id_creator"));
    kuis.setCategory(rs.getString("category"));
    return kuis;
  }

  // CREATE Kuis
  @Override
  public Kuis save(Kuis kuis) {
    String SQL = "INSERT INTO kuis (title, subject, description, start_time, end_time, id_creator, category) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING \"id\"";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, kuis.getTitle());
      stmt.setString(2, kuis.getSubject());
      stmt.setString(3, kuis.getDescription());
      // Mapping LocalDate Java ke Date SQL
      stmt.setDate(4, kuis.getStartTime() != null ? Date.valueOf(kuis.getStartTime()) : null);
      stmt.setDate(5, kuis.getEndTime() != null ? Date.valueOf(kuis.getEndTime()) : null);
      stmt.setObject(6, kuis.getIdCreator(), Types.OTHER);
      stmt.setString(7, kuis.getCategory());

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          kuis.setId(rs.getString("id"));
          return kuis;
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat menyimpan kuis: " + e.getMessage());
    }
    return null;
  }

  // READ Kuis Berdasarkan Creator ID
  @Override
  public List<Kuis> findByCreatorId(String creatorId) {
    List<Kuis> kuisList = new ArrayList<>();
    String SQL = "SELECT * FROM kuis WHERE id_creator = ?";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setObject(1, creatorId, Types.OTHER);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          kuisList.add(mapResultSetToKuis(rs));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat mencari kuis oleh creator: " + e.getMessage());
    }
    return kuisList;
  }

  // READ Kuis yang Tersedia Saat Ini
  @Override
  public List<Kuis> findAvailableQuizzes() {
    List<Kuis> kuisList = new ArrayList<>();
    String SQL = "SELECT * FROM kuis WHERE start_time <= CURRENT_DATE AND end_time >= CURRENT_DATE";
    try (Connection conn = DB.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(SQL)) {

      while (rs.next()) {
        kuisList.add(mapResultSetToKuis(rs));
      }
    } catch (SQLException e) {
      System.err.println("Error saat mencari kuis yang tersedia: " + e.getMessage());
    }
    return kuisList;
  }

  @Override
  public Optional<Kuis> findById(String id) {
    String SQL = "SELECT * FROM \"kuis\" WHERE id = ?";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setObject(1, id, Types.OTHER);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapResultSetToKuis(rs));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat mencari kuis by id: " + e.getMessage());
    }
    return Optional.empty();
  }

  @Override
  public List<Kuis> findAll() {
    // Implementasi findAll (BLOM KELAR)
    return new ArrayList<>();
  }

  @Override
  public boolean delete(String id) {
    // Implementasi delete (BLOM KELAR)
    return false;
  }
}