package org.odata4j.test.integration.producer.jpa.oneoff.oneoff02;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

  @ManyToMany(targetEntity = Course.class, cascade = CascadeType.ALL)
  @JoinTable(name = "Student_Course", joinColumns = @JoinColumn(name = "StudentID"), inverseJoinColumns = @JoinColumn(name = "CourseID"))
  private java.util.Collection<Course> courses;

  public Integer getStudentID() {
    return StudentID;
  }

  public void setStudentID(Integer studentID) {
    StudentID = studentID;
  }

  public String getStudentName() {
    return StudentName;
  }

  public void setStudentName(String studentName) {
    StudentName = studentName;
  }

  public java.util.Collection<Course> getCourses() {
    return courses;
  }

  public void setCourses(java.util.Collection<Course> courses) {
    this.courses = courses;
  }

}
