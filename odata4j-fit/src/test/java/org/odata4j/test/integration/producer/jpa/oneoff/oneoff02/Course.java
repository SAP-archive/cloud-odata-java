package org.odata4j.test.integration.producer.jpa.oneoff.oneoff02;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Course")
public class Course implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "CourseID")
  private Integer CourseID;

  @Basic(optional = false)
  @Column(name = "CourseName")
  private String CourseName;

  @ManyToMany(targetEntity = Student.class)
  private java.util.Collection<Student> students;

  public Integer getCourseID() {
    return CourseID;
  }

  public void setCourseID(Integer courseID) {
    CourseID = courseID;
  }

  public String getCourseName() {
    return CourseName;
  }

  public void setCourseName(String courseName) {
    CourseName = courseName;
  }

  public java.util.Collection<Student> getStudents() {
    return students;
  }

  public void setStudents(java.util.Collection<Student> students) {
    this.students = students;
  }

}
