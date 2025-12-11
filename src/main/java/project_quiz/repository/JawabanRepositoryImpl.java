package project_quiz.repository;

import project_quiz.config.DB;
import project_quiz.models.Jawaban;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JawabanRepositoryImpl implements JawabanRepository {

  private Jawaban mapResultSetToJawaban(ResultSet rs) throws SQLException {
    Jawaban detail = new Jawaban();
    detail.setId(rs.getString("ID"));
    detail.setIdSoal(rs.getString("id_soal"));
    detail.setIdSiswa(rs.getString("id_siswa"));
    detail.setIdNilai(rs.getString("id_nilai"));
    detail.setScore(rs.getBigDecimal("score"));
    detail.setJawabanSiswa(rs.getString("jawaban"));
    detail.setCreatedAt(rs.getDate("created_at").toLocalDate());
    return detail;
  }

  // CREATE Jawaban (Menyimpan Jawaban Siswa)
  @Override
  public Jawaban save(Jawaban detail) {
    String SQL = "INSERT INTO jawaban (id_soal, id_siswa, id_nilai, score, jawaban) " +
        "VALUES (?, ?, ?, ?, ?) RETURNING \"id\", created_at";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, detail.getIdSoal());
      stmt.setString(2, detail.getIdSiswa());
      stmt.setString(3, detail.getIdNilai());
      stmt.setBigDecimal(4, detail.getScore());
      stmt.setString(5, detail.getJawabanSiswa());

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          detail.setId(rs.getString("id"));
          detail.setCreatedAt(rs.getDate("created_at").toLocalDate());
          return detail;
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat menyimpan detail nilai: " + e.getMessage());
    }
    return null;
  }

  // READ Jawaban yang Belum Dinilai untuk Kuis Esai (Untuk Guru)
  // @Override
  // public List<Jawaban> findUnratedEssaysByKuisId(String kuisId) {
  // List<Jawaban> detailList = new ArrayList<>();
  // String SQL = "SELECT j.* FROM jawaban j " +
  // "JOIN nilai n ON j.id_nilai = n.\"id\" " +
  // "JOIN kuis k ON n.id_kuis = k.\"id\" " +
  // "WHERE k.\"id\" = ? AND k.category = 'ESSAY' AND j.score = 0";

  // try (Connection conn = DB.getConnection();
  // PreparedStatement stmt = conn.prepareStatement(SQL)) {

  // stmt.setString(1, kuisId);
  // try (ResultSet rs = stmt.executeQuery()) {
  // while (rs.next()) {
  // detailList.add(mapResultSetToJawaban(rs));
  // }
  // }
  // } catch (SQLException e) {
  // System.err.println("Error saat mencari jawaban esai yang belum dinilai: " +
  // e.getMessage());
  // }
  // return detailList;
  // }

  @Override
  public List<String> findUniqueSiswaIdWithUnratedEssays(String kuisId) {
    List<String> siswaIds = new ArrayList<>();

    String SQL = "SELECT DISTINCT n.id_siswa FROM detail_nilai dn " +
        "JOIN nilai n ON dn.id_nilai = n.\"id\" " +
        "JOIN kuis k ON n.id_kuis = k.\"id\" " +
        "WHERE k.\"id\" = ? AND k.category = 'ESSAY' AND dn.score = 0";

    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, kuisId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          siswaIds.add(rs.getString("id_siswa"));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat mencari siswa dengan esai yang belum dinilai: " + e.getMessage());
    }
    return siswaIds;
  }

  // UPDATE Skor Detail (Setelah Guru menilai)
  @Override
  public Jawaban updateScore(Jawaban detail) {
    String SQL = "UPDATE jawaban SET score = ? WHERE \"id\" = ? RETURNING *";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setBigDecimal(1, detail.getScore());
      stmt.setString(2, detail.getId());

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return mapResultSetToJawaban(rs);
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat mengupdate skor detail: " + e.getMessage());
    }
    return null;
  }

  @Override
  public List<Jawaban> findByNilaiId(String nilaiId) {
    // Implementasi findByNilaiId
    return new ArrayList<>();
  }

  @Override
  public Optional<Jawaban> findById(String id) {
    String SQL = "SELECT * FROM jawaban WHERE \"id\" = ?";

    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, id);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapResultSetToJawaban(rs));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error mencari user by ID: " + e.getMessage());
    }
    return Optional.empty();

  }
}