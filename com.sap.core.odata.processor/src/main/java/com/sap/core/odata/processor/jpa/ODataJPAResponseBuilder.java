package com.sap.core.odata.processor.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.enums.InlineCount;
import com.sap.core.odata.api.ep.ODataEntityProvider;
import com.sap.core.odata.api.ep.ODataEntityProviderException;
import com.sap.core.odata.api.ep.ODataEntityProviderProperties;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public final class ODataJPAResponseBuilder {

	public static ODataResponse build(List<Object> jpaEntities,
			GetEntitySetView resultsView, ContentType contentType,ODataJPAContext odataJPAContext) throws ODataJPARuntimeException {

		EdmEntityType edmEntityType = null;
		ODataResponse odataResponse = null;

		try {
			edmEntityType = resultsView.getTargetEntitySet().getEntityType();

			List<Map<String, Object>> edmEntityList = new ArrayList<Map<String, Object>>();
			Map<String, Object> edmPropertyValueMap = null;
			
			JPAResultParser jpaResultParser = JPAResultParser.create( );
			for (Object jpaEntity : jpaEntities) {
				edmPropertyValueMap = jpaResultParser.parse2EdmPropertyValueMap(jpaEntity,edmEntityType);
				edmEntityList.add(edmPropertyValueMap);
			}
			
		    ODataEntityProviderProperties feedProperties = null;;
			try {
				 final Integer count = resultsView.getInlineCount() == InlineCount.ALLPAGES ? edmEntityList.size() : null;
				feedProperties = ODataEntityProviderProperties
				        .baseUri(odataJPAContext.getODataContext().getUriInfo().getBaseUri())
				        .inlineCount(count)
				        .skipToken("")
				        .build();
			} catch (ODataException e) {
				throw ODataJPARuntimeException.throwException(ODataJPARuntimeException.GENERAL.addContent(e.getMessage()),e);
			}
		    
			odataResponse = ODataResponse.fromResponse(ODataEntityProvider.create(contentType).writeFeed(resultsView, edmEntityList, feedProperties))
			        .status(HttpStatusCodes.OK)
			        .build();
			
			
		} catch (ODataEntityProviderException e) {
			throw ODataJPARuntimeException.throwException(ODataJPARuntimeException.GENERAL.addContent(e.getMessage()),e);
		} catch (EdmException e) {
			throw ODataJPARuntimeException.throwException(ODataJPARuntimeException.GENERAL.addContent(e.getMessage()),e);
		}

		return odataResponse;
	}
}
