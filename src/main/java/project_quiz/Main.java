package project_quiz;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import project_quiz.models.HasilKuis;
import project_quiz.models.Jawaban;
import project_quiz.models.Kuis;
import project_quiz.models.Nilai;
import project_quiz.models.Soal;
import project_quiz.models.User;
import project_quiz.services.AuthService;
import project_quiz.services.KuisService;
import project_quiz.services.NilaiService;
import project_quiz.services.SoalService;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.init();
    }
}

class Menu {
    private static final Scanner sc = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final KuisService kuisService = new KuisService();
    private static final SoalService soalService = new SoalService();
    private static final NilaiService nilaiService = new NilaiService();

    private static User loggedInUser = null;

    public void init() {
        System.out.println("===== SELAMAT DATANG =====");
        System.out.println("Creator : Kelompok 2");

        showMain();
    }

    private static void helperMenu(String[] menus) {
        for (int i = 0; i < menus.length; i++) {
            System.out.printf("%d. %s\n", i + 1, menus[i]);
        }
        System.out.print("Pilih Opsi: ");
    }

    private static void showMain() {
        while (true) {
            System.out.println("\n--- MAIN MENU ---    ");
            String[] mainMenus = { "Login", "Register", "Exit" };

            helperMenu(mainMenus);
            var choice = sc.nextLine();

            switch (choice) {
                case "1":
                    login();
                    if (loggedInUser != null) {
                        if ("LECTURER".equalsIgnoreCase(loggedInUser.getRole())) {
                            showGuruMenu();
                        } else if ("STUDENT".equalsIgnoreCase(loggedInUser.getRole())) {
                            showSiswaMenu();
                        }
                        loggedInUser = null;
                    }
                    break;
                case "2":
                    register();
                    break;
                case "3":
                    System.out.println("Terima kasih! Aplikasi dimatikan.");
                    sc.close();
                    return;
                default:
                    System.err.println("Opsi tidak valid.");
            }
        }
    }

    private static void login() {
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        Optional<User> userOpt = authService.login(email, password);

        if (userOpt.isPresent()) {
            loggedInUser = userOpt.get();
        }
    }

    private static void register() {
        System.out.println("\n--- REGISTER BARU ---");
        System.out.print("Nama Lengkap: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        String role;
        while (true) {
            System.out.print("Daftar sebagai (S for Student/L for Lecturer): ");
            String roleInput = sc.nextLine().toUpperCase();
            if (roleInput.equals("S")) {
                role = "STUDENT";
                break;
            } else if (roleInput.equals("L")) {
                role = "LECTURER";
                break;
            } else {
                System.err.println("Pilihan role tidak valid. Pilih S (STUDENT) atau L (LECTURER).");
            }
        }

        authService.register(name, email, password, role);
    }

    private static void showGuruMenu() {
        System.out.println("\n--- MENU GURU ---");
        System.out.println("Selamat datang, Guru " + loggedInUser.getName() + "!");
        while (true) {
            String[] menus = { "Buat Kuis Baru", "Lihat Kuis Yang Saya Buat", "Nilai Kuis Essay", "Logout" };
            helperMenu(menus);

            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    handleCreateKuis();
                    break;
                case "2":
                    handleViewCreatedKuis();
                    break;
                case "3":
                    handleRateEssays();
                    break;
                case "4":
                    System.out.println("Guru " + loggedInUser.getName() + " berhasil logout.");
                    return;
                default:
                    System.err.println("Opsi tidak valid.");
            }
        }
    }

