package com.sap.core.odata.core.producer;

public interface Entity {
  void read();
  void exists();
  void update();
  void delete();
}
