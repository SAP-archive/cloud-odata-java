package com.sap.core.odata.core.uri.expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmLiteralException;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.uri.expression.FilterParserException;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;

public class Tokenizer
{

  private boolean flagIncludeWhitespace = false;
  private EdmSimpleTypeFacade typeDectector = null;

  int curPosition;
  String token;
  final String expression;
  final int expressionLength;
  TokenList tokens;

  public Tokenizer(String expression)
  {
    typeDectector = new EdmSimpleTypeFacadeImpl();
    this.expression = expression;
    this.expressionLength = expression.length();
    tokens = new TokenList();
  }

  /**
   * Inform the Tokenizer whether extra tokens for whitespace characters should be added to the token list or not.
   * @param flagIncludeWhitespace True -> Whitespace token will be added to token list; False otherwise
   * @return this
   */
  public Tokenizer setFlagWhiteSpace(Boolean flagIncludeWhitespace)
  {
    this.flagIncludeWhitespace = flagIncludeWhitespace;

    return this;
  }

  /**
   * Tokenizes an expression as defined per OData specification 
   * @return Token list 
   */
  public TokenList tokenize() throws TokenizerException, FilterParserException
  {
    EdmLiteral edmLiteral;
    curPosition = 0;
    int oldPosition;
    char curCharacter;
    token = "";
    String rem_expr;
    Matcher matcher;

    while (curPosition < expressionLength)
    {
      oldPosition = curPosition;

      curCharacter = expression.charAt(curPosition);
      switch (curCharacter)
      {
      case ' ':
        //count whitespace and move pointer to next non-whitespace char
        eatWhiteSpaces(oldPosition, curCharacter);
        break;

      case '(':
        curPosition = curPosition + 1;
        tokens.appendToken(oldPosition, TokenKind.OPENPAREN, curCharacter);
        break;

      case ')':
        curPosition = curPosition + 1;
        tokens.appendToken(oldPosition, TokenKind.CLOSEPAREN, curCharacter);
        break;

      case '\'':
        //read up to single ' and move pointer to the following char
        token = "";
        readLiteral(expression, expressionLength, curCharacter);

        try
        {
          edmLiteral = typeDectector.parseUriLiteral(token);
        } catch (EdmLiteralException ex)
        {
          throw TokenizerException.createTYPEDECTECTION_FAILED_ON_STRING(ex, oldPosition, token);
        }

        tokens.appendEdmTypedToken(oldPosition, TokenKind.SIMPLE_TYPE, token, edmLiteral);
        break;

      case ',':
        curPosition = curPosition + 1;
        tokens.appendToken(oldPosition, TokenKind.COMMA, curCharacter);
        break;

      case '=':
      case '/':
      case '?':
      case '.':
      case '*':
        curPosition = curPosition + 1;
        tokens.appendToken(oldPosition, TokenKind.SYMBOL, curCharacter);
        break;
        
      default:
        rem_expr = expression.substring(curPosition); //remaining expression

        boolean isBinary = checkForBinary(oldPosition, rem_expr);
        if (isBinary)
          break;

        //check for prefixes like X, binary, guid, datetime
        boolean isPrefix = checkForPrefix(rem_expr);
        if (isPrefix)
          break;

        //check for math
        boolean isMath = checkForMath(oldPosition, rem_expr);
        if (isMath)
          break;

        //check for function
        boolean isFunction = checkForFunction(oldPosition, rem_expr);
        if (isFunction)
          break;

        if (rem_expr.equals("true") || rem_expr.equals("false"))
        {
          curPosition = curPosition + rem_expr.length();
          tokens.appendEdmTypedToken(oldPosition, TokenKind.SIMPLE_TYPE, rem_expr, new EdmLiteral(
              EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Boolean), rem_expr));
          break;
        }

        //Pattern OTHER_LIT = Pattern.compile("^([[A-Za-z0-9]._~%!$&*+;:@-]+)");
        Pattern OTHER_LIT = Pattern.compile("^([\\w._~%!$&*+;:@-]+)");
        matcher = OTHER_LIT.matcher(rem_expr);
        if (matcher.find())
        {
          token = matcher.group(1);
          try
          {
            edmLiteral = typeDectector.parseUriLiteral(token);
            curPosition = curPosition + token.length();

            //its really a simple type
            tokens.appendEdmTypedToken(oldPosition, TokenKind.SIMPLE_TYPE, token, edmLiteral);

            break;
          } catch (EdmLiteralException ex)
          {
            //we thread is as normal literal
          }

          if (curCharacter == '-')
          {
            curPosition = curPosition + 1;

            tokens.appendToken(oldPosition, TokenKind.SYMBOL, curCharacter);

            break;
          }

          curPosition = curPosition + token.length();

          tokens.appendToken(oldPosition, TokenKind.LITERAL, token);

          break;
        }

        throw TokenizerException.createUNKNOWN_CHARACTER(oldPosition, token);
      } //ENDCASE.
    } //ENDWHILE.

