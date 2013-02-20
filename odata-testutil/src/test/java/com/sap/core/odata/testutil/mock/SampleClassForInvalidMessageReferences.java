package com.sap.core.odata.testutil.mock;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

public class SampleClassForInvalidMessageReferences extends ODataMessageException
{
  private static final long serialVersionUID = 1L;

  public SampleClassForInvalidMessageReferences(MessageReference messageReference) {
    super(messageReference);
  }

  public SampleClassForInvalidMessageReferences(MessageReference messageReference, String errorCode) {
    super(messageReference, errorCode);
  }

  public static final MessageReference EXIST = createMessageReference(SampleClassForInvalidMessageReferences.class, "EXIST");
  public static final MessageReference DOES_NOT_EXIST = createMessageReference(SampleClassForInvalidMessageReferences.class, "DOES_NOT_EXIST");
  public static final MessageReference EXITS_BUT_EMPTY = createMessageReference(SampleClassForInvalidMessageReferences.class, "EXITS_BUT_EMPTY");
}
