package org.odata4j.examples.producer.jpa;

public enum JPAProvider {
  ECLIPSELINK("EclipseLink"),
  HIBERNATE("Hibernate");

  public final String caption;

  JPAProvider(String caption) {
    this.caption = caption;
  }

  public static final JPAProvider JPA_PROVIDER;

  static {
    String prop = System.getProperty("jpa");
    if (JPAProvider.ECLIPSELINK.caption.equalsIgnoreCase(prop))
      JPA_PROVIDER = JPAProvider.ECLIPSELINK;
    else if (JPAProvider.HIBERNATE.caption.equalsIgnoreCase(prop))
      JPA_PROVIDER = JPAProvider.HIBERNATE;
    else
      JPA_PROVIDER = JPAProvider.ECLIPSELINK;
  }

}