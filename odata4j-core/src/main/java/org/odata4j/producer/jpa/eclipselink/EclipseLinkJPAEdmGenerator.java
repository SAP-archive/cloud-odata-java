package org.odata4j.producer.jpa.eclipselink;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.SingularAttribute;

import org.core4j.Enumerable;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.edm.EdmType;
import org.odata4j.producer.jpa.JPAEdmGenerator;

public class EclipseLinkJPAEdmGenerator extends JPAEdmGenerator {

  public EclipseLinkJPAEdmGenerator(EntityManagerFactory emf, String namespace) {
    super(emf, namespace);
  }

  protected EdmProperty.Builder toEdmProperty(String modelNamespace, SingularAttribute<?, ?> sa) {
    EdmProperty.Builder p = super.toEdmProperty(modelNamespace, sa);

    Integer maxLength = null;
    Map<String, Object> eclipseLinkProps = EclipseLink.getPropertyInfo(sa, p.getType());
    if (Enumerable.<EdmType> create(EdmSimpleType.STRING, EdmSimpleType.BINARY).contains(p.getType())
        && eclipseLinkProps.containsKey("MaxLength"))
      maxLength = (Integer) eclipseLinkProps.get("MaxLength");

    return p.setMaxLength(maxLength);
  }

}
