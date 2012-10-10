package org.odata4j.test.integration.producer.jpa.oneoff.oneoff01;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Ticket")
public class Ticket implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "TicketID")
  private Integer TicketID;
  @Basic(optional = false)
  @Column(name = "Description")
  private String Description;

  @OneToMany(cascade = CascadeType.ALL)
  private Collection<Comment> Comments = new ArrayList<Comment>();

  public Collection<Comment> getComments() {
    return Comments;
  }

  public void setComments(Collection<Comment> comments) {
    Comments = comments;
  }

  public Integer getTicketID() {
    return TicketID;
  }

  public void setTicketID(Integer ticketID) {
    TicketID = ticketID;
  }

  public String getDescription() {
    return Description;
  }

  public void setDescription(String description) {
    Description = description;
  }

}
