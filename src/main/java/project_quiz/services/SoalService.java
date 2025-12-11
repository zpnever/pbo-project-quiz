package project_quiz.services;

import project_quiz.models.Soal;
import project_quiz.repository.SoalRepository;
import project_quiz.repository.SoalRepositoryImpl;
import java.util.List;

public class SoalService {

  private final SoalRepository soalRepository;

  public SoalService() {
    this.soalRepository = new SoalRepositoryImpl();
  }

  // Method untuk Guru: Menambahkan soal ke Kuis yang sudah ada
  public Soal addSoalToKuis(String idKuis, String question, int points, String answer, List<String> butirPilihan) {

    // Validasi
    if (points <= 0) {
      System.err.println("Poin soal harus lebih dari nol.");
      return null;
    }

    // Validasi
    if (answer.trim().isEmpty()) {
      System.err.println("Kunci jawaban wajib diisi.");
      return null;
    }

    Soal newSoal = new Soal();
    newSoal.setIdKuis(idKuis);
    newSoal.setQuestion(question);
    newSoal.setPoints(points);
    newSoal.setAnswer(answer);
    newSoal.setButirPilihan(butirPilihan);

    return soalRepository.save(newSoal);
  }

  public List<Soal> getSoalByKuisId(String kuisId) {
    return soalRepository.findByKuisId(kuisId);
  }
}