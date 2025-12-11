package project_quiz.repository;

import project_quiz.config.DB;
import project_quiz.models.Nilai;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NilaiRepositoryImpl implements NilaiRepository {

  private Nilai mapResultSetToNilai(ResultSet rs) throws SQLException {
    Nilai nilai = new Nilai();
    nilai.setId(rs.getString("ID"));
    nilai.setIdKuis(rs.getString("id_kuis"));
    nilai.setIdSiswa(rs.getString("id_siswa"));
    nilai.setSkor(rs.getBigDecimal("skor"));
    nilai.setCreatedAt(rs.getDate("created_at").toLocalDate());
    return nilai;
  }

  // CREATE Nilai (Header)
  @Override
  public Nilai save(Nilai nilai) {
    String SQL = "INSERT INTO nilai (id_kuis, id_siswa, skor) VALUES (?, ?, ?) RETURNING \"id\", created_at";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, nilai.getIdKuis());
      stmt.setString(2, nilai.getIdSiswa());
      stmt.setBigDecimal(3, nilai.getSkor() != null ? nilai.getSkor() : BigDecimal.ZERO);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          nilai.setId(rs.getString("id"));
          nilai.setCreatedAt(rs.getDate("created_at").toLocalDate());
          return nilai;
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat menyimpan nilai header: " + e.getMessage());
    }
    return null;
  }

  // READ Nilai berdasarkan Siswa ID
  @Override
  public List<Nilai> findBySiswaId(String siswaId) {
    List<Nilai> nilaiList = new ArrayList<>();
    String SQL = "SELECT * FROM nilai WHERE id_siswa = ? ORDER BY created_at DESC";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, siswaId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          nilaiList.add(mapResultSetToNilai(rs));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat mengambil riwayat nilai siswa: " + e.getMessage());
    }
    return nilaiList;
  }

  @Override
  public Nilai updateSkor(Nilai nilai) {
    String SQL = "UPDATE nilai SET skor = ? WHERE \"id\" = ? RETURNING *";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setBigDecimal(1, nilai.getSkor());
      stmt.setString(2, nilai.getId());

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return mapResultSetToNilai(rs);
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat mengupdate skor total: " + e.getMessage());
    }
    return null;
  }

  @Override
  public Optional<Nilai> findById(String id) {
    String SQL = "SELECT * FROM \"nilai\" WHERE id = ?";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, id);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapResultSetToNilai(rs));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat mencari nilai by id: " + e.getMessage());
    }
    return Optional.empty();
  }

  @Override
  public Optional<Nilai> findByKuisIdAndSiswaId(String kuisId, String siswaId) {
    String SQL = "SELECT * FROM \"nilai\" WHERE id_kuis = ? AND id_siswa = ?";
    try (Connection conn = DB.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL)) {

      stmt.setString(1, kuisId);
      stmt.setString(2, siswaId);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapResultSetToNilai(rs));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error saat mencari nilai by kuis id dan siswa id: " + e.getMessage());
    }
    return Optional.empty();
  }

  @Override
  public List<Nilai> findByKuisId(String kuisId) {
    // Implementasi findByKuisId
    return new ArrayList<>();
  }
}