package project_quiz.models;

// Siswa mewarisi properti dari User
public class Siswa extends User {

  public Siswa() {
    super();
  }

  public Siswa(String id, String name, String email, String password, String role) {
    super(id, name, email, password, role);
  }
}