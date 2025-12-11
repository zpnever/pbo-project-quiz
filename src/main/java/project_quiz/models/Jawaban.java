package project_quiz.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Jawaban {

  private String id;
  private String idSoal;
  private String idSiswa;
  private String idNilai;
  private BigDecimal score;
  private String jawabanSiswa;
  private LocalDate createdAt;

  public Jawaban() {
  }

  public Jawaban(String id, String idSoal, String idSiswa, String idNilai, BigDecimal score, String jawabanSiswa,
      LocalDate createdAt) {
    this.id = id;
    this.idSoal = idSoal;
    this.idSiswa = idSiswa;
    this.idNilai = idNilai;
    this.score = score;
    this.jawabanSiswa = jawabanSiswa;
    this.createdAt = createdAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIdSoal() {
    return idSoal;
  }

  public void setIdSoal(String idSoal) {
    this.idSoal = idSoal;
  }

  public String getIdSiswa() {
    return idSiswa;
  }

  public void setIdSiswa(String idSiswa) {
    this.idSiswa = idSiswa;
  }

  public String getIdNilai() {
    return idNilai;
  }

  public void setIdNilai(String idNilai) {
    this.idNilai = idNilai;
  }

  public BigDecimal getScore() {
    return score;
  }

  public void setScore(BigDecimal score) {
    this.score = score;
  }

  public String getJawabanSiswa() {
    return jawabanSiswa;
  }

  public void setJawabanSiswa(String jawabanSiswa) {
    this.jawabanSiswa = jawabanSiswa;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
  }
}