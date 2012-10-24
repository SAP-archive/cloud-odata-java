package com.sap.core.odata.core.edm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;

public class EdmSimpleTypeTest {

  
  private void testCompability(EdmSimpleType type, ArrayList<EdmSimpleType> typeList){
    for(EdmSimpleType compatible : typeList){
      assertTrue(type.isCompatible(compatible));   
    }
  }
  @Test
  public void testBoolean() {

  }

    @Test
    public void testNames() throws EdmException {
  
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      assertEquals("Binary", facade.binaryInstance().getName());
      assertEquals("Boolean", facade.booleanInstance().getName());
      assertEquals("Byte", facade.byteInstance().getName());
      assertEquals("DateTime", facade.dateTimeInstance().getName());
      assertEquals("DateTimeOffset", facade.dateTimeOffsetInstance().getName());
      assertEquals("Decimal", facade.decimalInstance().getName());
      assertEquals("Double", facade.doubleInstance().getName());
      assertEquals("Guid", facade.guidInstance().getName());
      assertEquals("Int16", facade.int16Instance().getName());
      assertEquals("Int32", facade.int32Instance().getName());
      assertEquals("Int64", facade.int64Instance().getName());
      assertEquals("SByte", facade.sByteInstance().getName());
      assertEquals("Single", facade.singleInstance().getName());
      assertEquals("String", facade.stringInstance().getName());
      assertEquals("Time", facade.timeInstance().getName());
    }
  
    @Test
    public void testBinaryCompatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.binaryInstance());     
      
      testCompability(facade.binaryInstance(), typeList);
    }
    
    @Test
    public void testBooleanCompatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.booleanInstance());
      typeList.add(facade.getInstance(EdmSimpleTypeKind.BIT));      
      
      testCompability(facade.booleanInstance(), typeList);
    }
    
    @Test
    public void testByteCompatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.byteInstance());
      typeList.add(facade.getInstance(EdmSimpleTypeKind.BIT));
      typeList.add(facade.getInstance(EdmSimpleTypeKind.UINT7));
      
      testCompability(facade.byteInstance(), typeList);
    }
    
    @Test
    public void testDateTimeCompatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.dateTimeInstance());      
      
      testCompability(facade.dateTimeInstance(), typeList);
    }
  

    @Test
    public void testDateTimeOffsetCompatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.dateTimeOffsetInstance());
      
      testCompability(facade.dateTimeOffsetInstance(), typeList);
    }
    
    @Test
    public void testDecimalCompatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.BIT));      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.UINT7));
      typeList.add(facade.byteInstance());
      typeList.add(facade.sByteInstance());
      typeList.add(facade.int16Instance());
      typeList.add(facade.int32Instance());
      typeList.add(facade.int64Instance());
      typeList.add(facade.singleInstance());
      typeList.add(facade.doubleInstance());
      typeList.add(facade.decimalInstance());
      
      testCompability(facade.decimalInstance(), typeList);
    }
    
    @Test
    public void testDoubleCompatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.BIT));      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.UINT7));
      typeList.add(facade.byteInstance());
      typeList.add(facade.sByteInstance());
      typeList.add(facade.int16Instance());
      typeList.add(facade.int32Instance());
      typeList.add(facade.int64Instance());
      typeList.add(facade.singleInstance());
      typeList.add(facade.doubleInstance());    
      
      testCompability(facade.doubleInstance(), typeList);
    }
    
    @Test
    public void testGuidCompatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.guidInstance());
      
      testCompability(facade.guidInstance(), typeList);
    }
    
    @Test
    public void testint16Compatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.BIT));      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.UINT7));
      typeList.add(facade.byteInstance());
      typeList.add(facade.sByteInstance());
      typeList.add(facade.int16Instance());
      
      testCompability(facade.int16Instance(), typeList);
    }
    
    @Test
    public void testInt32Compatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.BIT));      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.UINT7));
      typeList.add(facade.byteInstance());
      typeList.add(facade.sByteInstance());
      typeList.add(facade.int16Instance());
      typeList.add(facade.int32Instance()); 
      
      testCompability(facade.int32Instance(), typeList);
    }
    
    
    @Test
    public void testInt64Compatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.BIT));      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.UINT7));
      typeList.add(facade.byteInstance());
      typeList.add(facade.sByteInstance());
      typeList.add(facade.int16Instance());
      typeList.add(facade.int32Instance());
      typeList.add(facade.int64Instance());
      
      testCompability(facade.int64Instance(), typeList);
    }
    
    
    @Test
    public void testSByteCompatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.BIT));      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.UINT7));
      typeList.add(facade.sByteInstance());
      
      testCompability(facade.sByteInstance(), typeList);
    }
    
    
    @Test
    public void testSingleCompatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.BIT));      
      typeList.add(facade.getInstance(EdmSimpleTypeKind.UINT7));
      typeList.add(facade.byteInstance());
      typeList.add(facade.sByteInstance());
      typeList.add(facade.int16Instance());
      typeList.add(facade.int32Instance());
      typeList.add(facade.int64Instance());
      typeList.add(facade.singleInstance());
      
      testCompability(facade.singleInstance(), typeList);
    }
    
    @Test
    public void testStringCompatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.stringInstance());
      
      testCompability(facade.stringInstance(), typeList);
    }
    
    @Test
    public void testTimeCompatibility() {
      EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
      ArrayList<EdmSimpleType> typeList = new ArrayList<EdmSimpleType>();
      
      typeList.add(facade.timeInstance());
      
      testCompability(facade.timeInstance(), typeList);
    }
}
