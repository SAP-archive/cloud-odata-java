package org.odata4j.test.integration.producer.jpa.oneoff.oneoff04;

import java.io.Serializable;
import java.util.HashSet;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "School")
public class School implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "SchoolID")
  private Integer SchoolID;

  @OneToMany
  private java.util.Collection<Student> students = new HashSet<Student>();

  @Basic(optional = false)
  @Column(name = "SchoolName")
  private String SchoolName;

  public Integer getSchoolID() {
    return SchoolID;
  }

  public void setSchoolID(Integer schoolID) {
    SchoolID = schoolID;
  }

  public java.util.Collection<Student> getStudents() {
    return students;
  }

  public void setStudents(java.util.Collection<Student> students) {
    this.students = students;
  }

  public String getSchoolName() {
    return SchoolName;
  }

  public void setSchoolName(String schoolName) {
    SchoolName = schoolName;
  }

}
