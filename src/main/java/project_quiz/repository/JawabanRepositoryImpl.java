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

      stmt.setObject(1, detail.getIdSoal(), Types.OTHER);
      stmt.setObject(2, detail.getIdSiswa(), Types.OTHER);
      stmt.setObject(3, detail.getIdNilai(), Types.OTHER);
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

    String SQL = "SELECT DISTINCT n.id_siswa FROM jawaban dn " +
        "JOIN nilai n ON dn.id_nilai = n.\"id\" " +
        "JOIN kuis k ON n.id_kuis = k.\"id\" " +
        "WHERE k.\"id\" = ? AND k.category = 'ESSAY' AND dn.score = 0";

    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setObject(1, kuisId, Types.OTHER);
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

  @Override
  public Jawaban updateScore(Jawaban detail) {
    String SQL = "UPDATE jawaban SET score = ? WHERE \"id\" = ? RETURNING *";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setBigDecimal(1, detail.getScore());
      stmt.setObject(2, detail.getId(), Types.OTHER);

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
    List<Jawaban> detailList = new ArrayList<>();
    String SQL = "SELECT * FROM jawaban WHERE id_nilai = ?";

    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setObject(1, nilaiId, Types.OTHER);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          detailList.add(mapResultSetToJawaban(rs));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat mencari detail nilai berdasarkan header ID: " + e.getMessage());
    }
    return detailList;
  }

  @Override
  public Optional<Jawaban> findById(String id) {
    String SQL = "SELECT * FROM jawaban WHERE \"id\" = ?";

    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setObject(1, id, Types.OTHER);

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

  @Override
  public List<Jawaban> findEssayDetailsBySiswaAndKuis(String siswaId, String kuisId) {
    List<Jawaban> detailList = new ArrayList<>();
    String SQL = "SELECT dn.* FROM jawaban dn " +
        "JOIN nilai n ON dn.id_nilai = n.\"id\" " +
        "JOIN kuis k ON n.id_kuis = k.\"id\" " +
        "WHERE dn.id_siswa = ? AND k.\"id\" = ? AND k.category = 'ESSAY' " +
        "ORDER BY dn.created_at";

    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setObject(1, siswaId, Types.OTHER);
      stmt.setObject(2, kuisId, Types.OTHER);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          detailList.add(mapResultSetToJawaban(rs));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat mencari detail esai siswa: " + e.getMessage());
    }
    return detailList;
  }
}