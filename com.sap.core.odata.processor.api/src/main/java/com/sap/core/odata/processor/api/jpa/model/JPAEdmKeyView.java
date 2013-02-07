package com.sap.core.odata.processor.api.jpa.model;

import com.sap.core.odata.api.edm.provider.Key;

public interface JPAEdmKeyView extends JPAEdmBaseView {
	public Key getEdmKey( );
}
