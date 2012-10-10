package org.odata4j.test.integration.producer.jpa.northwind;

import java.util.ArrayList;
import java.util.List;

import org.odata4j.core.OCollection;
import org.odata4j.core.OCollections;
import org.odata4j.core.OComplexObject;
import org.odata4j.core.OComplexObjects;
import org.odata4j.core.OFunctionParameter;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.core.OSimpleObject;
import org.odata4j.edm.EdmCollectionType;
import org.odata4j.edm.EdmComplexType;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntityContainer;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.edm.EdmFunctionParameter;
import org.odata4j.edm.EdmProperty.CollectionKind;
import org.odata4j.edm.EdmSchema;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.producer.BaseResponse;
import org.odata4j.producer.ODataContext;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.ODataProducerDelegate;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.Responses;
import org.odata4j.producer.jpa.JPAProducer;

/**
 * a producer delegate that implements a few functions for testing purposes.
 * The function implementations are in no way JPA specific.
 */
public class NorthwindProducerWithFunctions extends ODataProducerDelegate {

  private final JPAProducer producer;
  private final EdmDataServices metadata;

  public NorthwindProducerWithFunctions(JPAProducer p) {
    producer = p;
    metadata = extendModel(p.getMetadata());
  }

  @Override
  public ODataProducer getDelegate() {
    return producer;
  }

  @Override
  public EdmDataServices getMetadata() {
    return metadata;
  }

  @Override
  public BaseResponse callFunction(ODataContext context, EdmFunctionImport function, java.util.Map<String, OFunctionParameter> params, QueryInfo queryInfo) {
    if (function.getName().equals("TestFunction1")) {
      return testFunction1(function, params, queryInfo);
    } else if (function.getName().equals("TestFunction2")) {
      return testFunction2(function, params, queryInfo);
    } else if (function.getName().equals("TestFunction3")) {
      return testFunction3(function, params, queryInfo);
    } else {
      throw new RuntimeException("unknown function"); // TODO 404?
    }
  }

  private BaseResponse testFunction1(EdmFunctionImport function, java.util.Map<String, OFunctionParameter> params, QueryInfo queryInfo) {

    List<OProperty<?>> props = new ArrayList<OProperty<?>>(2);
    props.add(OProperties.int32("OrderID", 33));
    props.add(OProperties.int32("ProductID", 44));

    OComplexObject o = OComplexObjects.create(this.getMetadata().findEdmComplexType("NorthwindModel.Order_DetailsPK"), props);
    return Responses.complexObject(o, function.getName());
  }

  private BaseResponse testFunction2(EdmFunctionImport function, java.util.Map<String, OFunctionParameter> params, QueryInfo queryInfo) {

    OFunctionParameter fp = params.get("NResults");
    if (fp == null) {
      throw new RuntimeException("missing parameter NResults");
    }

    Short nresults = (Short) ((OSimpleObject<?>) fp.getValue()).getValue();

    EdmComplexType ct = this.getMetadata().findEdmComplexType("NorthwindModel.Order_DetailsPK");
    OCollection.Builder<OComplexObject> c = OCollections.<OComplexObject> newBuilder(ct);

    for (int i = 0, orderid = 1, productid = 2; i < nresults; i++, orderid += 2, productid += 2) {
      List<OProperty<?>> props = new ArrayList<OProperty<?>>(2);
      props.add(OProperties.int32("OrderID", orderid));
      props.add(OProperties.int32("ProductID", productid));
      c = c.add(OComplexObjects.create(ct, props));
    }

    return Responses.collection(c.build(), null, null, null, function.getName());
  }

  private BaseResponse testFunction3(EdmFunctionImport function, java.util.Map<String, OFunctionParameter> params, QueryInfo queryInfo) {

    return null;
  }

  private static EdmDataServices extendModel(EdmDataServices metadata) {
    // add some functions to the edm
    EdmDataServices.Builder ds = EdmDataServices.newBuilder(metadata);

    EdmSchema.Builder schema = ds.findSchema("NorthwindContainer");
    EdmEntityContainer.Builder container = schema.findEntityContainer("NorthwindEntities");

    EdmComplexType.Builder ct = ds.findEdmComplexType("NorthwindModel.Order_DetailsPK");
    List<EdmFunctionParameter.Builder> params = new ArrayList<EdmFunctionParameter.Builder>(15);
    params.add(EdmFunctionParameter.newBuilder().input("PBoolean", EdmSimpleType.BOOLEAN));
    params.add(EdmFunctionParameter.newBuilder().input("PByte", EdmSimpleType.BYTE));
    params.add(EdmFunctionParameter.newBuilder().input("PSByte", EdmSimpleType.SBYTE));
    params.add(EdmFunctionParameter.newBuilder().input("PDateTime", EdmSimpleType.DATETIME));
    params.add(EdmFunctionParameter.newBuilder().input("PDateTimeOffset", EdmSimpleType.DATETIMEOFFSET));
    params.add(EdmFunctionParameter.newBuilder().input("PDecimal", EdmSimpleType.DECIMAL));
    params.add(EdmFunctionParameter.newBuilder().input("PDouble", EdmSimpleType.DOUBLE));
    params.add(EdmFunctionParameter.newBuilder().input("PGuid", EdmSimpleType.GUID));
    params.add(EdmFunctionParameter.newBuilder().input("PInt16", EdmSimpleType.INT16));
    params.add(EdmFunctionParameter.newBuilder().input("PInt32", EdmSimpleType.INT32));
    params.add(EdmFunctionParameter.newBuilder().input("PInt64", EdmSimpleType.INT64));
    params.add(EdmFunctionParameter.newBuilder().input("PSingle", EdmSimpleType.SINGLE));
    params.add(EdmFunctionParameter.newBuilder().input("PString", EdmSimpleType.STRING));
    params.add(EdmFunctionParameter.newBuilder().input("PTime", EdmSimpleType.TIME));

    EdmFunctionImport.Builder testFunction1 = EdmFunctionImport.newBuilder()
        .setName("TestFunction1")
        .setReturnType(ct)
        .setHttpMethod("GET")
        .addParameters(params);
    container.addFunctionImports(testFunction1);

    params = new ArrayList<EdmFunctionParameter.Builder>(1);
    params.add(EdmFunctionParameter.newBuilder().input("NResults", EdmSimpleType.INT16));

    EdmFunctionImport.Builder testFunction2 = EdmFunctionImport.newBuilder()
        .setName("TestFunction2")
        .setReturnType(new EdmCollectionType(CollectionKind.Collection, ct.build()))
        .setHttpMethod("GET")
        .addParameters(params);
    container.addFunctionImports(testFunction2);

    // no return type
    EdmFunctionImport.Builder testFunction3 = EdmFunctionImport.newBuilder()
        .setName("TestFunction3")
        .setHttpMethod("POST")
        .addParameters(EdmFunctionParameter.newBuilder().input("PString", EdmSimpleType.STRING));
    container.addFunctionImports(testFunction3);

    return ds.build();
  }

}
