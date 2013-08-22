package com.sap.core.odata.processor.core.jpa.access.data;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.access.JPAProcessor;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;
import com.sap.core.odata.processor.core.jpa.ODataEntityParser;

public class JPALink {

  private ODataJPAContext context;
  private JPAProcessor jpaProcessor;
  private ODataEntityParser parser;
  private Object targetJPAEntity;
  private Object sourceJPAEntity;

  public JPALink(final ODataJPAContext context) {
    this.context = context;
    jpaProcessor = ODataJPAFactory.createFactory().getJPAAccessFactory()
        .getJPAProcessor(this.context);
    parser = new ODataEntityParser(this.context);
  }

  public void setSourceJPAEntity(final Object jpaEntity) {
    sourceJPAEntity = jpaEntity;
  }

  public void create(final PostUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType)
      throws ODataJPARuntimeException, ODataJPAModelException {

    EdmEntitySet targetEntitySet = uriInfo.getTargetEntitySet();
    String targerEntitySetName;
    EdmNavigationProperty navigationProperty = null;
    try {
      targerEntitySetName = targetEntitySet.getName();
    } catch (EdmException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    }

    List<UriInfo> uriInfoList = new ArrayList<UriInfo>();

    if (((UriInfo) uriInfo).isLinks()) {
      UriInfo getUriInfo = parser.parseLink(targetEntitySet, content, requestContentType);
      uriInfoList = new ArrayList<UriInfo>();
      uriInfoList.add(getUriInfo);
      navigationProperty = uriInfo.getNavigationSegments().get(0).getNavigationProperty();
    }
    else
    {
      uriInfoList = parser.parseLinks(targetEntitySet, content, contentType);
    }

    if (uriInfoList == null) {
      return;
    }
    try {
      for (UriInfo getUriInfo : uriInfoList) {

        if (!getUriInfo.getTargetEntitySet().getName().equals(targerEntitySetName))
        {
          throw ODataJPARuntimeException
              .throwException(ODataJPARuntimeException.RELATIONSHIP_INVALID, null);
        }
        if (!((UriInfo) uriInfo).isLinks()) {
          navigationProperty = getUriInfo.getNavigationSegments().get(0).getNavigationProperty();
        }

        targetJPAEntity = jpaProcessor.process((GetEntityUriInfo) getUriInfo);
        if (targetJPAEntity != null && ((UriInfo) uriInfo).isLinks()) {
          getUriInfo = parser.parseLinkURI();
          sourceJPAEntity = jpaProcessor.process((GetEntityUriInfo) getUriInfo);
          if (sourceJPAEntity == null) {
            throw ODataJPARuntimeException
                .throwException(ODataJPARuntimeException.RESOURCE_X_NOT_FOUND.
                    addContent(getUriInfo.getTargetEntitySet().getName()), null);
          }
        }

        JPAEntityParser entityParser = new JPAEntityParser();
        Method setMethod = entityParser.getAccessModifier(sourceJPAEntity,
            navigationProperty, JPAEntityParser.ACCESS_MODIFIER_SET);

        Method getMethod = entityParser.getAccessModifier(sourceJPAEntity,
            navigationProperty, JPAEntityParser.ACCESS_MODIFIER_GET);

        if (getMethod.getReturnType().getTypeParameters() != null) {
          @SuppressWarnings("unchecked")
          List<Object> relatedEntities = (List<Object>) getMethod.invoke(sourceJPAEntity);
          relatedEntities.add(targetJPAEntity);
          setMethod.invoke(sourceJPAEntity, relatedEntities);
        } else {
          setMethod.invoke(sourceJPAEntity, targetJPAEntity);
        }
      }
    } catch (IllegalAccessException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (IllegalArgumentException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (InvocationTargetException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (EdmException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    }
  }

  public void delete() {}

  public void save() {
    EntityManager em = context.getEntityManager();
    EntityTransaction tx = em.getTransaction();

    if (!tx.isActive()) {
      em.getTransaction().begin();
      em.persist(sourceJPAEntity);
      em.getTransaction().commit();
    }

  }

  public void update(final PutMergePatchUriInfo putUriInfo, final InputStream content,
      final String requestContentType, final String contentType) throws ODataJPARuntimeException, ODataJPAModelException {
    UriInfo uriInfo = (UriInfo) putUriInfo;

    EdmEntitySet targetEntitySet = uriInfo.getTargetEntitySet();
    String targerEntitySetName;
    EdmNavigationProperty navigationProperty = null;
    try {
      targerEntitySetName = targetEntitySet.getName();
    } catch (EdmException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    }

    List<UriInfo> uriInfoList = new ArrayList<UriInfo>();

    if (((UriInfo) uriInfo).isLinks()) {
      UriInfo getUriInfo = parser.parseLink(targetEntitySet, content, requestContentType);
      uriInfoList = new ArrayList<UriInfo>();
      uriInfoList.add(getUriInfo);
      navigationProperty = uriInfo.getNavigationSegments().get(0).getNavigationProperty();
    }
    else
    {
      uriInfoList = parser.parseLinks(targetEntitySet, content, contentType);
    }

    if (uriInfoList == null) {
      return;
    }
    try {
      for (UriInfo getUriInfo : uriInfoList) {

        if (!getUriInfo.getTargetEntitySet().getName().equals(targerEntitySetName))
        {
          throw ODataJPARuntimeException
              .throwException(ODataJPARuntimeException.RELATIONSHIP_INVALID, null);
        }
        if (!((UriInfo) uriInfo).isLinks()) {
          navigationProperty = getUriInfo.getNavigationSegments().get(0).getNavigationProperty();
        }

        targetJPAEntity = jpaProcessor.process((GetEntityUriInfo) getUriInfo);
        if (targetJPAEntity != null && ((UriInfo) uriInfo).isLinks()) {
          getUriInfo = parser.parseLinkURI();
          sourceJPAEntity = jpaProcessor.process((GetEntityUriInfo) getUriInfo);
          if (sourceJPAEntity == null) {
            throw ODataJPARuntimeException
                .throwException(ODataJPARuntimeException.RESOURCE_X_NOT_FOUND.
                    addContent(getUriInfo.getTargetEntitySet().getName()), null);
          }
        }

        JPAEntityParser entityParser = new JPAEntityParser();
        Method setMethod = entityParser.getAccessModifier(sourceJPAEntity,
            navigationProperty, JPAEntityParser.ACCESS_MODIFIER_SET);

        Method getMethod = entityParser.getAccessModifier(sourceJPAEntity,
            navigationProperty, JPAEntityParser.ACCESS_MODIFIER_GET);

        if (getMethod.getReturnType().getTypeParameters() != null && getMethod.getReturnType().getTypeParameters().length != 0) {
          @SuppressWarnings("unchecked")
          List<Object> relatedEntities = (List<Object>) getMethod.invoke(sourceJPAEntity);
          relatedEntities.add(targetJPAEntity);
          setMethod.invoke(sourceJPAEntity, relatedEntities);
        } else {
          setMethod.invoke(sourceJPAEntity, targetJPAEntity);
        }
      }
    } catch (IllegalAccessException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (IllegalArgumentException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (InvocationTargetException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    } catch (EdmException e) {
      throw ODataJPARuntimeException
          .throwException(ODataJPARuntimeException.GENERAL
              .addContent(e.getMessage()), e);
    }

  }

}
