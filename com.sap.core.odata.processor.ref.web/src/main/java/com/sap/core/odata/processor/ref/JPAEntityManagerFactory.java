package com.sap.core.odata.processor.ref;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAEntityManagerFactory {

  private static HashMap<String, EntityManagerFactory> emfMap;

  public static EntityManagerFactory getEntityManagerFactory(final String pUnit) {
    if (pUnit == null) {
      return null;
    }
    if (emfMap == null) {
      emfMap = new HashMap<String, EntityManagerFactory>();
    }

    if (emfMap.containsKey(pUnit)) {
      return emfMap.get(pUnit);
    } else
    {
      EntityManagerFactory emf = Persistence.createEntityManagerFactory(pUnit);
      emfMap.put(pUnit, emf);
      return emf;
    }

  }

  public static void closeAll() {

  }

  public static void closeMe(final String pUnit) {

  }
}
