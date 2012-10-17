package com.sap.core.odata.core.experimental.edm.adapter;

import org.odata4j.edm.EdmComplexType;

import com.sap.core.odata.core.edm.EdmConcurrencyMode;
import com.sap.core.odata.core.edm.EdmContentKind;
import com.sap.core.odata.core.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.core.edm.EdmException;
import com.sap.core.odata.core.edm.EdmFacets;
import com.sap.core.odata.core.edm.EdmMapping;
import com.sap.core.odata.core.edm.EdmMultiplicity;
import com.sap.core.odata.core.edm.EdmProperty;
import com.sap.core.odata.core.edm.EdmTargetPath;
import com.sap.core.odata.core.edm.EdmType;

public class EdmPropertyAdapter extends EdmNamedAdapter implements EdmProperty {

  private org.odata4j.edm.EdmProperty edmProperty;

  public EdmPropertyAdapter(org.odata4j.edm.EdmProperty edmProperty) {
    super(edmProperty.getName());
    this.edmProperty = edmProperty;
  }

  @Override
  public EdmFacets getFacets() {
    return new EdmFacetsAdapter(
        this.edmProperty.isNullable(),
        this.edmProperty.getDefaultValue(),
        this.edmProperty.getMaxLength(),
        this.edmProperty.getFixedLength(),
        this.edmProperty.getPrecision(),
        this.edmProperty.getScale(),
        this.edmProperty.getUnicode(),
        this.edmProperty.getCollation(),
        this.edmProperty.getConcurrencyMode() == null ? null : EdmConcurrencyMode.valueOf(this.edmProperty.getConcurrencyMode()));
  }

  @Override
  public EdmType getType() {
    String fullQualifiedName = this.edmProperty.getType().getFullyQualifiedTypeName();
    if (this.edmProperty.getType().isSimple()) {
      return new EdmSimpleTypeAdapter(this.edmProperty.getType().getSimple(fullQualifiedName)).getType();
    } else {
      return new EdmComplexTypeAdapter((EdmComplexType) this.edmProperty.getType());
    }
  }

  @Override
  public EdmMultiplicity getMultiplicity() {
    return EdmMultiplicity.ONE;
  }

  @Override
  public EdmCustomizableFeedMappings getCustomizableFeedMappings() {
    return new EdmCustomizableFeedMappingsAdapter(
        this.edmProperty.getFcKeepInContent() == null ? null : Boolean.valueOf(this.edmProperty.getFcKeepInContent()),
        this.edmProperty.getFcContentKind() == null ? null : EdmContentKind.valueOf(this.edmProperty.getFcContentKind()),
        this.edmProperty.getFcNsPrefix(),
        this.edmProperty.getFcNsUri(),
        // TODO this.edmProperty.getfcSourcePath()
        null,
        this.edmProperty.getFcTargetPath() == null ? null : EdmTargetPath.valueOf(this.edmProperty.getFcTargetPath()));
  }

  @Override
  public String getMimeType() {
    return this.edmProperty.getMimeType();
  }

  @Override
  public EdmMapping getMapping() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }
}