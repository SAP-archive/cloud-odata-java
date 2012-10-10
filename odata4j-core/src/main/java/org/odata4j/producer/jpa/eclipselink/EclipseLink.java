package org.odata4j.producer.jpa.eclipselink;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.metamodel.SingularAttribute;

import org.core4j.CoreUtils;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.jpa.metamodel.AttributeImpl;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.edm.EdmType;

public class EclipseLink {

  private EclipseLink() {}

  public static Map<String, Object> getPropertyInfo(SingularAttribute<?, ?> sa, EdmType type) {
    Map<String, Object> rt = new HashMap<String, Object>();
    AttributeImpl<?, ?> ai = (AttributeImpl<?, ?>) sa;
    DatabaseMapping dm = CoreUtils.getFieldValue(ai, "mapping", DatabaseMapping.class);
    DatabaseField df = dm.getField();
    if (df != null && EdmSimpleType.STRING.equals(type)) {
      rt.put("MaxLength", df.getLength());
    }
    return rt;
  }

}
