package project_quiz.services;

import project_quiz.models.HasilKuis;
import project_quiz.models.Jawaban;
import project_quiz.models.Nilai;
import project_quiz.models.User;
import project_quiz.repository.JawabanRepository;
import project_quiz.repository.JawabanRepositoryImpl;
import project_quiz.repository.NilaiRepository;
import project_quiz.repository.NilaiRepositoryImpl;
import project_quiz.repository.UserRepository;
import project_quiz.repository.UserRepositoryImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NilaiService {

  private final NilaiRepository nilaiRepository;
  private final JawabanRepository jawabanRepository;
  private final UserRepository userRepository;
  private final KuisService kuisService;
  private final SoalService soalService;

  public NilaiService() {
    this.userRepository = new UserRepositoryImpl();
    this.nilaiRepository = new NilaiRepositoryImpl();
    this.jawabanRepository = new JawabanRepositoryImpl();
    this.kuisService = new KuisService();
    this.soalService = new SoalService();
  }

  public boolean hasStudentCompletedQuiz(String kuisId, String siswaId) {
    return nilaiRepository.findByKuisIdAndSiswaId(kuisId, siswaId).isPresent();
  }

  public List<Nilai> getScoreHistoryBySiswaId(String siswaId) {
    return nilaiRepository.findBySiswaId(siswaId);
  }

  public Nilai finalizeNilai(String kuisId, String siswaId, List<Jawaban> submittedJawabans) {

    BigDecimal totalScore = BigDecimal.ZERO;
    for (Jawaban jawaban : submittedJawabans) {
      totalScore = totalScore.add(jawaban.getScore());
    }

    Nilai nilaiHeader = new Nilai();
    nilaiHeader.setIdKuis(kuisId);
    nilaiHeader.setIdSiswa(siswaId);
    nilaiHeader.setSkor(totalScore);

    Nilai savedHeader = nilaiRepository.save(nilaiHeader);

    if (savedHeader != null) {
      for (Jawaban jawaban : submittedJawabans) {
        jawaban.setIdNilai(savedHeader.getId());
        jawabanRepository.save(jawaban);
      }
      return savedHeader;
    }
    return null;
  }

  public List<User> getStudentsWithUnratedEssays(String kuisId) {
    List<String> siswaIds = jawabanRepository.findUniqueSiswaIdWithUnratedEssays(kuisId);
    List<User> students = new ArrayList<>();

    for (String siswaId : siswaIds) {

      userRepository.findById(siswaId).ifPresent(students::add);
    }
    return students;
  }

  public List<Jawaban> getStudentEssayDetails(String siswaId, String kuisId) {
    System.out.println("[DEBUG SERVICE] Mencari esai untuk Siswa ID: " + siswaId + " Kuis ID: " + kuisId);

    return jawabanRepository.findEssayDetailsBySiswaAndKuis(siswaId, kuisId);
  }

  public boolean rateEssay(String jawabanId, BigDecimal scoreGiven) {

    Optional<Jawaban> detailOpt = jawabanRepository.findById(jawabanId);
    if (detailOpt.isEmpty())
      return false;
    Jawaban jawaban = detailOpt.get();

    jawaban.setId(jawabanId);

    jawaban.setScore(scoreGiven);
    Jawaban updatedDetail = jawabanRepository.updateScore(jawaban);

    if (updatedDetail != null) {

      Optional<Nilai> nilaiHeaderOpt = nilaiRepository.findById(updatedDetail.getIdNilai());
      if (nilaiHeaderOpt.isPresent()) {
        Nilai header = nilaiHeaderOpt.get();
        List<Jawaban> allDetails = jawabanRepository.findByNilaiId(header.getId());
        BigDecimal newTotalScore = allDetails.stream().map(Jawaban::getScore).reduce(BigDecimal.ZERO,
            BigDecimal::add);
        header.setSkor(newTotalScore);
        nilaiRepository.updateSkor(header);
      }
      System.out.println("Skor detail berhasil diupdate. (Perlu implementasi update skor total header)");
      return true;
    }
    return false;
  }

  public Nilai recalculateStudentTotalScore(String siswaId, String kuisId) {
    Optional<Nilai> headerOpt = nilaiRepository.findByKuisIdAndSiswaId(kuisId, siswaId);

    if (headerOpt.isEmpty()) {
      System.err.println("Gagal menghitung ulang: Nilai header tidak ditemukan.");
      return null;
    }

    Nilai header = headerOpt.get();
    String nilaiHeaderId = header.getId();

    List<Jawaban> allDetails = jawabanRepository.findByNilaiId(nilaiHeaderId);

    if (allDetails.isEmpty()) {
      System.err.println("Gagal menghitung ulang: Tidak ada detail nilai ditemukan.");
      return null;
    }

    BigDecimal newTotalScore = BigDecimal.ZERO;
    for (Jawaban detail : allDetails) {
      newTotalScore = newTotalScore.add(detail.getScore());
    }

    header.setSkor(newTotalScore);
    Nilai updatedHeader = nilaiRepository.updateSkor(header);

    if (updatedHeader != null) {
      System.out.println("Skor total baru: " + newTotalScore + " berhasil disimpan untuk siswa " + siswaId + ".");
      return updatedHeader;
    } else {
      System.err.println("Gagal menyimpan update skor total ke database.");
      return null;
    }
  }

  public List<HasilKuis> getResultsByKuisId(String kuisId) {
    List<Nilai> nilaiList = nilaiRepository.findByKuisId(kuisId);
    List<HasilKuis> results = new ArrayList<>();

    for (Nilai nilai : nilaiList) {
      Optional<User> userOpt = userRepository.findById(nilai.getIdSiswa());

      String studentName = userOpt.map(User::getName).orElse("N/A (Siswa Dihapus)");

      HasilKuis result = new HasilKuis(
          studentName,
          nilai.getSkor(),
          nilai.getId());
      results.add(result);
    }
    return results;
  }
}