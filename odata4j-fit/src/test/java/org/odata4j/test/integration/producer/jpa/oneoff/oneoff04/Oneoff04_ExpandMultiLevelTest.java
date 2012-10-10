package org.odata4j.test.integration.producer.jpa.oneoff.oneoff04;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperties;
import org.odata4j.core.ORelatedEntitiesLinkInline;
import org.odata4j.test.integration.producer.jpa.oneoff.AbstractOneoffBaseTest;

public class Oneoff04_ExpandMultiLevelTest extends AbstractOneoffBaseTest {

  private ODataConsumer consumer;

  public Oneoff04_ExpandMultiLevelTest(RuntimeFacadeType type) {
    super(type);
  }

  @Test
  public void expandMultiLevel() throws Exception {
    consumer = rtFacade.createODataConsumer(endpointUri, null);

    List<OEntity> coursesOneAndTwo = new ArrayList<OEntity>();
    addLocallyCreatedCourseEntity(coursesOneAndTwo, "Course1");
    addLocallyCreatedCourseEntity(coursesOneAndTwo, "Course2");

    List<OEntity> coursesThreeAndFour = new ArrayList<OEntity>();
    addLocallyCreatedCourseEntity(coursesThreeAndFour, "Course3");
    addLocallyCreatedCourseEntity(coursesThreeAndFour, "Course4");

    List<OEntity> studentsOneAndTwo = new ArrayList<OEntity>();
    addLocallyCreatedStudentEntity(studentsOneAndTwo, "Student1", coursesOneAndTwo);
    addLocallyCreatedStudentEntity(studentsOneAndTwo, "Student2", coursesThreeAndFour);

    createSchoolEntityWithStudentsAndCoursesInlined("School1", studentsOneAndTwo);

    // ensure correct entities count
    assertThat(consumer.getEntities("School").execute().count(), is(1));
    assertThat(consumer.getEntities("Student").execute().count(), is(2));
    assertThat(consumer.getEntities("Course").execute().count(), is(4));

    // retrieve expanded 'School' entity
    OEntity school = consumer.getEntities("School").expand("students/courses").execute().first();
    assertThat(getName(school, "SchoolName"), is("School1"));

    // get inlined 'Student' entities
    List<OEntity> students = school.getLink("students", ORelatedEntitiesLinkInline.class).getRelatedEntities();
    assertThat(getNames(students, "StudentName"), hasItems("Student1", "Student2"));

    // get inlined 'Course' entities (for each 'Student')
    List<OEntity> courses = new ArrayList<OEntity>();
    for (OEntity student : students)
      courses.addAll(student.getLink("courses", ORelatedEntitiesLinkInline.class).getRelatedEntities());
    assertThat(getNames(courses, "CourseName"), hasItems("Course1", "Course2", "Course3", "Course4"));
  }

  private void addLocallyCreatedCourseEntity(List<OEntity> courses, String courseName) {
    courses.add(consumer
        .createEntity("Course")
        .properties(OProperties.string("CourseName", courseName))
        .get());
  }

  private void addLocallyCreatedStudentEntity(List<OEntity> students, String studentName, List<OEntity> courses) {
    students.add(consumer
        .createEntity("Student")
        .properties(OProperties.string("StudentName", studentName))
        .inline("courses", courses)
        .get());
  }

  private void createSchoolEntityWithStudentsAndCoursesInlined(String schoolName, List<OEntity> students) throws Exception {
    consumer
        .createEntity("School")
        .properties(OProperties.string("SchoolName", schoolName))
        .inline("students", students)
        .execute();
  }

  private String getName(OEntity entity, String propertyName) {
    return (String) entity.getProperty(propertyName).getValue();
  }

  private List<String> getNames(List<OEntity> entities, String propertyName) {
    List<String> names = new ArrayList<String>();
    for (OEntity entity : entities)
      names.add(getName(entity, propertyName));
    return names;
  }
}
