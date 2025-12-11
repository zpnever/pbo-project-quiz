package project_quiz.services;

import project_quiz.models.User;
import project_quiz.repository.UserRepository;
import project_quiz.repository.UserRepositoryImpl;
import java.util.Optional;

public class AuthService {

  private final UserRepository userRepository;

  public AuthService() {
    this.userRepository = new UserRepositoryImpl();
  }

  public Optional<User> login(String email, String password) {
    if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
      System.err.println("Email dan password tidak boleh kosong.");
      return Optional.empty();
    }

    Optional<User> userOpt = userRepository.findByEmailAndPassword(email, password);

    if (userOpt.isPresent()) {
      User user = userOpt.get();
      System.out.println("Login berhasil! Selamat datang, " + user.getName());
      return userOpt;
    } else {
      System.err.println("Login gagal. Email atau password salah.");
      return Optional.empty();
    }
  }

  public User register(String name, String email, String password, String role) {

    if (name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
      System.err.println("Semua field (Nama, Email, Password) harus diisi.");
      return null;
    }

    if (userRepository.findByEmail(email).isPresent()) {
      System.err.println("Register gagal: Email " + email + " sudah terdaftar.");
      return null;
    }

    User newUser = new User();
    newUser.setName(name);
    newUser.setEmail(email);
    newUser.setPassword(password);
    newUser.setRole(role.toUpperCase());

    User savedUser = userRepository.save(newUser);

    if (savedUser != null) {
      System.out.println("Register berhasil! ID Anda: " + savedUser.getId() + " sebagai " + savedUser.getRole());
      return savedUser;
    } else {
      System.err.println("Register gagal. Terjadi error saat menyimpan data.");
      return null;
    }
  }

}