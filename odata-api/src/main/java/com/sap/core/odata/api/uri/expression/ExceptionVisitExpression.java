package com.sap.core.odata.api.uri.expression;
/*TODO*/
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * Exception thrown while traversing/visiting a filter expression tree
 * @author SAP AG
 */
public class ExceptionVisitExpression extends ODataMessageException
{
  private static final long serialVersionUID = 77L;

  public static final MessageReference COMMON = createMessageReference(ODataMessageException.class, "COMMON");

  private CommonExpression filterTree;

  public ExceptionVisitExpression() {
    super(COMMON);
  }

  public ExceptionVisitExpression(MessageReference messageReference) {
    super(messageReference);
  }

  public ExceptionVisitExpression(MessageReference message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Get erroneous filter for debug information
   * @return Erroneous filter tree 
   */
  public CommonExpression getFilterTree() {
    return filterTree;
  }

  /**
   * See erroneous filter tree for debug information
   * @param Erroneous filter tree
   */
  public void setFilterTree(CommonExpression filterTree)
  {
    this.filterTree = filterTree;
  }

}
