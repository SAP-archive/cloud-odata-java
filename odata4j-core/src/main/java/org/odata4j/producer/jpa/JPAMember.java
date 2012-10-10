package org.odata4j.producer.jpa;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import org.core4j.CoreUtils;
import org.odata4j.core.Throwables;

public abstract class JPAMember {

  private JPAMember() {}

  public abstract Class<?> getJavaType();

  public abstract boolean isReadable();

  public abstract boolean isWriteable();

  public abstract <T> T get();

  public abstract <T> void set(T value);

  public abstract <T extends Annotation> T getAnnotation(Class<T> annotationClass);

  public static JPAMember create(Attribute<?, ?> jpaAttribute, Object target) {
    Member javaMember = jpaAttribute.getJavaMember();
    if (javaMember instanceof Field)
      return new FieldMember((Field) javaMember, target);
    if (javaMember instanceof Method)
      return new GetterSetterMember((Method) javaMember, null, target);

    // http://wiki.eclipse.org/EclipseLink/Development/JPA_2.0/metamodel_api#DI_95:_20091017:_Attribute.getJavaMember.28.29_returns_null_for_a_BasicType_on_a_MappedSuperclass_because_of_an_uninitialized_accessor
    JPAMember rt = reverseEngineerJPAMember(jpaAttribute.getDeclaringType().getJavaType(), jpaAttribute.getName(), target);

    if (rt == null)
      throw new IllegalArgumentException("Could not find java member for: " + jpaAttribute);
    return rt;
  }

  public static JPAMember findByColumn(EntityType<?> jpaEntityType, String columnName, Object jpaEntity) {
    for (Attribute<?, ?> att : jpaEntityType.getAttributes()) {
      if (att.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {
        SingularAttribute<?, ?> eatt = (SingularAttribute<?, ?>) att;

        EmbeddableType<?> et = (EmbeddableType<?>) eatt.getType();
        Object eo = JPAMember.create(att, jpaEntity).get();
        for (Attribute<?, ?> embeddedAtt : et.getAttributes()) {
          JPAMember rt = findByColumn(embeddedAtt, columnName, eo);
          if (rt != null)
            return rt;
        }
      }
      JPAMember rt = findByColumn(att, columnName, jpaEntity);
      if (rt != null)
        return rt;
    }
    return null;
  }

  private static JPAMember findByColumn(Attribute<?, ?> att, String columnName, Object target) {
    JPAMember rt = JPAMember.create(att, target);
    Column c = rt.getAnnotation(Column.class);
    if (c == null)
      return null;
    if (columnName.equals(c.name()))
      return rt;
    return null;

  }

  private static JPAMember reverseEngineerJPAMember(Class<?> type, String name, Object target) {
    try {
      Field field = CoreUtils.getField(type, name);
      return new FieldMember(field, target);
    } catch (Exception ignore) {}

    // TODO handle setters, overloads
    String methodName = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    while (!type.equals(Object.class)) {
      try {
        Method method = type.getDeclaredMethod(methodName);
        return new GetterSetterMember(method, null, target);
      } catch (Exception ignore) {}
      type = type.getSuperclass();
    }
    return null;
  }

  private static class FieldMember extends JPAMember {

    private final Field field;
    private final Object target;

    public FieldMember(Field field, Object target) {
      this.field = field;
      this.target = target;
      field.setAccessible(true);
    }

    @Override
    public Class<?> getJavaType() {
      return field.getType();
    }

    @Override
    public boolean isReadable() {
      return true;
    }

    @Override
    public boolean isWriteable() {
      return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get() {
      try {
        return (T) field.get(target);
      } catch (Exception e) {
        throw Throwables.propagate(e);
      }
    }

    @Override
    public <T> void set(T value) {
      try {
        field.set(target, value);
      } catch (Exception e) {
        throw Throwables.propagate(e);
      }
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
      return field.getAnnotation(annotationClass);
    }
  }

  private static class GetterSetterMember extends JPAMember {

    private final Method getter;
    private final Method setter;
    private final Object target;

    public GetterSetterMember(Method getter, Method setter, Object target) {
      this.getter = getter;
      this.setter = setter;
      this.target = target;

      if (getter != null)
        getter.setAccessible(true);
      if (setter != null)
        setter.setAccessible(true);
    }

    @Override
    public Class<?> getJavaType() {
      return getter.getReturnType();
    }

    @Override
    public boolean isReadable() {
      return getter != null;
    }

    @Override
    public boolean isWriteable() {
      return setter != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get() {
      if (getter == null)
        throw new RuntimeException("Member is not readable");
      try {
        return (T) getter.invoke(target);
      } catch (Exception e) {
        throw Throwables.propagate(e);
      }
    }

    @Override
    public <T> void set(T value) {
      if (setter == null)
        throw new RuntimeException("Member is not writeable");
      try {
        setter.invoke(target, value);
      } catch (Exception e) {
        throw Throwables.propagate(e);
      }
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
      return getter.getAnnotation(annotationClass);
    }
  }

}
