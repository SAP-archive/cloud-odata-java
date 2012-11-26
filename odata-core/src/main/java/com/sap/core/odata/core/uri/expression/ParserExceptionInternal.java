package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.uri.expression.ExpressionException;

public class ParserExceptionInternal extends ExpressionException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  
  public static final int ParseStringToken = 1;
  
  private int textID;
  private String token;
  private Exception previous;
  private int position;
  
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }


  public Exception getPrevious() {
    return previous;
  }


  public void setPrevious(Exception previous) {
    this.previous = previous;
  }


  public int getPosition() {
    return position;
  }


  public void setPosition(int position) {
    this.position = position;
  }


   
  
  public ParserExceptionInternal(int textID)
  {
    //super(textID);
    this.textID = textID;
    this.token = token;
    this.previous = previous;
  }
}

