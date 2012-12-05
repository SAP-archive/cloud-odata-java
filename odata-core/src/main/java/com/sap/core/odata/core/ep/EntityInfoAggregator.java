package com.sap.core.odata.core.ep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.ep.ODataSerializationException;
import com.sap.core.odata.core.ep.EntityInfoAggregator.EntityPropertyInfo;

public class EntityInfoAggregator {

//  private final EdmEntitySet entitySet;
  private String entitySetName;
  
  EntityInfoAggregator() {
  }

  public static EntityInfoAggregator init(EdmEntitySet entitySet) throws ODataSerializationException {
    EntityInfoAggregator eia = new EntityInfoAggregator();
    eia.initialize(entitySet);
    return eia;
  }

  public String getPropertyNames() {
    return name2EntityPropertyInfo.toString();
  }

  public String getEntitySetName() {
    return entitySetName;
  }

  public List<EntityPropertyInfo> getKeyProperties() {
    List<EntityPropertyInfo> keyProperties = new ArrayList<EntityInfoAggregator.EntityPropertyInfo>();
    for (String keyPropertyName: keyPropertyNames) {
      EntityPropertyInfo e = name2EntityPropertyInfo.get(keyPropertyName);
      keyProperties.add(e);
    }
    return keyProperties;
  }

  public String getTargetPaths() {
    return targetPath2EntityPropertyInfo.toString();
  }

  public String getNavigationPropertyNames() {
    return name2NavigationPropertyInfo.toString();
  }

  public String getEntityContainerName() {
    return entityContainerName;
  }
  
  private Map<String, EntityPropertyInfo> name2EntityPropertyInfo = new HashMap<String, EntityInfoAggregator.EntityPropertyInfo>();
  private Map<String, NavigationPropertyInfo> name2NavigationPropertyInfo = new HashMap<String, EntityInfoAggregator.NavigationPropertyInfo>();
  private Map<String, EntityPropertyInfo> targetPath2EntityPropertyInfo = new HashMap<String, EntityInfoAggregator.EntityPropertyInfo>();
  private List<String> keyPropertyNames = new ArrayList<String>();
  private boolean isDefaultEntityContainer;
  private String entityContainerName;
  