    private static void handleCreateKuis() {
        System.out.println("\n--- BUAT KUIS BARU ---");
        System.out.print("Judul Kuis: ");
        String title = sc.nextLine();
        System.out.print("Mata Pelajaran: ");
        String subject = sc.nextLine();
        System.out.print("Deskripsi (Opsional): ");
        String description = sc.nextLine();

        LocalDate startDate = null;
        LocalDate endDate = null;
        try {
            System.out.print("Tanggal Mulai (YYYY-MM-DD, kosongkan untuk hari ini): ");
            String startStr = sc.nextLine();
            if (!startStr.isEmpty())
                startDate = LocalDate.parse(startStr);
            else
                startDate = LocalDate.now();

            System.out.print("Tanggal Selesai (YYYY-MM-DD): ");
            endDate = LocalDate.parse(sc.nextLine());
        } catch (DateTimeParseException e) {
            System.err.println("Format tanggal salah. Silakan ulangi. (Gunakan format YYYY-MM-DD)");
            return;
        }

        String category;
        while (true) {
            System.out.print("Kategori Soal (PG/ESSAY): ");
            String catInput = sc.nextLine().toUpperCase();
            if (catInput.equals("PG")) {
                category = "PG";
                break;
            } else if (catInput.equals("ESSAY")) {
                category = "ESSAY";
                break;
            } else {
                System.err.println("Kategori tidak valid. Pilih PG atau ESSAY.");
            }
        }

        Kuis kuis = kuisService.createKuis(title, subject, description, startDate, endDate, category,
                loggedInUser.getId());

        if (kuis != null) {
            System.out.println("Kuis '" + kuis.getTitle() + "' berhasil dibuat dengan ID: " + kuis.getId());
            handleAddSoalToKuis(kuis);
        }
    }

    private static void handleAddSoalToKuis(Kuis kuis) {
        System.out.println("\n--- TAMBAH SOAL UNTUK KUIS: " + kuis.getTitle() + "---");

        int soalCount = 1;
        while (true) {
            System.out.print("Tambah Soal #" + soalCount + "? (y/n): ");
            if (!sc.nextLine().equalsIgnoreCase("y"))
                break;

            System.out.print("Poin: ");
            int points = 0;

            try {
                points = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Poin harus berupa angka.");
                continue;
            }

            System.out.print("Pertanyaan: ");
            String question = sc.nextLine();

            List<String> butirPilihan = null;
            if (kuis.getCategory().equalsIgnoreCase("PG")) {
                butirPilihan = new ArrayList<>();
                System.out.println("Masukkan Opsi Pilihan Ganda (pisahkan dengan koma, cth: A,B,C,D):");
                String options = sc.nextLine();
                butirPilihan = Arrays.asList(options.split(", "));
            }

            String answer;
            System.out.print("Kunci Jawaban: ");
            answer = sc.nextLine();

            Soal soal = soalService.addSoalToKuis(kuis.getId(), question, points, answer, butirPilihan);
            if (soal != null) {
                System.out.println("Soal ke-" + soalCount + " berhasil ditambahkan.");
                soalCount++;
            } else {
                System.err.println("Gagal menambahkan soal. Cek input Anda.");
            }
        }
        System.out.println("Selesai menambahkan soal.");
    }

