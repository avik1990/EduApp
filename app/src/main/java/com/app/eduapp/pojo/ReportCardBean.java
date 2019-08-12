package com.app.eduapp.pojo;

/**
 * Created by user2 on 15-05-2018.
 */

public class ReportCardBean {
  private String subject;
  private String date;
  private String passmark;
  private String fullMark;
  private String acq_marks;
  private String grade;

  public ReportCardBean(String subject, String date, String passmark, String fullMark,
                        String acq_marks, String grade) {
    this.subject = subject;
    this.date = date;
    this.passmark = passmark;
    this.fullMark = fullMark;
    this.acq_marks = acq_marks;
    this.grade = grade;
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

  public String getAcq_marks() {
    return acq_marks;
  }

  public void setAcq_marks(String acq_marks) {
    this.acq_marks = acq_marks;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }
}
