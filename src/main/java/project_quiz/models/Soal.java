package project_quiz.models;

import java.util.List;

public class Soal {

  private String id;
  private String idKuis;
  private String question;
  private float points;
  private String answer;
  private List<String> butirPilihan;

  public Soal() {
  }

  public Soal(String id, String idKuis, String question, float points, String answer, List<String> butirPilihan) {
    this.id = id;
    this.idKuis = idKuis;
    this.question = question;
    this.points = points;
    this.answer = answer;
    this.butirPilihan = butirPilihan;
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

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public float getPoints() {
    return points;
  }

  public void setPoints(float points) {
    this.points = points;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public List<String> getButirPilihan() {
    return butirPilihan;
  }

  public void setButirPilihan(List<String> butirPilihan) {
    this.butirPilihan = butirPilihan;
  }
}