    return tokens;
  }

  private void eatWhiteSpaces(int oldPosition, char curCharacter) {
    int lv_token_len;
    String expression_sub;
    while ((curCharacter == ' ') && (curPosition < expressionLength))
    {
      curPosition = curPosition + 1;
      if (curPosition < expressionLength)
        curCharacter = expression.charAt(curPosition);
    }

    lv_token_len = curPosition - oldPosition;

    if (flagIncludeWhitespace == true)
    {
      expression_sub = expression.substring(oldPosition, oldPosition + lv_token_len);
      tokens.appendEdmTypedToken(oldPosition, TokenKind.WHITESPACE, expression_sub, null);

    }
  }

  private boolean checkForFunction(int oldPosition, String rem_expr)
  {
    final Pattern FUNK = Pattern.compile("^(startswith|endswith|substring|substring|substringof|indexof|replace|tolower|toupper|trim|concat|length|year|mounth|day|hour|minute|second|round|ceiling|floor)( *)\\(");
    Matcher matcher = FUNK.matcher(rem_expr);

    if (matcher.find())
    {
      token = matcher.group(1);
      curPosition = curPosition + token.length();
      tokens.appendToken(oldPosition, TokenKind.LITERAL, token);
      return true;
    }
    return false;
  }

  private boolean checkForMath(int oldPosition, String rem_expr) {
    final Pattern AND_SUB1 = Pattern.compile("^(add|sub|mul|div|mod|not) ");
    Matcher matcher1 = AND_SUB1.matcher(rem_expr);

    if (matcher1.find())
    {
      token = matcher1.group(1);
      curPosition = curPosition + token.length();
      tokens.appendToken(oldPosition, TokenKind.LITERAL, token);
      return true;
    }
    return false;
  }

  private boolean checkForBinary(int oldPosition, String rem_expr) {
    final Pattern AND_SUB = Pattern.compile("^(and|or|eq|ne|lt|gt|le|ge) ");
    Matcher matcher1 = AND_SUB.matcher(rem_expr);

    if (matcher1.find())
    {
      token = matcher1.group(1);
      curPosition = curPosition + token.length();
      tokens.appendToken(oldPosition, TokenKind.LITERAL, token);
      return true;
    }
    return false;
  }

  private boolean checkForPrefix(String rem_expr) throws FilterParserException, TokenizerException {
    final Pattern prefix = Pattern.compile("^(X|binary|guid|datetime|datetimeoffset|time)'");
    Matcher matcher = prefix.matcher(rem_expr);
    token = "";
    char curCharacter;
    EdmLiteral edmLiteral;

    if (matcher.find())
    {
      token = matcher.group(1);
      curPosition = curPosition + token.length();
      curCharacter = expression.charAt(curPosition); //"should  be '

      readLiteral(expression, expressionLength, curCharacter);

      try
      {
        edmLiteral = typeDectector.parseUriLiteral(token);
      } catch (EdmLiteralException ex)
      {
        throw TokenizerException.createTYPEDECTECTION_FAILED_ON_EDMTYPE(ex, curPosition, token);
      }

      tokens.appendEdmTypedToken(curPosition, TokenKind.SIMPLE_TYPE, token, edmLiteral);
      return true;
    }// matcher matches
    return false;
  }

  private void readLiteral(String expression, final int expressionLength, char curCharacter) throws FilterParserException {

    token += Character.toString(curCharacter);
    curPosition += 1;

    boolean wasHochkomma = false; //leading ' does not count
    while (curPosition < expressionLength)
    {
      curCharacter = expression.charAt(curPosition);

      if (curCharacter != '\'')
      {
        if (wasHochkomma == true)
          break;

        token = token + curCharacter;
        wasHochkomma = false;
      }
      else
      {
        if (wasHochkomma)
        {
          wasHochkomma = false; //a double ' is a normal character '
        }
        else
        {
          wasHochkomma = true;
          token = token + curCharacter;
        }
      }
      curPosition += 1;
    }

    if (!wasHochkomma)
      //Exception tested within TestPMparseFilterString
      throw FilterParserExceptionImpl.createTOKEN_UNDETERMINATED_STRING(curPosition, expression);

    //*********************************************
    /*
    int stack = 0; //zero ' read
    token = Character.toString(curCharacter);
    stack = 1;     //one ' read
    curPosition += 1;
    
    while (curPosition < expressionLength)
    {
      curCharacter = expression.charAt(curPosition);
      curPosition += 1;
      
      if (curCharacter == '\'')
      {
        if (stack == 1)
        {
          token = token + curCharacter;
          stack = 2; //two read
        }
        else if (stack == 2)
        {
          stack = 1; //a double '' was read
        }
      }
      else
      {
        if ( stack == 2)
          break;  //
        token = token + curCharacter;
      }
    }
    
    if (stack != 2)
      //Exception tested within TestPMparseFilterString
      throw FilterParserExceptionImpl.createTOKEN_UNDETERMINATED_STRING(curPosition,expression);
    */
    //*****************************
    /*
    //at this point the curPosition points to the first '
    token = Character.toString(curCharacter);
    curPosition += 1;
    boolean stringClosed = false;
    
    while (curPosition < expressionLength)
    {
      curCharacter = expression.charAt(curPosition);
      token = token + curCharacter;

      if ((curCharacter == '\'') && ((curPosition + 1) < expressionLength)) //there is another char
      {
        curPosition+= 1;
        curCharacter = expression.charAt(curPosition);
        if (curCharacter == '\'')
        {
          token = token + curCharacter;  //it is a double '
        }
        else
        {
          stringClosed = true;
          break; //closing ' found
        }
      }
      curPosition += 1;
    } 

    if (stringClosed == false)
      //Exception tested within TestPMparseFilterString
      throw FilterParserExceptionImpl.createTOKEN_UNDETERMINATED_STRING(curPosition,expression);
    */
  }

}
