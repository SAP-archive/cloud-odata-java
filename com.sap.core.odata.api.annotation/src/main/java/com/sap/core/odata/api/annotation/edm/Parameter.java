package com.sap.core.odata.api.annotation.edm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Parameter {
  enum Mode {
    IN {
      @Override
      public String toString() {
        return new String("In");
      }
    },
    OUT {
      @Override
      public String toString() {
        return new String("Out");
      }
    },
    INOUT {
      @Override
      public String toString() {
        return new String("InOut");
      }
    }
  };

  String name();

  Mode mode() default Mode.IN;

  Facets facets() default @Facets;

  Documentation documentation() default @Documentation;
}
