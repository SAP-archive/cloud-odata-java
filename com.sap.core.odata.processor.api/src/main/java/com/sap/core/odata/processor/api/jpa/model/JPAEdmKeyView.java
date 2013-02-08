package com.sap.core.odata.processor.api.jpa.model;

import com.sap.core.odata.api.edm.provider.Key;

/**
 * This class provides view to the implementation class which has functionality to fetch EDM Key.
 * @author AG
 *
 */
public interface JPAEdmKeyView extends JPAEdmBaseView {
	public Key getEdmKey( );
}
