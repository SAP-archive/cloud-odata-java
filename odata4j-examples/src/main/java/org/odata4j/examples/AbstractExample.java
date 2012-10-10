package org.odata4j.examples;

import org.core4j.Enumerable;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntity;
import org.odata4j.core.OError;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmAssociation;
import org.odata4j.edm.EdmAssociationSet;
import org.odata4j.edm.EdmComplexType;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntityContainer;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.edm.EdmFunctionParameter;
import org.odata4j.edm.EdmNavigationProperty;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSchema;
import org.odata4j.edm.EdmSimpleType;

public abstract class AbstractExample {

  protected static void report(String msg) {
    System.out.println(msg);
  }

  protected static void report(String msg, Object... args) {
    System.out.println(String.format(msg, args));
  }

  protected static void reportEntity(String caption, OEntity entity) {
    report(caption);
    if (entity.getEntityTag() != null)
      report("  ETag: %s", entity.getEntityTag());
    for (OProperty<?> p : entity.getProperties()) {
      Object v = p.getValue();
      if (p.getType().equals(EdmSimpleType.BINARY) && v != null)
        v = org.odata4j.repack.org.apache.commons.codec.binary.Base64.encodeBase64String((byte[]) v).trim();
      report("  %s: %s", p.getName(), v);
    }
  }

  protected static int reportEntities(ODataConsumer c, String entitySetHref, int limit) {
    report("entitySetHref: " + entitySetHref);
    Enumerable<OEntity> entities = c.getEntities(entitySetHref).execute().take(limit);
    return reportEntities(entitySetHref, entities);
  }

  protected static int reportEntities(String entitySetHref, Enumerable<OEntity> entities) {

    int count = 0;

    for (OEntity e : entities) {
      reportEntity(entitySetHref + " entity" + count, e);
      count++;
    }
    report("total count: %s \n\n", count);

    return count;
  }

  private static void reportProperties(Iterable<EdmProperty> properties) {
    for (EdmProperty property : properties) {
      String p = String.format("Property Name=%s Type=%s Nullable=%s", property.getName(), property.getType(), property.isNullable());
      if (property.getMaxLength() != null)
        p = p + " MaxLength=" + property.getMaxLength();
      if (property.getUnicode() != null)
        p = p + " Unicode=" + property.getUnicode();
      if (property.getFixedLength() != null)
        p = p + " FixedLength=" + property.getFixedLength();

      if (property.getStoreGeneratedPattern() != null)
        p = p + " StoreGeneratedPattern=" + property.getStoreGeneratedPattern();
      if (property.getConcurrencyMode() != null)
        p = p + " ConcurrencyMode=" + property.getConcurrencyMode();

      if (property.getFcTargetPath() != null)
        p = p + " TargetPath=" + property.getFcTargetPath();
      if (property.getFcContentKind() != null)
        p = p + " ContentKind=" + property.getFcContentKind();
      if (property.getFcKeepInContent() != null)
        p = p + " KeepInContent=" + property.getFcKeepInContent();
      if (property.getFcContentKind() != null)
        p = p + " EpmContentKind=" + property.getFcContentKind();
      if (property.getFcEpmKeepInContent() != null)
        p = p + " EpmKeepInContent=" + property.getFcEpmKeepInContent();
      report("    " + p);
    }
  }

  protected static void reportMetadata(EdmDataServices services) {

    for (EdmSchema schema : services.getSchemas()) {
      report("Schema Namespace=%s, Alias=%s", schema.getNamespace(), schema.getAlias());

      for (EdmEntityType et : schema.getEntityTypes()) {
        String ets = String.format("  EntityType Name=%s", et.getName());
        if (et.getHasStream() != null)
          ets = ets + " HasStream=" + et.getHasStream();
        report(ets);

        for (String key : et.getKeys()) {
          report("    Key PropertyRef Name=%s", key);
        }

        reportProperties(et.getDeclaredProperties());
        for (EdmNavigationProperty np : et.getDeclaredNavigationProperties()) {
          report("    NavigationProperty Name=%s Relationship=%s FromRole=%s ToRole=%s",
              np.getName(), np.getRelationship().getFQNamespaceName(), np.getFromRole().getRole(), np.getToRole().getRole());
        }

      }
      for (EdmComplexType ct : schema.getComplexTypes()) {
        report("  ComplexType Name=%s", ct.getName());

        reportProperties(ct.getProperties());

      }
      for (EdmAssociation assoc : schema.getAssociations()) {
        report("  Association Name=%s", assoc.getName());
        report("    End Role=%s Type=%s Multiplicity=%s", assoc.getEnd1().getRole(), assoc.getEnd1().getType().getFullyQualifiedTypeName(), assoc.getEnd1().getMultiplicity());
        report("    End Role=%s Type=%s Multiplicity=%s", assoc.getEnd2().getRole(), assoc.getEnd2().getType().getFullyQualifiedTypeName(), assoc.getEnd2().getMultiplicity());
      }
      for (EdmEntityContainer ec : schema.getEntityContainers()) {
        report("  EntityContainer Name=%s IsDefault=%s LazyLoadingEnabled=%s", ec.getName(), ec.isDefault(), ec.getLazyLoadingEnabled());

        for (EdmEntitySet ees : ec.getEntitySets()) {
          report("    EntitySet Name=%s EntityType=%s", ees.getName(), ees.getType().getFullyQualifiedTypeName());
        }

        for (EdmAssociationSet eas : ec.getAssociationSets()) {
          report("    AssociationSet Name=%s Association=%s", eas.getName(), eas.getAssociation().getFQNamespaceName());
          report("      End Role=%s EntitySet=%s", eas.getEnd1().getRole().getRole(), eas.getEnd1().getEntitySet().getName());
          report("      End Role=%s EntitySet=%s", eas.getEnd2().getRole().getRole(), eas.getEnd2().getEntitySet().getName());
        }

        for (EdmFunctionImport efi : ec.getFunctionImports()) {
          report("    FunctionImport Name=%s EntitySet=%s ReturnType=%s HttpMethod=%s",
              efi.getName(), efi.getEntitySet() == null ? null : efi.getEntitySet().getName(), efi.getReturnType(), efi.getHttpMethod());
          for (EdmFunctionParameter efp : efi.getParameters()) {
            report("      Parameter Name=%s Type=%s Mode=%s", efp.getName(), efp.getType(), efp.getMode());
          }
        }
      }
    }
  }

  protected static void reportError(OError error) {
    report("Error code=%s", error.getCode());
    report("Error message=%s", error.getMessage());
    if (error.getInnerError() != null)
      report("Inner error=%s", error.getInnerError());
  }
}
