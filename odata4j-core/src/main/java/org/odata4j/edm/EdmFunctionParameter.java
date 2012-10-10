package org.odata4j.edm;

import org.odata4j.core.ImmutableList;

/**
 * A CSDL Parameter element.
 *
 * <p>A Parameter element is used to define input and output parameters for function imports that are declared in CSDL.
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ee473431.aspx">[msdn] Parameter Element (CSDL)</a>
 */
public class EdmFunctionParameter extends EdmItem {

  /** Parameter type: In, Out, or InOut */
  public enum Mode {
    In, Out, InOut;
  };

  private final String name;
  private final EdmType type;
  private final Mode mode;
  private final Boolean nullable;
  private final Integer maxLength;
  private final Integer precision;
  private final Integer scale;

  private EdmFunctionParameter(String name, EdmType type, Mode mode, Boolean nullable,
      Integer maxLength, Integer precision, Integer scale, EdmDocumentation doc,
      ImmutableList<EdmAnnotation<?>> annots, ImmutableList<EdmAnnotation<?>> annotElements) {
    super(doc, annots, annotElements);
    this.name = name;
    this.type = type;
    this.mode = mode;
    this.nullable = nullable;
    this.maxLength = maxLength;
    this.precision = precision;
    this.scale = scale;
  }

  public String getName() {
    return name;
  }

  public EdmType getType() {
    return type;
  }

  public Mode getMode() {
    return mode;
  }

  public Boolean isNullable() {
    return nullable;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public Integer getPrecision() {
    return precision;
  }

  public Integer getScale() {
    return scale;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(EdmFunctionParameter functionParameter, BuilderContext context) {
    return context.newBuilder(functionParameter, new Builder());
  }

  /** Mutable builder for {@link EdmFunctionParameter} objects. */
  public static class Builder extends EdmItem.Builder<EdmFunctionParameter, Builder> {

    private String name;
    private EdmType type;
    private EdmType.Builder<?, ?> typeBuilder;
    private Mode mode;
    private Boolean nullable;
    private Integer maxLength;
    private Integer precision;
    private Integer scale;

    @Override
    Builder newBuilder(EdmFunctionParameter functionParameter, BuilderContext context) {
      return new Builder().setName(functionParameter.name).setType(functionParameter.type).setMode(functionParameter.mode);
    }

    public EdmFunctionParameter build() {
      return new EdmFunctionParameter(name, typeBuilder != null ? typeBuilder.build() : type,
          mode, nullable, maxLength, precision, scale, getDocumentation(), ImmutableList.copyOf(getAnnotations()),
          ImmutableList.copyOf(getAnnotationElements()));
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setType(EdmType type) {
      this.type = type;
      return this;
    }

    public Builder setType(EdmType.Builder<?, ?> typeBuilder) {
      this.typeBuilder = typeBuilder;
      return this;
    }

    public Builder setMode(Mode mode) {
      this.mode = mode;
      return this;
    }

    public Builder setNullable(Boolean nullable) {
      this.nullable = nullable;
      return this;
    }

    public Builder setMaxLength(Integer maxLength) {
      this.maxLength = maxLength;
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

    public Builder input(String name, EdmType type) {
      this.mode = Mode.In;
      this.name = name;
      this.type = type;
      return this;
    }

  }

}
