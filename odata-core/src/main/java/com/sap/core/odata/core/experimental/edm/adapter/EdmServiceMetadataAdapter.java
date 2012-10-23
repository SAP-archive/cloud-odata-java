package com.sap.core.odata.core.experimental.edm.adapter;

import org.odata4j.edm.EdmDataServices;

import com.sap.core.odata.api.edm.EdmServiceMetadata;

public class EdmServiceMetadataAdapter implements EdmServiceMetadata {

  private EdmDataServices edmDataServices;

  public EdmServiceMetadataAdapter(EdmDataServices edmDataServices) {
    this.edmDataServices = edmDataServices;
  }

  @Override
  public byte[] getMetadata() {
    // TODO Auto-generated method stub
    /*
     *ODataProducer producer = producerResolver.getContext(ODataProducer.class);

    EdmDataServices metadata = producer.getMetadata();

    StringWriter w = new StringWriter();
    FormatWriter<EdmDataServices> fw = FormatWriterFactory.getFormatWriter(EdmDataServices.class, httpHeaders.getAcceptableMediaTypes(), format, callback);
    fw.write(uriInfo, w, metadata);

    return Response.ok(w.toString(), fw.getContentType())
        .header(ODataConstants.Headers.DATA_SERVICE_VERSION, ODataConstants.DATA_SERVICE_VERSION_HEADER)
        .build();
     */
    return null;
  }

  @Override
  public String getDataServiceVersion() {
    return this.edmDataServices.getVersion();
  }

}
