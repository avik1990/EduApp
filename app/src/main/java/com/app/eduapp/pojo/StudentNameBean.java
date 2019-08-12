package com.app.eduapp.pojo;

/**
 * Created by user2 on 15-05-2018.
 */

public class StudentNameBean {

  private String studentName;
  private boolean isSwitched;

  public StudentNameBean(String studentName, boolean isSwitched) {
    this.studentName = studentName;
    this.isSwitched = isSwitched;
  }

  public String getStudentName() {
    return studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  public boolean getIsSwitched() {
    return isSwitched;
  }

  public void setIsSwitched(boolean isSwitched) {
    this.isSwitched = isSwitched;
  }
}
