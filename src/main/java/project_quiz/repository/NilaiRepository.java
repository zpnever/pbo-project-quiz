package project_quiz.repository;

import project_quiz.models.Nilai;
import java.util.List;
import java.util.Optional;

public interface NilaiRepository {

  Nilai save(Nilai nilai); // Menyimpan header skor baru (setelah kuis selesai)

  Optional<Nilai> findById(String id);

  Optional<Nilai> findByKuisIdAndSiswaId(String kuisId, String siswaId); // Cek apakah siswa sudah mengerjakan kuis ini

  List<Nilai> findBySiswaId(String siswaId); // Riwayat skor siswa

  List<Nilai> findByKuisId(String kuisId); // Semua skor untuk satu kuis (laporan guru)

  Nilai updateSkor(Nilai nilai); // Update skor total (misalnya setelah penilaian esai)
}