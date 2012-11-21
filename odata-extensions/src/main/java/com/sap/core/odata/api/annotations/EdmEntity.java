package com.sap.core.odata.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EdmEntity {

  EdmType type() default @EdmType(EdmTypeKind.STRING);

  String name();

//  String name() default "";
//  String namespace() default "";
}
