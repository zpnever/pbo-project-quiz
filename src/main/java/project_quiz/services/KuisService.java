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

  private final SoalService soalService;

  public KuisService() {
    this.kuisRepository = new KuisRepositoryImpl();
    this.soalService = new SoalService();
  }

  public Kuis createKuis(String title, String subject, String description, LocalDate start, LocalDate end,
      String category, String creatorId) {

    if (title.trim().isEmpty() || creatorId.trim().isEmpty()) {
      System.err.println("Judul dan ID pembuat kuis wajib diisi.");
      return null;
    }

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

  public List<Kuis> getKuisByCreator(String creatorId) {
    return kuisRepository.findByCreatorId(creatorId);
  }

  public List<Kuis> getAvailableQuizzes() {
    return kuisRepository.findAvailableQuizzes();
  }

  public Optional<Kuis> getFullKuisDetails(String kuisId) {
    Optional<Kuis> kuisOpt = kuisRepository.findById(kuisId);
    if (kuisOpt.isPresent()) {

      return kuisOpt;
    }
    return Optional.empty();
  }
}