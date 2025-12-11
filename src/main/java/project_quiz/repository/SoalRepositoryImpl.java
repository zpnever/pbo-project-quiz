package project_quiz.repository;

import project_quiz.config.DB;
import project_quiz.models.Soal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SoalRepositoryImpl implements SoalRepository {

  private Soal mapResultSetToSoal(ResultSet rs) throws SQLException {
    Soal soal = new Soal();
    soal.setId(rs.getString("id"));
    soal.setIdKuis(rs.getString("id_kuis"));
    soal.setQuestion(rs.getString("question"));
    soal.setPoints(rs.getInt("points"));
    soal.setAnswer(rs.getString("answer"));

    // Penanganan Array dari DB (TEXT[])
    Array sqlArray = rs.getArray("butir_pilihan");
    if (sqlArray != null) {
      String[] stringArray = (String[]) sqlArray.getArray();
      soal.setButirPilihan(Arrays.asList(stringArray));
    }

    return soal;
  }

  // CREATE Soal
  @Override
  public Soal save(Soal soal) {
    String SQL = "INSERT INTO soal (id_kuis, question, point, answer, butir_pilihan) " +
        "VALUES (?, ?, ?, ?, ?) RETURNING \"id\"";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setObject(1, soal.getIdKuis(), Types.OTHER);
      stmt.setString(2, soal.getQuestion());
      stmt.setFloat(3, soal.getPoints());
      stmt.setString(4, soal.getAnswer());

      if (soal.getButirPilihan() != null && !soal.getButirPilihan().isEmpty()) {
        Array sqlArray = conn.createArrayOf("TEXT", soal.getButirPilihan().toArray());
        stmt.setArray(5, sqlArray);
      } else {
        stmt.setNull(5, Types.ARRAY);
      }

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          soal.setId(rs.getString("id"));
          return soal;
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat menyimpan soal: " + e.getMessage());
    }
    return null;
  }

  // READ Soal Berdasarkan Kuis ID
  @Override
  public List<Soal> findByKuisId(String kuisId) {
    List<Soal> soalList = new ArrayList<>();
    String SQL = "SELECT * FROM soal WHERE id_kuis = ?";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, kuisId);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          soalList.add(mapResultSetToSoal(rs));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat mencari soal berdasarkan kuis ID: " + e.getMessage());
    }
    return soalList;
  }

  @Override
  public Optional<Soal> findById(String id) {
    // Implementasi findById
    return Optional.empty();
  }

  @Override
  public boolean delete(String id) {
    // Implementasi delete
    return false;
  }
}