package com.sap.core.odata.core.producer;

public interface EntityLink {
  void read();
  void exists();
  void update();
  void delete();
}
