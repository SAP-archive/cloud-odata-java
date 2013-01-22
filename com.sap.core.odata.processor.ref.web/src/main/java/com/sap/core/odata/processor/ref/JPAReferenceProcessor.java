package com.sap.core.odata.processor.ref;

import java.util.List;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.processor.jpa.ODataJPAResponseBuilder;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.api.ODataJPAProcessor;
import com.sap.core.odata.processor.jpa.api.factory.ODataJPAFactory;
import com.sap.core.odata.processor.ref.jpa.SalesOrderHeader;
import com.sap.core.odata.processor.ref.jpa.SalesOrderItem;

public class JPAReferenceProcessor extends ODataJPAProcessor {

	private static final String SALESORDERHEADERS = "SalesOrderHeaders";

	public JPAReferenceProcessor(ODataJPAContext oDataJPAContext) {
		super(oDataJPAContext);
	}

	/*@Override
	public ODataResponse readEntitySet(GetEntitySetUriInfo uriParserResultView,
			String contentType) throws ODataException {

		ODataJPAFactory factory = ODataJPAFactory.createFactory();
		factory.getJPAAccessFactory().getJPAProcessor(oDataJPAContext);

		if (uriParserResultView.getTargetEntitySet().getName()
				.equals(SALESORDERHEADERS)) {

			List<SalesOrderHeader> salesOrderHeaders = this.jpaProcessor
					.process(uriParserResultView);
			
		
			SalesOrderHeader salesOrderHeader = salesOrderHeaders.get(0);
			salesOrderHeader
					.setGrossAmount(calculateSOGrossAmount(salesOrderHeader));
			salesOrderHeader.setNetAmount(calcualteNetAmount(salesOrderHeader));

			// Build OData Response out of a JPA Response
			ODataResponse oDataResponse = ODataJPAResponseBuilder.build(
					salesOrderHeaders, uriParserResultView, contentType,
					oDataJPAContext);

			return oDataResponse;
		}

		return null;

	}

	private double calculateSOGrossAmount(SalesOrderHeader salesOrderHeader) {
		double grossAmount = 0;
		if (salesOrderHeader.getSalesOrderItem() != null)
			for (SalesOrderItem item : salesOrderHeader.getSalesOrderItem()) 
				grossAmount = grossAmount + item.getAmount();
			
			return grossAmount;

	}

	private double calcualteNetAmount(SalesOrderHeader salesOrderHeader) {
		if (salesOrderHeader.getSalesOrderItem() != null) {
			double netAmount = 0;
			for (SalesOrderItem item : salesOrderHeader.getSalesOrderItem()) {
				netAmount = netAmount + item.getNetAmount();
			}
			return netAmount;
		} else {
			return (Double) 0.0;
		}

	}
*/
}