  private void initialize(EdmEntitySet entitySet) throws ODataSerializationException {
    try {
      EdmEntityType type = entitySet.getEntityType();
      entitySetName = entitySet.getName();
      isDefaultEntityContainer = entitySet.getEntityContainer().isDefaultEntityContainer();
      entityContainerName = entitySet.getEntityContainer().getName();
      //
      Collection<String> propertyNames = new ArrayList<String>();
      propertyNames.addAll(type.getPropertyNames());
      propertyNames.addAll(type.getNavigationPropertyNames());
      //
      name2EntityPropertyInfo = createInfoObjects(type, propertyNames);
      //
      keyPropertyNames.addAll(type.getKeyPropertyNames());
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private Map<String, EntityPropertyInfo> createInfoObjects(EdmStructuralType type, Collection<String> propertyNames) throws EdmException {
    Map<String, EntityPropertyInfo> infos = new HashMap<String, EntityInfoAggregator.EntityPropertyInfo>();
    
    for (String propertyName : propertyNames) {
      EdmTyped typed = type.getProperty(propertyName);
      
      if(typed instanceof EdmProperty) {
        EdmProperty property = (EdmProperty) typed;

        EdmType t = property.getType();
        if(t instanceof EdmSimpleType) {
          EntityPropertyInfo info = EntityPropertyInfo.create(property);
          infos.put(info.name, info);
          checkTargetPathInfo(property, info);
        } else if(t instanceof EdmComplexType) {
          EdmComplexType complex = (EdmComplexType) t;
          Map<String, EntityPropertyInfo> recursiveInfos = createInfoObjects(complex, complex.getPropertyNames());
          EntityComplexPropertyInfo info = EntityComplexPropertyInfo.create(complex, recursiveInfos);
          infos.put(info.name, info);
        }
      } else if(typed instanceof EdmNavigationProperty) {
        EdmNavigationProperty navProperty = (EdmNavigationProperty) typed;
        NavigationPropertyInfo info = NavigationPropertyInfo.create(navProperty);
        name2NavigationPropertyInfo.put(info.name, info);
      }
    }
    
    return infos;
  }

  
  private void checkTargetPathInfo(EdmProperty property, EntityPropertyInfo value) throws EdmException {
    EdmCustomizableFeedMappings customizableFeedMappings = property.getCustomizableFeedMappings();
    if (customizableFeedMappings != null) {
      String targetPath = customizableFeedMappings.getFcTargetPath();
      if (EdmTargetPath.SYNDICATION_TITLE.equals(targetPath)) {
        targetPath2EntityPropertyInfo.put(EdmTargetPath.SYNDICATION_TITLE, value);
      } else if (EdmTargetPath.SYNDICATION_UPDATED.equals(targetPath)) {
        targetPath2EntityPropertyInfo.put(EdmTargetPath.SYNDICATION_UPDATED, value);
      } else if (EdmTargetPath.SYNDICATION_SUMMARY.equals(targetPath)) {
        targetPath2EntityPropertyInfo.put(EdmTargetPath.SYNDICATION_SUMMARY, value);
      } else if (EdmTargetPath.SYNDICATION_SOURCE.equals(targetPath)) {
        targetPath2EntityPropertyInfo.put(EdmTargetPath.SYNDICATION_SOURCE, value);
      } else if (EdmTargetPath.SYNDICATION_RIGHTS.equals(targetPath)) {
        targetPath2EntityPropertyInfo.put(EdmTargetPath.SYNDICATION_RIGHTS, value);
      } else if (EdmTargetPath.SYNDICATION_PUBLISHED.equals(targetPath)) {
        targetPath2EntityPropertyInfo.put(EdmTargetPath.SYNDICATION_PUBLISHED, value);
      } else if (EdmTargetPath.SYNDICATION_CONTRIBUTORURI.equals(targetPath)) {
        targetPath2EntityPropertyInfo.put(EdmTargetPath.SYNDICATION_CONTRIBUTORURI, value);
      } else if (EdmTargetPath.SYNDICATION_CONTRIBUTORNAME.equals(targetPath)) {
        targetPath2EntityPropertyInfo.put(EdmTargetPath.SYNDICATION_CONTRIBUTORNAME, value);
      } else if (EdmTargetPath.SYNDICATION_CONTRIBUTOREMAIL.equals(targetPath)) {
        targetPath2EntityPropertyInfo.put(EdmTargetPath.SYNDICATION_CONTRIBUTOREMAIL, value);
      } else if (EdmTargetPath.SYNDICATION_AUTHORURI.equals(targetPath)) {
        targetPath2EntityPropertyInfo.put(EdmTargetPath.SYNDICATION_AUTHORURI, value);
      } else if (EdmTargetPath.SYNDICATION_AUTHORNAME.equals(targetPath)) {
        targetPath2EntityPropertyInfo.put(EdmTargetPath.SYNDICATION_AUTHORNAME, value);
      } else if (EdmTargetPath.SYNDICATION_AUTHOREMAIL.equals(targetPath)) {
        targetPath2EntityPropertyInfo.put(EdmTargetPath.SYNDICATION_AUTHOREMAIL, value);
      }
    }
  }

  public String getTargetPath(String targetPath, Object value) throws ODataSerializationException {
    try {
      EntityPropertyInfo info = targetPath2EntityPropertyInfo.get(targetPath);
      if(info != null) {
        EdmSimpleType st = (EdmSimpleType) info.type;
        return st.valueToString(value, EdmLiteralKind.DEFAULT, info.facets);
      }
      return null;
    } catch(EdmException e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON);
    }
  }

  static class EntityComplexPropertyInfo extends EntityPropertyInfo {
    protected Map<String, EntityPropertyInfo> internalName2EntityPropertyInfo;
    
    static EntityComplexPropertyInfo create(EdmComplexType property, Map<String, EntityPropertyInfo> childEntityInfos) throws EdmException {
      EntityComplexPropertyInfo info = new EntityComplexPropertyInfo();
      info.name = property.getName();
      info.internalName2EntityPropertyInfo = childEntityInfos;
      return info;
    }
    
    @Override
    boolean isComplex() {
      return true;
    }
    
    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (EntityPropertyInfo info: internalName2EntityPropertyInfo.values()) {
        if(sb.length() == 0) {
          sb.append(super.toString()).append("=>[").append(info.toString());
        } else {
          sb.append(", ").append(info.toString());
        }
      }
      sb.append("]");
      return sb.toString();
    }
    
    public Collection<EntityPropertyInfo> getPropertyInfos() {
      return Collections.unmodifiableCollection(internalName2EntityPropertyInfo.values());
    }
  }
  
  static class EntityPropertyInfo {
    protected String name;
    protected EdmType type;
    protected EdmFacets facets;
    
    static EntityPropertyInfo create(EdmProperty property) throws EdmException {
      EntityPropertyInfo info = new EntityPropertyInfo();
      info.name = property.getName();
      info.type = property.getType();
      info.facets = property.getFacets();
      return info;
    }
    
    boolean isComplex() {
      return false;
    }

    @Override
    public String toString() {
      return name;
    }
    
    public String getName() {
      return name;
    }
    public EdmType getType() {
      return type;
    }
    public EdmFacets getFacets() {
      return facets;
    }
  }
  
  static class NavigationPropertyInfo {
    String name;
    EdmMultiplicity multiplicity;
    
    static NavigationPropertyInfo create(EdmNavigationProperty property) throws EdmException {
      NavigationPropertyInfo info = new NavigationPropertyInfo();
      info.name = property.getName();
      info.multiplicity = property.getMultiplicity();
      return info;
    }

    @Override
    public String toString() {
      return name + "; multiplicity=" + multiplicity;
    }
  }

  public boolean isDefaultEntityContainer() {
    return isDefaultEntityContainer;
  }

  public EntityPropertyInfo getTargetPathInfo(String targetPath) {
    return targetPath2EntityPropertyInfo.get(targetPath);
  }

}
