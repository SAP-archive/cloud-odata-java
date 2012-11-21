package com.sap.core.odata.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EdmProperty {
  
  EdmType type() default @EdmType(EdmTypeKind.STRING);
  
  String name();

  String facet() default "";

}