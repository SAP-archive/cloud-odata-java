package org.odata4j.test.integration.producer.jpa.oneoff.oneoff05;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Student")
public class Student implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "StudentID")
  private Integer StudentID;

  @Basic(optional = false)
  @Column(name = "StudentName")
  private String StudentName;

  //@ElementCollection
  //private java.util.List<String> courses=new ArrayList<String>();

  public String getStudentName() {
    return StudentName;
  }

  public void setStudentName(String studentName) {
    StudentName = studentName;
  }

  public Integer getStudentID() {
    return StudentID;
  }

  public void setStudentID(Integer studentID) {
    StudentID = studentID;
  }

  /*public java.util.List<String> getCourses() {
   return courses;
  }

  public void setCourses(java.util.List<String> courses) {
   this.courses = courses;
  }*/

}
