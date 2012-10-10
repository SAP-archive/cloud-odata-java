package org.odata4j.test.integration.producer.jpa.oneoff.oneoff03;

import java.io.Serializable;
import java.util.ArrayList;

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

  @ManyToMany(targetEntity = Student.class, mappedBy = "courses")
  private java.util.Collection<Student> students = new ArrayList<Student>();

  public java.util.Collection<Student> getStudents() {
    return students;
  }

  public void setStudents(java.util.Collection<Student> students) {
    this.students = students;
  }

  public String getCourseName() {
    return CourseName;
  }

  public void setCourseName(String courseName) {
    CourseName = courseName;
  }

  public Integer getCourseID() {
    return CourseID;
  }

  public void setCourseID(Integer courseID) {
    CourseID = courseID;
  }

}
