package project_quiz.repository;

import project_quiz.models.Jawaban;
import java.util.List;
import java.util.Optional;

public interface JawabanRepository {

  Jawaban save(Jawaban jawaban); // Menyimpan jawaban siswa per soal

  List<Jawaban> findByNilaiId(String nilaiId); // Detail jawaban satu kuis

  Optional<Jawaban> findById(String id);

  List<String> findUniqueSiswaIdWithUnratedEssays(String kuisId);

  // List<Jawaban> findEssayDetailsBySiswaAndKuis(String siswaId, String kuisId);

  // List<Jawaban> findUnratedEssaysByKuisId(String kuisId); // Untuk Guru menilai

  Jawaban updateScore(Jawaban jawaban); // Update skor setelah dinilai guru
}