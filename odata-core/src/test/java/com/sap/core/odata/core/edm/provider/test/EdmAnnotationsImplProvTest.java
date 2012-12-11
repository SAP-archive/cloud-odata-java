package com.sap.core.odata.core.edm.provider.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmAnnotationAttribute;
import com.sap.core.odata.api.edm.EdmAnnotationElement;
import com.sap.core.odata.api.edm.provider.AnnotationAttribute;
import com.sap.core.odata.api.edm.provider.AnnotationElement;
import com.sap.core.odata.core.edm.provider.EdmAnnotationsImplProv;

public class EdmAnnotationsImplProvTest {

  private static EdmAnnotationsImplProv annotationsProvider;

  @BeforeClass
  public static void getEdmEntityContainerImpl() throws Exception {

    List<AnnotationAttribute> annotationAttributes = new ArrayList<AnnotationAttribute>();
    AnnotationAttribute attribute = new AnnotationAttribute().setName("attributeName").setNamespace("namespace").setPrefix("prefix").setText("Text");
    annotationAttributes.add(attribute);

    List<AnnotationElement> annotationElements = new ArrayList<AnnotationElement>();
    AnnotationElement element = new AnnotationElement().setName("elementName").setNamespace("namespace").setPrefix("prefix").setXmlData("xmlData");
    annotationElements.add(element);

    annotationsProvider = new EdmAnnotationsImplProv(annotationAttributes, annotationElements);

  }

  @Test
  public void testAttributes() {
    List<? extends EdmAnnotationAttribute> annotations = annotationsProvider.getAnnotationAttributes();
    assertEquals(1, annotations.size());

    Iterator<? extends EdmAnnotationAttribute> iterator = annotations.iterator();
    while (iterator.hasNext()) {
      EdmAnnotationAttribute attribute = iterator.next();
      assertEquals("attributeName", attribute.getName());
      assertEquals("namespace", attribute.getNamespace());
      assertEquals("prefix", attribute.getPrefix());
      assertEquals("Text", attribute.getText());
    }
  }

  @Test
  public void testAttribute() {
    EdmAnnotationAttribute attribute = annotationsProvider.getAnnotationAttribute("attributeName", "namespace");
    assertEquals("attributeName", attribute.getName());
    assertEquals("namespace", attribute.getNamespace());
    assertEquals("prefix", attribute.getPrefix());
    assertEquals("Text", attribute.getText());
  }

  @Test
  public void testAttributeNull() {
    EdmAnnotationAttribute attribute = annotationsProvider.getAnnotationAttribute("attributeNameWrong", "namespaceWrong");
    assertNull(attribute);
  }

  @Test
  public void testElements() {
    List<? extends EdmAnnotationElement> annotations = annotationsProvider.getAnnotationElements();
    assertEquals(1, annotations.size());

    Iterator<? extends EdmAnnotationElement> iterator = annotations.iterator();
    while (iterator.hasNext()) {
      EdmAnnotationElement element = iterator.next();
      assertEquals("elementName", element.getName());
      assertEquals("namespace", element.getNamespace());
      assertEquals("prefix", element.getPrefix());
      assertEquals("xmlData", element.getXmlData());
    }
  }

  @Test
  public void testElement() {
    EdmAnnotationElement element = annotationsProvider.getAnnotationElement("elementName", "namespace");
    assertEquals("elementName", element.getName());
    assertEquals("namespace", element.getNamespace());
    assertEquals("prefix", element.getPrefix());
    assertEquals("xmlData", element.getXmlData());
  }

  @Test
  public void testElementNull() {
    EdmAnnotationElement element = annotationsProvider.getAnnotationElement("elementNameWrong", "namespaceWrong");
    assertNull(element);
  }

}
