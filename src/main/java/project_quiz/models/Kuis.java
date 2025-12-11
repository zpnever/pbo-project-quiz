package project_quiz.models;

import java.time.LocalDate;

public class Kuis {

  private String id;
  private String title;
  private String subject;
  private String description;
  private LocalDate startTime;
  private LocalDate endTime;
  private String idCreator;
  private String category;

  public Kuis() {
  }

  public Kuis(String id, String title, String subject, String description, LocalDate startTime, LocalDate endTime,
      String idCreator, String category) {
    this.id = id;
    this.title = title;
    this.subject = subject;
    this.description = description;
    this.startTime = startTime;
    this.endTime = endTime;
    this.idCreator = idCreator;
    this.category = category;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDate startTime) {
    this.startTime = startTime;
  }

  public LocalDate getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDate endTime) {
    this.endTime = endTime;
  }

  public String getIdCreator() {
    return idCreator;
  }

  public void setIdCreator(String idCreator) {
    this.idCreator = idCreator;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}