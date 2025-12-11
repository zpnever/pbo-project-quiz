package project_quiz.services;

import project_quiz.models.Kuis;
import project_quiz.models.Soal;
import project_quiz.repository.KuisRepository;
import project_quiz.repository.KuisRepositoryImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class KuisService {

  private final KuisRepository kuisRepository;
  // Asumsi SoalService akan dibutuhkan di sini
  private final SoalService soalService;

  public KuisService() {
    this.kuisRepository = new KuisRepositoryImpl();
    this.soalService = new SoalService(); // Harus ada SoalService
  }

  // Method untuk Guru: Membuat Kuis Baru (Header)
  public Kuis createKuis(String title, String subject, String description, LocalDate start, LocalDate end,
      String category, String creatorId) {

    // Validasi: Pastikan title dan creatorId ada
    if (title.trim().isEmpty() || creatorId.trim().isEmpty()) {
      System.err.println("Judul dan ID pembuat kuis wajib diisi.");
      return null;
    }

    // Validasi: Pastikan tanggal start tidak setelah tanggal end
    if (start != null && end != null && start.isAfter(end)) {
      System.err.println("Tanggal mulai kuis tidak boleh setelah tanggal selesai.");
      return null;
    }

    Kuis newKuis = new Kuis();
    newKuis.setTitle(title);
    newKuis.setSubject(subject);
    newKuis.setDescription(description);
    newKuis.setStartTime(start);
    newKuis.setEndTime(end);
    newKuis.setCategory(category.toUpperCase());
    newKuis.setIdCreator(creatorId);

    return kuisRepository.save(newKuis);
  }

  // Method untuk Guru: Melihat daftar kuis yang dibuat
  public List<Kuis> getKuisByCreator(String creatorId) {
    return kuisRepository.findByCreatorId(creatorId);
  }

  // Method untuk Siswa: Melihat kuis yang tersedia
  public List<Kuis> getAvailableQuizzes() {
    return kuisRepository.findAvailableQuizzes();
  }

  // Method untuk mendapatkan Kuis dan Soal lengkap (digunakan saat siswa mulai
  // kuis)
  public Optional<Kuis> getFullKuisDetails(String kuisId) {
    Optional<Kuis> kuisOpt = kuisRepository.findById(kuisId);
    if (kuisOpt.isPresent()) {
      Kuis kuis = kuisOpt.get();
      // Panggil SoalService untuk mengambil semua soal terkait kuis ini
      List<Soal> soalList = soalService.getSoalByKuisId(kuis.getId());
      // Di sini Anda mungkin perlu memodifikasi model Kuis untuk menampung
      // List<Soal>,
      // atau cukup mengembalikan data terpisah di Service. Untuk CLI sederhana, ini
      // cukup.
      return kuisOpt;
    }
    return Optional.empty();
  }
}