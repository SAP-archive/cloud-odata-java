package org.odata4j.edm;

import org.odata4j.core.ImmutableList;
import org.odata4j.core.Named;

/**
 * A CSDL Property element.
 *
 * <p>Property elements define the shape and characteristics of data that an entity type instance or complex type
 * instance will contain. Properties in a conceptual model are analogous to properties that are defined on a class.
 * In the same way that properties on a class define the shape of the class and carry information about objects,
 * properties in a conceptual model define the shape of an entity type and carry information about entity type instances.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/bb399546.aspx">[msdn] Property Element (CSDL)</a>
 */
public class EdmProperty extends EdmPropertyBase {

  /** Kind of collection: List, Bag, or Collection */
  public enum CollectionKind {
    NONE,
    // note that the toString() of these enum values is used in $metadata generation
    // so case matters.

    // CSDL is inconsistent:
    List, // used in Property
    Bag, // used in Property
    Collection // used in FunctionImport return types and parameter types
  }

  private final EdmStructuralType declaringType;
  private final EdmType type;
  private final boolean nullable;
  private final Integer maxLength;
  private final Boolean unicode;
  private final Boolean fixedLength;
  private final String storeGeneratedPattern;
  private final String concurrencyMode;
  private final CollectionKind collectionKind;
  private final String defaultValue;
  private final Integer precision;
  private final Integer scale;
  private final String collation;

  private final String fcTargetPath;
  private final String fcContentKind;
  private final String fcKeepInContent;
  private final String fcEpmContentKind;
  private final String fcEpmKeepInContent;
  private final String fcNsPrefix;
  private final String fcNsUri;
  private final String mimeType;

  private EdmProperty(EdmDocumentation documentation, ImmutableList<EdmAnnotation<?>> annotations, ImmutableList<EdmAnnotation<?>> annotElements,
      String name, EdmStructuralType declaringType, EdmType type, boolean nullable, Integer maxLength, Boolean unicode, Boolean fixedLength,
      String storeGeneratedPattern, String concurrencyMode, String fcTargetPath, String fcContentKind, String fcKeepInContent, String fcEpmContentKind,
      String fcEpmKeepInContent, String fcNsPrefix, String fcNsUri, CollectionKind collectionKind, String defaultValue,
      Integer precision, Integer scale, String collation, String mimeType) {
    super(documentation, annotations, annotElements, name);
    this.declaringType = declaringType;
    this.type = type;
    this.nullable = nullable;
    this.maxLength = maxLength;
    this.unicode = unicode;
    this.fixedLength = fixedLength;
    this.storeGeneratedPattern = storeGeneratedPattern;
    this.collectionKind = collectionKind;
    this.defaultValue = defaultValue;
    this.precision = precision;
    this.scale = scale;
    this.collation = collation;

    this.fcTargetPath = fcTargetPath;
    this.fcContentKind = fcContentKind;
    this.fcKeepInContent = fcKeepInContent;
    this.fcEpmContentKind = fcEpmContentKind;
    this.fcEpmKeepInContent = fcEpmKeepInContent;
    this.fcNsPrefix = fcNsPrefix;
    this.fcNsUri = fcNsUri;
    this.mimeType = mimeType;
    this.concurrencyMode = concurrencyMode;
  }

  public EdmType getType() {
    return type;
  }

