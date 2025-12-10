package project_quiz.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Nilai {

  private String id;
  private String idKuis;
  private String idSiswa;
  private BigDecimal skor;
  private LocalDate createdAt;

  public Nilai() {
  }

  public Nilai(String id, String idKuis, String idSiswa, BigDecimal skor, LocalDate createdAt) {
    this.id = id;
    this.idKuis = idKuis;
    this.idSiswa = idSiswa;
    this.skor = skor;
    this.createdAt = createdAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIdKuis() {
    return idKuis;
  }

  public void setIdKuis(String idKuis) {
    this.idKuis = idKuis;
  }

  public String getIdSiswa() {
    return idSiswa;
  }

  public void setIdSiswa(String idSiswa) {
    this.idSiswa = idSiswa;
  }

  public BigDecimal getSkor() {
    return skor;
  }

  public void setSkor(BigDecimal skor) {
    this.skor = skor;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
  }
}