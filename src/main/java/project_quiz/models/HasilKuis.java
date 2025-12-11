package project_quiz.models;

import java.math.BigDecimal;

public class HasilKuis {
  private String studentName;
  private BigDecimal score;
  private String resultId;

  public HasilKuis(String studentName, BigDecimal score, String resultId) {
    this.studentName = studentName;
    this.score = score;
    this.resultId = resultId;
  }

  // Getters
  public String getStudentName() {
    return studentName;
  }

  public BigDecimal getScore() {
    return score;
  }

  public String getResultId() {
    return resultId;
  }

}