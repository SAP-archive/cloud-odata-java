package org.odata4j.test.integration.producer.jpa.oneoff.oneoff01;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Comment")
public class Comment implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "CommentID")
  private Integer CommentID;
  @Basic(optional = false)
  @Column(name = "Description")
  private String Description;

  //    @ManyToOne
  //    private Ticket ticket;
  //    
  // public Ticket getTicket() {
  //  return ticket;
  // }
  // public void setTicket(Ticket ticket) {
  //  this.ticket = ticket;
  // }
  public Integer getCommentID() {
    return CommentID;
  }

  public void setCommentID(Integer commentID) {
    CommentID = commentID;
  }

  public String getDescription() {
    return Description;
  }

  public void setDescription(String description) {
    Description = description;
  }
}
