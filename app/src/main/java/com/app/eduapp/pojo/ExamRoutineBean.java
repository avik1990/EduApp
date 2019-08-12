package com.app.eduapp.pojo;

/**
 * Created by user2 on 15-05-2018.
 */

public class ExamRoutineBean {

  public ExamRoutineBean(String subject, String date, String passmark, String fullMark) {
    this.subject = subject;
    this.date = date;
    this.passmark = passmark;
    this.fullMark = fullMark;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getPassmark() {
    return passmark;
  }

  public void setPassmark(String passmark) {
    this.passmark = passmark;
  }

  public String getFullMark() {
    return fullMark;
  }

  public void setFullMark(String fullMark) {
    this.fullMark = fullMark;
  }

  private String subject;
  private String date;
  private String passmark;
  private String fullMark;
}
