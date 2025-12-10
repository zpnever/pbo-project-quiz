package project_quiz.models;

// Guru mewarisi properti dari User
public class Guru extends User {

  public Guru() {
    super();
  }

  public Guru(String id, String name, String email, String password, String role) {
    super(id, name, email, password, role);
  }

}