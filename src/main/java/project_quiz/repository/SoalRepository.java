package project_quiz.repository;

import project_quiz.models.Soal;
import java.util.List;
import java.util.Optional;

public interface SoalRepository {

  Soal save(Soal soal);

  Optional<Soal> findById(String id);

  List<Soal> findByKuisId(String kuisId);

  boolean delete(String id);
}