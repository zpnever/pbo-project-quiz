package project_quiz;

public class User {
  private String id;
  private String name;
  private String email;
  private String password;
  private String role;

  // Constructor
  public User(String name, String email, String password, String id, String role) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  // Getter
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getRole() {
    return role;
  }

  // Optional (nice to have)
  @Override
  public String toString() {
    return "User{name='" + name + "', email='" + email + "', password='" + password + "'}";
  }
}