  public boolean isNullable() {
    return nullable;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public Boolean getUnicode() {
    return unicode;
  }

  public Boolean getFixedLength() {
    return fixedLength;
  }

  public String getStoreGeneratedPattern() {
    return storeGeneratedPattern;
  }

  public CollectionKind getCollectionKind() {
    return collectionKind;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public Integer getPrecision() {
    return precision;
  }

  public Integer getScale() {
    return scale;
  }

  public String getCollation() {
    return collation;
  }

  public String getFcTargetPath() {
    return fcTargetPath;
  }

  public String getFcContentKind() {
    return fcContentKind;
  }

  public String getFcKeepInContent() {
    return fcKeepInContent;
  }

  public String getFcEpmContentKind() {
    return fcEpmContentKind;
  }

  public String getFcEpmKeepInContent() {
    return fcEpmKeepInContent;
  }

  public String getFcNsPrefix() {
    return fcNsPrefix;
  }

  public String getFcNsUri() {
    return fcNsUri;
  }

  public String getMimeType() {
    return mimeType;
  }

  public String getConcurrencyMode() {
    return concurrencyMode;
  }

  @Override
  public String toString() {
    return String.format("EdmProperty[%s,%s]", getName(), type);
  }

  public EdmStructuralType getDeclaringType() {
    return this.declaringType;
  }

  public static EdmProperty.Builder newBuilder(String name) {
    return new Builder(name);
  }

  static Builder newBuilder(EdmProperty property, BuilderContext context) {
    return context.newBuilder(property, new Builder(property.getName()));
  }

  /** Mutable builder for {@link EdmProperty} objects. */
  public static class Builder extends EdmPropertyBase.Builder<EdmProperty, Builder> implements Named {

    private EdmStructuralType declaringType;
    private EdmType type;
    private EdmType.Builder<?, ?> typeBuilder;
    private boolean nullable;
    private Integer maxLength;
    private Boolean unicode;
    private Boolean fixedLength;
    private String storeGeneratedPattern;
    private CollectionKind collectionKind = CollectionKind.NONE;
    private String defaultValue;
    private Integer precision;
    private Integer scale;
    private String collation;

    private String fcTargetPath;
    private String fcContentKind;
    private String fcKeepInContent;
    private String fcEpmContentKind;
    private String fcEpmKeepInContent;
    private String fcNsPrefix;
    private String fcNsUri;

    private String mimeType;
    private String concurrencyMode;

    private EdmProperty builtProperty;

    private Builder(String name) {
      super(name);
    }

    @Override
    Builder newBuilder(EdmProperty property, BuilderContext context) {
      this.declaringType = property.declaringType;
      this.type = property.type;
      if (type != null) {
        if (!type.isSimple()) {
          // we want to use the re-built version of this type, not the original object
          this.typeBuilder = EdmType.newDeferredBuilder(type.getFullyQualifiedTypeName(), context.getDataServices());
          type = null;
        }
      }
      this.nullable = property.nullable;
      this.maxLength = property.maxLength;
      this.unicode = property.unicode;
      this.fixedLength = property.fixedLength;
      this.storeGeneratedPattern = property.storeGeneratedPattern;
      this.collectionKind = property.collectionKind;
      this.defaultValue = property.defaultValue;
      this.precision = property.precision;
      this.scale = property.scale;
      this.collation = property.collation;

      this.fcTargetPath = property.fcTargetPath;
      this.fcContentKind = property.fcContentKind;
      this.fcKeepInContent = property.fcKeepInContent;
      this.fcEpmContentKind = property.fcEpmContentKind;
      this.fcEpmKeepInContent = property.fcEpmKeepInContent;
      this.fcNsPrefix = property.fcNsPrefix;
      this.fcNsUri = property.fcNsUri;
      this.mimeType = property.mimeType;
      this.concurrencyMode = property.concurrencyMode;
      return this;
    }

    public EdmProperty build() {
      if (builtProperty == null) {
        EdmType type = this.type != null ? this.type : typeBuilder.build();
        builtProperty = new EdmProperty(getDocumentation(), ImmutableList.copyOf(getAnnotations()),
            ImmutableList.copyOf(getAnnotationElements()), getName(), declaringType, type, nullable, maxLength, unicode,
            fixedLength, storeGeneratedPattern,
            concurrencyMode, fcTargetPath, fcContentKind, fcKeepInContent, fcEpmContentKind, fcEpmKeepInContent, fcNsPrefix,
            fcNsUri, collectionKind, defaultValue, precision, scale, collation, mimeType);
      }
      return builtProperty;
    }

    public Builder setType(EdmType type) {
      this.type = type;
      return this;
    }

    public Builder setType(EdmType.Builder<?, ?> type) {
      this.typeBuilder = type;
      return this;
    }

    public Builder setNullable(boolean nullable) {
      this.nullable = nullable;
      return this;
    }

    public Builder setDeclaringType(EdmStructuralType declaringType) {
      this.declaringType = declaringType;
      return this;
    }

    public Builder setMaxLength(Integer maxLength) {
      this.maxLength = maxLength;
      return this;
    }

    public Builder setUnicode(Boolean unicode) {
      this.unicode = unicode;
      return this;
    }

    public Builder setFixedLength(Boolean fixedLength) {
      this.fixedLength = fixedLength;
      return this;
    }

    public Builder setStoreGeneratedPattern(String storeGeneratedPattern) {
      this.storeGeneratedPattern = storeGeneratedPattern;
      return this;
    }

    public Builder setFcTargetPath(String fcTargetPath) {
      this.fcTargetPath = fcTargetPath;
      return this;
    }

    public Builder setFcContentKind(String fcContentKind) {
      this.fcContentKind = fcContentKind;
      return this;
    }

    public Builder setFcKeepInContent(String fcKeepInContent) {
      this.fcKeepInContent = fcKeepInContent;
      return this;
    }

    public Builder setFcEpmContentKind(String fcEpmContentKind) {
      this.fcEpmContentKind = fcEpmContentKind;
      return this;
    }

    public Builder setFcEpmKeepInContent(String fcEpmKeepInContent) {
      this.fcEpmKeepInContent = fcEpmKeepInContent;
      return this;
    }

    public Builder setFcNsPrefix(String fcNsPrefix) {
      this.fcNsPrefix = fcNsPrefix;
      return this;
    }

    public Builder setFcNsUri(String fcNsUri) {
      this.fcNsUri = fcNsUri;
      return this;
    }

    public Builder setCollectionKind(CollectionKind collectionKind) {
      this.collectionKind = collectionKind;
      return this;
    }

    public Builder setDefaultValue(String defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    public Builder setPrecision(Integer precision) {
      this.precision = precision;
      return this;
    }

    public Builder setScale(Integer scale) {
      this.scale = scale;
      return this;
    }

    public Builder setCollation(String collation) {
      this.collation = collation;
      return this;
    }

    public Builder setMimeType(String mimeType) {
      this.mimeType = mimeType;
      return this;
    }

    public Builder setConcurrencyMode(String concurrencyMode) {
      this.concurrencyMode = concurrencyMode;
      return this;
    }

    public EdmType getType() {
      return type;
    }

    public boolean isNullable() {
      return nullable;
    }

    public Integer getMaxLength() {
      return maxLength;
    }

    public Boolean getUnicode() {
      return unicode;
    }

    public Boolean getFixedLength() {
      return fixedLength;
    }

    public String getStoreGeneratedPattern() {
      return storeGeneratedPattern;
    }

    public CollectionKind getCollectionKind() {
      return collectionKind;
    }

    public String getDefaultValue() {
      return defaultValue;
    }

    public Integer getPrecision() {
      return precision;
    }

    public Integer getScale() {
      return scale;
    }

    public String getCollation() {
      return collation;
    }

    public String getFcTargetPath() {
      return fcTargetPath;
    }

    public String getFcContentKind() {
      return fcContentKind;
    }

    public String getFcKeepInContent() {
      return fcKeepInContent;
    }

    public String getFcEpmContentKind() {
      return fcEpmContentKind;
    }

    public String getFcEpmKeepInContent() {
      return fcEpmKeepInContent;
    }

    public String getFcNsPrefix() {
      return fcNsPrefix;
    }

    public String getFcNsUri() {
      return fcNsUri;
    }

    public String getMimeType() {
      return mimeType;
    }

    public String getConcurrencyMode() {
      return concurrencyMode;
    }

  }

}
