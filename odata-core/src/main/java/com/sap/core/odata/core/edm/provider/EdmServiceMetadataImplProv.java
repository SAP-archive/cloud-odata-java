package com.sap.core.odata.core.edm.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.provider.DataServices;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.core.ep.producer.XmlMetadataProducer;
import com.sap.core.odata.core.ep.util.CircleStreamBuffer;

/**
 * @author SAP AG
 */
public class EdmServiceMetadataImplProv implements EdmServiceMetadata {

  private static final Logger LOG = LoggerFactory.getLogger(EdmServiceMetadataImplProv.class);

  private EdmProvider edmProvider;
  private String dataServiceVersion;
  private List<Schema> schemas;

  public EdmServiceMetadataImplProv(final EdmProvider edmProvider) {
    this.edmProvider = edmProvider;
  }

  @Override
  public InputStream getMetadata() throws ODataException {
    if (schemas == null) {
      schemas = edmProvider.getSchemas();
    }

    OutputStreamWriter writer = null;
    CircleStreamBuffer csb = new CircleStreamBuffer();

    try {
      DataServices metadata = new DataServices().setSchemas(schemas).setDataServiceVersion(getDataServiceVersion());
      writer = new OutputStreamWriter(csb.getOutputStream(), "UTF-8");
      XmlMetadataProducer.writeMetadata(metadata, writer, null);
      return csb.getInputStream();
    } catch (UnsupportedEncodingException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          // don't throw in finally!  
          LOG.error(e.getLocalizedMessage(), e);
        }
      }
    }
  }

  @Override
  public String getDataServiceVersion() throws ODataException {
    if (schemas == null) {
      schemas = edmProvider.getSchemas();
    }

    if (dataServiceVersion == null) {
      dataServiceVersion = ODataServiceVersion.V10;

      if (schemas != null) {
        for (Schema schema : schemas) {
          List<EntityType> entityTypes = schema.getEntityTypes();
          if (entityTypes != null) {
            for (EntityType entityType : entityTypes) {
              List<Property> properties = entityType.getProperties();
              if (properties != null) {
                for (Property property : properties) {
                  if (property.getCustomizableFeedMappings() != null) {
                    if (property.getCustomizableFeedMappings().getFcKeepInContent() != null) {
                      if (!property.getCustomizableFeedMappings().getFcKeepInContent()) {
                        dataServiceVersion = ODataServiceVersion.V20;
                        return dataServiceVersion;
                      }
                    }
                  }
                }
                if (entityType.getCustomizableFeedMappings() != null) {
                  if (entityType.getCustomizableFeedMappings().getFcKeepInContent() != null) {
                    if (entityType.getCustomizableFeedMappings().getFcKeepInContent()) {
                      dataServiceVersion = ODataServiceVersion.V20;
                      return dataServiceVersion;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return dataServiceVersion;
  }
}