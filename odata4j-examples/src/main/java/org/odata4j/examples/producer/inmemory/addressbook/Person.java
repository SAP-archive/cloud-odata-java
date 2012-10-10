package org.odata4j.examples.producer.inmemory.addressbook;

import org.joda.time.LocalDateTime;

public class Person {

  private int personId;
  private String name;
  private String emailAddress;
  private LocalDateTime birthDay;

  public Person(int personId, String name, String emailAddress, LocalDateTime birthDay) {
    this.personId = personId;
    this.name = name;
    this.emailAddress = emailAddress;
    this.birthDay = birthDay;
  }

  public int getPersonId() {
    return personId;
  }

  public void setPersonId(int personId) {
    this.personId = personId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public LocalDateTime getBirthDay() {
    return birthDay;
  }

  public void setBirthDay(LocalDateTime birthDay) {
    this.birthDay = birthDay;
  }
}
