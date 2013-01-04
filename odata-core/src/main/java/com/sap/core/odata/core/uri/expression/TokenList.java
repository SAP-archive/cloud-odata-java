package com.sap.core.odata.core.uri.expression;

import java.util.Iterator;
import java.util.Vector;

import com.sap.core.odata.api.edm.EdmLiteral;

public class TokenList implements Iterator<Token>
{
  private Vector<Token> tokens = null;
  int currentToken = 0;

  public TokenList()
  {
    this.tokens = new Vector<Token>();
  }

  /**
   * Append StringValue Token to tokens parameter
   * @param position Position of parsed token 
   * @param kind Kind of parsed token
   * @param uriLiteral String value of parsed token
   */
  public void appendToken(int position, TokenKind kind, String uriLiteral)
  {
    Token token = new Token(kind, position, uriLiteral);
    this.tokens.add(token);
    return;
  }

  /**
   * Append CharValue Token to tokens parameter
   * @param position Position of parsed token 
   * @param kind Kind of parsed token
   * @param charValue Char value of parsed token
   */
  public void appendToken(int position, TokenKind kind, char charValue)
  {
    Token token = new Token(kind, position, Character.toString(charValue));
    this.tokens.add(token);
    return;
  }

  /**
   * Append UriLiteral Token to tokens parameter
   * @param position Position of parsed token 
   * @param kind Kind of parsed token
   * @param javaLiteral EdmLiteral of parsed token containing type and value of UriLiteral 
   */
  public void appendEdmTypedToken(int position, TokenKind kind, String uriLiteral, EdmLiteral javaLiteral)
  {
    Token token = new Token(kind, position, uriLiteral, javaLiteral);
    this.tokens.add(token);
    return;
  }

  public Token lookToken()
  {
    if (currentToken >= tokens.size())
      return null;

    return tokens.get(currentToken);
  }

  public boolean hasTokens()
  {
    return (tokens.size() > 0);
  }

  public int tokenCount()
  {
    int i = tokens.size();

    return i;
  }

  public Token expectToken(TokenKind comma) throws TokenizerExpectError
  {
    Token actual = next();
    if (comma != actual.getKind())
      throw TokenizerExpectError.createINVALID_TOKENKIND_AT(comma, actual);
    return actual;
  }

  public Token expectToken(String literal) throws TokenizerExpectError
  {
    Token actual = next();
    if (!literal.equals(actual.getUriLiteral()))
      throw TokenizerExpectError.createINVALID_TOKEN_AT(literal, actual);
    return actual;
  }

  public void skip()
  {
    currentToken++;
  }

  @Override
  public boolean hasNext() {
    return (currentToken < tokens.size());
  }

  @Override
  public Token next() {
    if (currentToken >= tokens.size())
      return null;

    Token ret = tokens.get(currentToken);
    currentToken++;
    return ret;
  }

  @Override
  public void remove() {
    // TODO Auto-generated method stub
  }

  public Token elementAt(int index) {

    return tokens.elementAt(index);
  }

}