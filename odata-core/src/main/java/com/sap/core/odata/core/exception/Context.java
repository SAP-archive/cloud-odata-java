package com.sap.core.odata.core.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 */
public abstract class Context {

  private final String key;
  private final List<Object> values;

  private Context(String key) {
    this.key = key;
    this.values = new ArrayList<Object>();
  }

  public static Context create(Class<? extends ODataCustomerException> clazz, String key) {
    return new SimpleContext(clazz.getName() + "." + key);
  }

  public String getKey() {
    return key;
  }
  
  public Context addValues(Object ... values) {
    for (Object val : values) {
      this.values.add(val);
    }
    return this;
  }
  
  public List<Object> getValues() {
    return Collections.unmodifiableList(values);
  }

  private static class SimpleContext extends Context {
    public SimpleContext(String implKey) {
      super(implKey);
    }
  }
}