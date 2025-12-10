package project_quiz.repository;

import project_quiz.models.User;
import java.util.Optional;

public interface UserRepository {

  User save(User user); // register

  Optional<User> findById(String id);

  User updateName(User user);

  User updatePassword(User user);

  Optional<User> findByEmailAndPassword(String email, String password); // login

  Optional<User> findByEmail(String email); // buat cek email
}