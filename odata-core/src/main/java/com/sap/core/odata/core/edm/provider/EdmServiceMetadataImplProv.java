package com.sap.core.odata.core.edm.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.provider.DataServices;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.ep.ODataEntityProviderException;
import com.sap.core.odata.api.exception.ODataException;

public class EdmServiceMetadataImplProv implements EdmServiceMetadata {

  private static final Logger LOG = LoggerFactory.getLogger(EdmServiceMetadataImplProv.class);
  
  private EdmProvider edmProvider;
  private String dataServiceVersion;
  private Collection<Schema> schemas;

  public EdmServiceMetadataImplProv(EdmProvider edmProvider) {
    this.edmProvider = edmProvider;
  }

  @Override
  public InputStream getMetadata() throws ODataException {
    if (schemas == null) {
      schemas = edmProvider.getSchemas();
    }

    OutputStreamWriter writer = null;

    try {
      DataServices metadata = new DataServices().setSchemas(schemas).setDataServiceVersion(getDataServiceVersion());
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      writer = new OutputStreamWriter(outputStream, "UTF-8");
      EdmMetadata.writeMetadata(metadata, writer);
      return new ByteArrayInputStream(outputStream.toByteArray());
    } catch (UnsupportedEncodingException e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
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
      dataServiceVersion = Edm.DATA_SERVICE_VERSION_10;

      if (schemas != null) {
        for (Schema schema : schemas) {
          Collection<EntityType> entityTypes = schema.getEntityTypes();
          if (entityTypes != null) {
            for (EntityType entityType : entityTypes) {
              Collection<Property> properties = entityType.getProperties();
              if (properties != null) {
                for (Property property : properties) {
                  if (property.getCustomizableFeedMappings() != null) {
                    if (property.getCustomizableFeedMappings().getFcKeepInContent() != null) {
                      if (property.getCustomizableFeedMappings().getFcKeepInContent()) {
                        dataServiceVersion = Edm.DATA_SERVICE_VERSION_20;
                        return dataServiceVersion;
                      }
                    }
                  }
                }
                if (entityType.getCustomizableFeedMappings() != null) {
                  if (entityType.getCustomizableFeedMappings().getFcKeepInContent() != null) {
                    if (entityType.getCustomizableFeedMappings().getFcKeepInContent()) {
                      dataServiceVersion = Edm.DATA_SERVICE_VERSION_20;
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