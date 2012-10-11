package com.sap.core.odata.producer;

public interface EntityLink {
  void read();
  void exists();
  void update();
  void delete();
}
