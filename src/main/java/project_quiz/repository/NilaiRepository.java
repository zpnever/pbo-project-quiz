package project_quiz.repository;

import project_quiz.models.Nilai;
import java.util.List;
import java.util.Optional;

public interface NilaiRepository {

  Nilai save(Nilai nilai);

  Optional<Nilai> findById(String id);

  Optional<Nilai> findByKuisIdAndSiswaId(String kuisId, String siswaId);

  List<Nilai> findBySiswaId(String siswaId);

  List<Nilai> findByKuisId(String kuisId);

  Nilai updateSkor(Nilai nilai);
}