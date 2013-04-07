package com.sap.core.odata.processor.ref;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.sap.core.odata.api.annotation.edm.Facets;
import com.sap.core.odata.api.annotation.edm.FunctionImport;
import com.sap.core.odata.api.annotation.edm.FunctionImport.Multiplicity;
import com.sap.core.odata.api.annotation.edm.FunctionImport.ReturnType;
import com.sap.core.odata.api.annotation.edm.Parameter;
import com.sap.core.odata.api.annotation.edm.Parameter.Mode;
import com.sap.core.odata.api.annotation.edmx.HttpMethod;
import com.sap.core.odata.api.annotation.edmx.HttpMethod.Name;
import com.sap.core.odata.processor.ref.jpa.Address;
import com.sap.core.odata.processor.ref.jpa.SalesOrderHeader;
import com.sap.core.odata.processor.ref.jpa.SalesOrderItem;

public class SalesOrderHeaderProcessor {

	private EntityManager em;

	public SalesOrderHeaderProcessor() {
		em = Persistence.createEntityManagerFactory("salesorderprocessing")
				.createEntityManager();
	}

	@SuppressWarnings("unchecked")
	@FunctionImport(name = "FindAllSalesOrders", entitySet = "SalesOrders", returnType = ReturnType.ENTITY_TYPE, multiplicity = Multiplicity.MANY)
	public List<SalesOrderHeader> findAllSalesOrders(
			@Parameter(name = "DeliveryStatusCode", facets = @Facets(maxLength = 2)) String status) {

		Query q = em
				.createQuery("SELECT E1 from SalesOrderHeader E1 WHERE E1.deliveryStatus = '"
						+ status + "'");
		List<SalesOrderHeader> soList = (List<SalesOrderHeader>) q
				.getResultList();
		return soList;
	}

	@FunctionImport(name = "CheckATP", returnType = ReturnType.SCALAR, multiplicity = Multiplicity.ONE, httpMethod = @HttpMethod(name = Name.GET) )
	public boolean checkATP(
			@Parameter(name = "SoID", facets = @Facets(nullable = false), mode=Mode.IN) Long soID,
			@Parameter(name = "LiId", facets = @Facets(nullable = false), mode=Mode.IN) Long lineItemID)
	{
		if (soID == 2L)
			return false;
		else
			return true;
	}

	@FunctionImport(returnType = ReturnType.ENTITY_TYPE, entitySet = "SalesOrders")
	public SalesOrderHeader calculateNetAmount(
			@Parameter(name = "SoID", facets = @Facets(nullable = false)) Long soID) {

		Query q = em
				.createQuery("SELECT E1 from SalesOrderHeader E1 WHERE E1.soId = "
						+ soID + "l");
		if (q.getResultList().isEmpty())
			return null;
		SalesOrderHeader so = (SalesOrderHeader) q.getResultList().get(0);
		double amount = 0;
		for (SalesOrderItem soi : so.getSalesOrderItem()) {
			amount = amount
					+ (soi.getAmount() * soi.getDiscount() * soi.getQuantity());
		}
		so.setNetAmount(amount);
		return so;
	}

	@SuppressWarnings("unchecked")
	@FunctionImport(returnType = ReturnType.COMPLEX_TYPE)
	public Address getAddress(
			@Parameter(name = "SoID", facets = @Facets(nullable = false)) Long soID) {
		Query q = em
				.createQuery("SELECT E1 from SalesOrderHeader E1 WHERE E1.soId = "
						+ soID + "l");
		List<SalesOrderHeader> soList = (List<SalesOrderHeader>) q
				.getResultList();
		if (!soList.isEmpty())
			return soList.get(0).getBuyerAddress();
		else
			return null;
	}

}
