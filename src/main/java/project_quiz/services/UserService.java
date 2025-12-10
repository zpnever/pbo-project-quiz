package project_quiz.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import project_quiz.config.DB;
import project_quiz.models.User;

public class UserService {
  // --- C R E A T E (I N S E R T) ---
  public boolean createUser(String name, String email, String password) {
    String SQL = "INSERT INTO \"user\"(name, email, password) VALUES(?, ?, ?)";

    try (Connection conn = DB.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(SQL)) {

      if (conn == null)
        return false;

      pstmt.setString(1, name);
      pstmt.setString(2, email);
      pstmt.setString(3, password);

      int affectedRows = pstmt.executeUpdate();

      if (affectedRows > 0) {
        System.out.println("✅ Pengguna " + name + " berhasil ditambahkan.");
        return true;
      }

    } catch (SQLException e) {
      System.err.println("❌ Error saat menambahkan pengguna: " + e.getMessage());
      e.printStackTrace();
    }
    return false;
  }

  // --- R E A D (S E L E C T B Y I D) ---
  public void getUserById(String id) {
    String SQL = "SELECT * FROM \"user\" WHERE id='?'";

    System.out.println("\n--- Detail Pengguna Berdasarkan ID ---");

    // Deklarasikan ResultSet di luar try-with-resources agar bisa diakses jika ada
    // masalah
    // Namun, kita akan tetap menggunakan try-with-resources untuk efisiensi
    try (Connection conn = DB.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(SQL)) {

      if (conn == null)
        return;

      // 2. Pasang nilai input ke placeholder (PENTING!)
      // Karena id adalah String representasi UUID, kita gunakan setString.
      pstmt.setString(1, id);

      try (java.sql.ResultSet rs = pstmt.executeQuery()) {

        if (rs.next()) {
          String retrievedId = rs.getString("id");
          String name = rs.getString("name");
          String email = rs.getString("email");

          // Cetak ID sebagai String (%s)
          System.out.printf("ID: %-36s | Nama: %-20s | Email: %-30s",
              retrievedId, name, email);
        } else {
          System.out.println("⚠️ Pengguna dengan ID " + id + " tidak ditemukan.");
        }
      }

    } catch (SQLException e) {
      System.err.println("❌ Error saat mengambil data pengguna: " + e.getMessage());
      e.printStackTrace();
    }
  }

  // --- U S E R L O G I N ---
  public User userLogin(String email, String password) {
    String SQL = "SELECT * FROM \"user\" WHERE email = ? AND password = ?";

    try (Connection conn = DB.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(SQL)) {

      if (conn == null)
        return null;

      pstmt.setString(1, email);
      pstmt.setString(2, password);

      try (java.sql.ResultSet rs = pstmt.executeQuery()) {

        if (rs.next()) {
          String id = rs.getString("id");
          String name = rs.getString("name");
          String emailRetrieve = rs.getString("email");
          String passwordRetrieve = rs.getString("password");
          String role = rs.getString("role");

          return new User(id, name, emailRetrieve, passwordRetrieve, role);
        } else {
          System.out.println("⚠️ Login gagal: email atau password salah.");
          return null;
        }
      }

    } catch (SQLException e) {
      System.err.println("❌ Error saat mengambil data pengguna: " + e.getMessage());
      return null;
    }
  }

}