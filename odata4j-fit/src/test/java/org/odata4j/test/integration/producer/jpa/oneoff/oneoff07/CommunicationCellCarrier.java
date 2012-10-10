package org.odata4j.test.integration.producer.jpa.oneoff.oneoff07;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CommunicationCellCarrier")
public class CommunicationCellCarrier implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Basic(optional = false)
  @Column(name = "CellCarrierId")
  private UUID id;

  @Basic
  @Column(name = "CellCarrierName")
  private String name;

  @Basic
  @Column(name = "SMSMailServer")
  private String smsMailServer;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSmsMailServer() {
    return smsMailServer;
  }

  public void setSmsMailServer(String smsMailServer) {
    this.smsMailServer = smsMailServer;
  }

}
