package org.odata4j.examples.producer.jpa.addressbook;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Persons {

  @Id
  private int PersonId;
  private String Name;
  private String EmailAddress;
  @Temporal(TemporalType.DATE)
  private Date BirthDay;

  public int getPersonId() {
    return PersonId;
  }

  public void setPersonId(int PersonId) {
    this.PersonId = PersonId;
  }

  public String getName() {
    return Name;
  }

  public void setName(String Name) {
    this.Name = Name;
  }

  public String getEmailAddress() {
    return EmailAddress;
  }

  public void setEmailAddress(String EmailAddress) {
    this.EmailAddress = EmailAddress;
  }

  public Date getBirthDay() {
    return BirthDay;
  }

  public void setBirthDay(Date BirthDay) {
    this.BirthDay = BirthDay;
  }
}
