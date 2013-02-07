package com.sap.core.odata.processor.api.jpa.exception;

import com.sap.core.odata.api.exception.MessageReference;

public interface ODataJPAMessageService {
	public String getLocalizedMessage(MessageReference context);
}
