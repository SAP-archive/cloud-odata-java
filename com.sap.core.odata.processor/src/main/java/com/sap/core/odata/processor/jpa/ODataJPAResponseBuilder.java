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

			List<Map<String, Object>> edmEntityListMap = new ArrayList<Map<String, Object>>();
			Map<String, Object> edmEntity = null;
			
			JPAResultParser jpaResultParser = JPAResultParser.create( );
			for (Object jpaEntity : jpaEntities) {
				edmEntity = jpaResultParser.parse2EdmEntity(jpaEntity,
						edmEntityType);
				edmEntityListMap.add(edmEntity);
			}
			
		    ODataEntityProviderProperties feedProperties = null;;
			try {
				 final Integer count = resultsView.getInlineCount() == InlineCount.ALLPAGES ? edmEntityListMap.size() : null;
				feedProperties = ODataEntityProviderProperties
				        .baseUri(odataJPAContext.getODataContext().getUriInfo().getBaseUri())
				        .inlineCount(count)
				        .skipToken("")
				        .build();
			} catch (ODataException e) {
				throw new ODataJPARuntimeException(ODataJPARuntimeException.COMMON.addContent(e.getMessage()),e);
			}
		    
			odataResponse = ODataResponse
					.status(HttpStatusCodes.OK)
					.entity(ODataEntityProvider.create(contentType).writeFeed(
							resultsView, edmEntityListMap, feedProperties))
					.build();
			
		} catch (ODataEntityProviderException e) {
			throw new ODataJPARuntimeException(ODataJPARuntimeException.COMMON.addContent(e.getMessage()),e);
		} catch (EdmException e) {
			throw new ODataJPARuntimeException(ODataJPARuntimeException.COMMON.addContent(e.getMessage()),e);
		}

		return odataResponse;
	}
}
