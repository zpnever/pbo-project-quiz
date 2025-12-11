package project_quiz.repository;

import project_quiz.models.Kuis;
import java.util.List;
import java.util.Optional;

public interface KuisRepository {
  Kuis save(Kuis kuis);

  Optional<Kuis> findById(String id);

  List<Kuis> findAll(); // Semua kuis

  List<Kuis> findByCreatorId(String creatorId); // Kuis yang dibuat guru tertentu

  List<Kuis> findAvailableQuizzes(); // Kuis yang sedang aktif (start_time <= now <= end_time)

  boolean delete(String id);
}