    private static void handleViewCreatedKuis() {
        System.out.println("\n--- DAFTAR KUIS YANG ANDA BUAT ---");
        List<Kuis> myQuizzes = kuisService.getKuisByCreator(loggedInUser.getId());

        if (myQuizzes.isEmpty()) {
            System.out.println("Anda belum membuat kuis apa pun.");
            return;
        }

        for (int i = 0; i < myQuizzes.size(); i++) {
            Kuis k = myQuizzes.get(i);
            System.out.printf("%d. [%s] %s (%s) - Mulai: %s\n",
                    i + 1, k.getCategory(), k.getTitle(), k.getSubject(), k.getStartTime());
            System.out.println("   ID Kuis: " + k.getId());
        }

        System.out.println("Menu:");
        String[] menus = { "Lihat Detail Kuis", "Kembali" };

        while (true) {
            helperMenu(menus);
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    handleViewDetailKuis(myQuizzes);
                    break;
                case "2":
                    return;
                default:
                    System.err.println("Opsi tidak valid.");
            }
        }
    }

    private static void handleViewDetailKuis(List<Kuis> quizzes) {
        System.out.print("Masukkan ID Kuis yang ingin dilihat detailnya: ");
        String kuisId = sc.nextLine();

        Optional<Kuis> kuisOpt = quizzes.stream()
                .filter(k -> k.getId().equals(kuisId))
                .findFirst();

        if (kuisOpt.isEmpty()) {
            System.err.println("ID Kuis tidak ditemukan dalam daftar kuis yang Anda buat.");
            return;
        }

        Kuis selectedKuis = kuisOpt.get();

        System.out.println("\n--- DETAIL KUIS: " + selectedKuis.getTitle() + " ---");
        System.out.println("Mata Pelajaran: " + selectedKuis.getSubject());
        System.out.println("Kategori: " + selectedKuis.getCategory());
        System.out.println("Periode: " + selectedKuis.getStartTime() + " s/d " + selectedKuis.getEndTime());

        System.out.println("\n--- HASIL SISWA YANG SUDAH MENGERJAKAN ---");

        List<HasilKuis> results = nilaiService.getResultsByKuisId(selectedKuis.getId());

        if (results.isEmpty()) {
            System.out.println("Belum ada siswa yang mengerjakan kuis ini.");
            return;
        }

        System.out.printf("| %-30s | %-10s | %-36s |\n", "NAMA SISWA", "SKOR AKHIR", "ID NILAI");
        System.out.println("---------------------------------------------------------------------------------");

        for (HasilKuis result : results) {
            System.out.printf("| %-30s | %-10.2f | %-36s |\n",
                    result.getStudentName(),
                    result.getScore(),
                    result.getResultId());
        }
        System.out.println("---------------------------------------------------------------------------------");

        System.out.println("\nTekan ENTER untuk kembali.");
        sc.nextLine();
    }

    private static void handleRateEssays() {
        System.out.println("\n--- MENILAI JAWABAN ESAI ---");

        List<Kuis> myQuizzes = kuisService.getKuisByCreator(loggedInUser.getId());
        List<Kuis> essayQuizzes = new ArrayList<>();

        System.out.println("Pilih Kuis Esai yang akan Dinilai:");
        int count = 1;
        for (Kuis k : myQuizzes) {
            if (k.getCategory() != null && k.getCategory().equalsIgnoreCase("ESSAY")) {
                essayQuizzes.add(k);
                System.out.println(count++ + ". " + k.getTitle() + " (ID: " + k.getId() + ")");
            }
        }

        if (essayQuizzes.isEmpty()) {
            System.out.println("Anda tidak memiliki kuis Esai yang aktif.");
            return;
        }

        System.out.print("Pilih nomor kuis: ");
        int choice = Integer.parseInt(sc.nextLine());
        if (choice < 1 || choice > essayQuizzes.size()) {
            System.err.println("Pilihan tidak valid.");
            return;
        }

        Kuis selectedKuis = essayQuizzes.get(choice - 1);

        List<User> studentsToRate = nilaiService.getStudentsWithUnratedEssays(selectedKuis.getId());

        if (studentsToRate.isEmpty()) {
            System.out.println("Semua jawaban esai untuk kuis ini sudah dinilai.");
            return;
        }

        System.out.println("\n--- " + studentsToRate.size() + " SISWA PERLU DINILAI ---");
        for (int i = 0; i < studentsToRate.size(); i++) {
            System.out.println((i + 1) + ". " + studentsToRate.get(i).getName());
        }

        System.out.print("Pilih nomor Siswa yang akan dinilai: ");
        int studentChoice = Integer.parseInt(sc.nextLine());

        if (studentChoice < 1 || studentChoice > studentsToRate.size()) {
            System.err.println("Pilihan tidak valid.");
            return;
        }

        User student = studentsToRate.get(studentChoice - 1);

        List<Jawaban> studentEssays = nilaiService.getStudentEssayDetails(student.getId(), selectedKuis.getId());

        System.out.println("\n--- MENILAI ESAI SISWA: " + student.getName() + " ---");

        boolean needsRecalculation = false;

        for (Jawaban dn : studentEssays) {

            Optional<Soal> soalOpt = soalService.findById(dn.getIdSoal());
            Soal soal = soalOpt.orElse(new Soal());

            System.out.println("------------------------------------");
            System.out.println("Soal: " + soal.getQuestion());
            System.out.println("Poin Maksimum: " + soal.getPoints());
            System.out.println("Kunci Jawaban: " + soal.getAnswer());
            System.out.println("Jawaban Siswa:\n" + dn.getJawabanSiswa());

            BigDecimal currentScore = dn.getScore();

            if (currentScore.compareTo(BigDecimal.ZERO) == 0) {
                try {
                    while (true) {
                        System.out.print("Beri Nilai (Maks " + soal.getPoints() + "): ");
                        BigDecimal score = new BigDecimal(sc.nextLine());

                        if (score.intValue() > soal.getPoints() || score.compareTo(BigDecimal.ZERO) < 0) {
                            System.err.println("Nilai harus antara 0 dan " + soal.getPoints() + ".");
                        } else {
                            if (nilaiService.rateEssay(dn.getId(), score)) {
                                System.out.println("Nilai " + score + " berhasil disimpan untuk jawaban ini.");
                                needsRecalculation = true;
                                break;
                            } else {
                                System.err.println("Gagal menyimpan nilai.");
                            }
                        }

                    }
                } catch (Exception e) {
                    System.err.println("Input nilai tidak valid.");
                }
            } else {
                System.out.println("Skor Saat Ini: " + currentScore + " (Sudah dinilai)");
            }
        }

        if (needsRecalculation) {
            System.out.print("\nSemua penilaian selesai. Hitung ulang skor total header siswa (y/n)? ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                nilaiService.recalculateStudentTotalScore(student.getId(), selectedKuis.getId());
                System.out.println("Skor total siswa (header Nilai) berhasil diperbarui!");
            }
        } else {
            System.out.println("\nTidak ada perubahan skor yang memerlukan perhitungan ulang header.");
        }
    }

    private static void showSiswaMenu() {
        System.out.println("\n--- MENU SISWA ---");
        System.out.println("Selamat datang, " + loggedInUser.getName() + "!");
        while (true) {
            String[] menus = { "Lihat Kuis Tersedia", "Lihat Riwayat Skor", "Edit Profil", "Logout" };
            helperMenu(menus);

            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    handleViewAvailableQuizzes();
                    break;
                case "2":
                    handleViewScores();
                    break;
                case "3":
                    handleEditProfile();
                    break;
                case "4":
                    System.out.println("Siswa " + loggedInUser.getName() + " berhasil logout.");
                    return;
                default:
                    System.err.println("Opsi tidak valid.");
            }
        }
    }

    private static void handleViewAvailableQuizzes() {
        System.out.println("\n--- DAFTAR KUIS TERSEDIA ---");
        List<Kuis> availableQuizzes = kuisService.getAvailableQuizzes();

        if (availableQuizzes.isEmpty()) {
            System.out.println("Tidak ada kuis yang tersedia saat ini.");
            return;
        }

        for (int i = 0; i < availableQuizzes.size(); i++) {
            Kuis k = availableQuizzes.get(i);
            System.out.printf("%d. [%s] %s (%s) - Berakhir: %s\n",
                    i + 1, k.getCategory(), k.getTitle(), k.getSubject(), k.getEndTime());
            System.out.println("   ID Kuis: " + k.getId());
        }

        System.out.print("Masukkan ID Kuis yang ingin dikerjakan (atau 'n' untuk batal): ");
        String kuisId = sc.nextLine();

        if (!kuisId.equalsIgnoreCase("n")) {
            handleTakeQuiz(kuisId);
        }
    }

    private static void handleTakeQuiz(String kuisId) {
        if (nilaiService.hasStudentCompletedQuiz(kuisId, loggedInUser.getId())) {
            System.err.println("Anda sudah pernah mengerjakan kuis ini.");
            return;
        }

        Optional<Kuis> kuisOpt = kuisService.getFullKuisDetails(kuisId);
        if (kuisOpt.isEmpty()) {
            System.err.println("Kuis tidak ditemukan atau tidak aktif.");
            return;
        }
        Kuis kuis = kuisOpt.get();

        List<Soal> soalList = soalService.getSoalByKuisId(kuis.getId());
        if (soalList.isEmpty()) {
            System.err.println("Kuis ini belum memiliki soal.");
            return;
        }

        System.out.println("\n--- MULAI KUIS: " + kuis.getTitle() + " ---");
        System.out.println("Total Soal: " + soalList.size());

        List<Jawaban> submittedDetails = new ArrayList<>();
        int currentScore = 0;

        for (int i = 0; i < soalList.size(); i++) {
            Soal soal = soalList.get(i);
            System.out.println("\nSoal #" + (i + 1) + " (Poin: " + soal.getPoints() + ")");
            System.out.println("Q: " + soal.getQuestion());

            if (kuis.getCategory().equalsIgnoreCase("PG") && soal.getButirPilihan() != null) {
                System.out.println("Opsi Jawaban:");
                for (String option : soal.getButirPilihan()) {
                    System.out.println(" - " + option.trim());
                }
            }

            System.out.print("Jawaban Anda: ");
            String studentAnswer = sc.nextLine();

            BigDecimal score = BigDecimal.ZERO;

            if (kuis.getCategory().equalsIgnoreCase("PG")) {
                if (studentAnswer.trim().equalsIgnoreCase(soal.getAnswer().trim())) {
                    score = new BigDecimal(soal.getPoints());
                    currentScore += soal.getPoints();
                }
            } else if (kuis.getCategory().equalsIgnoreCase("ESSAY")) {
                score = BigDecimal.ZERO;
                System.out.println("(Jawaban esai ini akan dinilai oleh Guru.)");
            }

            Jawaban detail = new Jawaban();
            detail.setIdSoal(soal.getId());
            detail.setIdSiswa(loggedInUser.getId());
            detail.setJawabanSiswa(studentAnswer);
            detail.setScore(score);
            submittedDetails.add(detail);
        }

        Nilai finalNilai = nilaiService.finalizeNilai(kuis.getId(), loggedInUser.getId(), submittedDetails);

        if (finalNilai != null) {
            System.out.println("\n--- KUIS SELESAI ---");
            System.out.println("Skor Awal Anda (PG/Isian): " + finalNilai.getSkor());
            if (kuis.getCategory().equalsIgnoreCase("ESSAY")) {
                System.out.println("Nilai akhir akan muncul setelah Guru selesai menilai jawaban esai Anda.");
            }
        } else {
            System.err.println("Gagal menyimpan hasil kuis.");
        }
    }

    private static void handleViewScores() {
        System.out.println("\n--- RIWAYAT SKOR ANDA ---");
        List<Nilai> scoreHistory = nilaiService.getScoreHistoryBySiswaId(loggedInUser.getId());

        if (scoreHistory.isEmpty()) {
            System.out.println("Anda belum mengerjakan kuis apa pun.");
            return;
        }

        System.out.printf("| %-36s | %-10s | %-10s | %-12s |\n", "ID Nilai", "ID Kuis", "SKOR", "Tanggal");
        System.out.println("--------------------------------------------------------------------------------");
        for (Nilai n : scoreHistory) {

            System.out.printf("| %-36s | %-10s | %-10.2f | %-12s |\n",
                    n.getId(), n.getIdKuis().substring(0, 8) + "...", n.getSkor(), n.getCreatedAt());
        }
    }

    private static void handleEditProfile() {
        System.out.println("\n--- EDIT PROFIL (" + loggedInUser.getName() + ") ---");

        System.out.print("Nama Baru (kosongkan jika tidak diubah): ");
        String newName = sc.nextLine();

        System.out.print("Password Baru (kosongkan jika tidak diubah): ");
        String newPassword = sc.nextLine();

        User tempUser = new User(loggedInUser.getId(), loggedInUser.getName(), loggedInUser.getEmail(),
                loggedInUser.getPassword(), loggedInUser.getRole());

        if (!newName.trim().isEmpty()) {
            tempUser.setName(newName);
        }
        if (!newPassword.trim().isEmpty()) {
            tempUser.setPassword(newPassword);
        }

        if (true) {
            loggedInUser.setName(tempUser.getName());
            loggedInUser.setPassword(tempUser.getPassword());
            System.out.println("Profil berhasil diperbarui!");
        } else {
            System.err.println("Gagal memperbarui profil.");
        }
    }
}