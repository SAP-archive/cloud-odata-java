package com.sap.core.odata.processor.jpa.api.exception;

import com.sap.core.odata.api.exception.MessageReference;

public interface ODataJPAMessageService {
	public String getLocalizedMessage(MessageReference context);
}
