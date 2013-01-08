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

    while (curPosition < expressionLength)
    {
      oldPosition = curPosition;

      curCharacter = expression.charAt(curPosition);
      switch (curCharacter)
      {
      case ' ':
        //count whitespace and move pointer to next non-whitespace char
        eatWhiteSpaces(curPosition, curCharacter);
        break;

      case '(':
        tokens.appendToken(curPosition, TokenKind.OPENPAREN, curCharacter);
        curPosition = curPosition + 1;

        break;

      case ')':
        tokens.appendToken(curPosition, TokenKind.CLOSEPAREN, curCharacter);
        curPosition = curPosition + 1;
        break;

      case '\'':
        token = "";
        readLiteral(curCharacter);

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
        String rem_expr = expression.substring(curPosition); //remaining expression

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

        boolean isBoolean = checkForBoolean(oldPosition, rem_expr);
        if (isBoolean)
          break;
        
        
        boolean isLiteral = checkForLiteral(oldPosition, curCharacter, rem_expr);
        if (isLiteral)
          break;
            

        throw TokenizerException.createUNKNOWN_CHARACTER(oldPosition, token);
      } 
    }
    return tokens;
  }
  
  private boolean checkForLiteral(int oldPosition, char curCharacter, String rem_expr) 
  {
    //Pattern OTHER_LIT = Pattern.compile("^([[A-Za-z0-9]._~%!$&*+;:@-]+)");
    final Pattern OTHER_LIT = Pattern.compile("^([\\w._~%!$&*+;:@-]+)");
    Matcher matcher = OTHER_LIT.matcher(rem_expr);
    if (matcher.find())
    {
      token = matcher.group(1);
      try
      {
        EdmLiteral edmLiteral = typeDectector.parseUriLiteral(token);
        curPosition = curPosition + token.length();
        //it is a simple type
        tokens.appendEdmTypedToken(oldPosition, TokenKind.SIMPLE_TYPE, token, edmLiteral);
        return true;
      } catch (EdmLiteralException ex)
      { //we thread is as normal untyped literal 

      }

      //The '-' is checked here ( and not in the switch statement) because is may
      //part of negative number
      if (curCharacter == '-')
      {
        curPosition = curPosition + 1;
        tokens.appendToken(oldPosition, TokenKind.SYMBOL, curCharacter);
        return true;
      }

      curPosition = curPosition + token.length();
      tokens.appendToken(oldPosition, TokenKind.LITERAL, token);
      return true;
    }
    return false;
  }

  private boolean checkForBoolean(int oldPosition, String rem_expr) {
    if (rem_expr.equals("true") || rem_expr.equals("false"))
    {
      curPosition = curPosition + rem_expr.length();
      tokens.appendEdmTypedToken(oldPosition, TokenKind.SIMPLE_TYPE, rem_expr, new EdmLiteral(
          EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.Boolean), rem_expr));
      return true;
    }
    return false;
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

    if (matcher.find())
    {
      token = matcher.group(1);
      curPosition = curPosition + token.length();
      curCharacter = expression.charAt(curPosition); //"should  be '
      readLiteral(curCharacter);
      return true;
    }
    return false;
  }

  /**
   * Read up to single ' and move pointer to the following char and tries a type detection
   * @param expression
   * @param curCharacter
   * @throws FilterParserException
   * @throws TokenizerException
   */
  private void readLiteral(char curCharacter) throws FilterParserException, TokenizerException
  {
    int oldPosition = curPosition;
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

    try
    {
      EdmLiteral edmLiteral = typeDectector.parseUriLiteral(token);
      tokens.appendEdmTypedToken(oldPosition, TokenKind.SIMPLE_TYPE, token, edmLiteral);

    } catch (EdmLiteralException ex)
    {
      throw TokenizerException.createTYPEDECTECTION_FAILED_ON_STRING(ex, oldPosition, token);
    }
  }

}
