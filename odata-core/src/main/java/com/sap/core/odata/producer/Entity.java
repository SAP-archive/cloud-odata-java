package com.sap.core.odata.producer;

public interface Entity {
  void read();
  void exists();
  void update();
  void delete();
}
