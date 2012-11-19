package com.sap.core.odata.core.uri.expression;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.uri.UriLiteral;
import com.sap.core.odata.api.uri.UriParserException;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;

public class Tokenizer
{

  private boolean flagIncludeWhitespace = false;
  private EdmSimpleTypeFacade typeDectector = null;

  public Tokenizer()
  {
    typeDectector = new EdmSimpleTypeFacadeImpl();
  }

  /**
   * Inform the Tokenizer whether extra tokens for whitespace characters should be added to the token list or not.
   * @param True -> Whitespace token will be added to token list; False otherwise
   * @return Returns this
   */
  public Tokenizer SetFlagWhiteSpace(Boolean flagIncludeWhitespace)
  {
    this.flagIncludeWhitespace = flagIncludeWhitespace;

    return this;
  }

  /**
   * Append StringValue Token to tokens parameter
   * @param Token list to be extended
   * @param Position of parsed token 
   * @param Kind of parsed token
   * @param Stringvalue of parsed token
   */
  private void appendToken(Vector<Token> tokens, int position, TokenKind kind, String stringValue)
  {
    Token token = new Token(kind, position, null, stringValue);
    tokens.add(token);
    return;
  }

  /**
   * Append CharValue Token to tokens parameter
   * @param Token list to be extended
   * @param Position of parsed token 
   * @param Kind of parsed token
   * @param Charvalue of parsed token
   */
  private void appendToken(Vector<Token> tokens, int position, TokenKind kind,  char charValue)
  {
    Token token = new Token(kind, position, null, Character.toString(charValue));
    tokens.add(token);
    return;
  }

  /**
   * Append UriLiteral Token to tokens parameter
   * @param Token list to be extended
   * @param Position of parsed token 
   * @param Kind of parsed token
   * @param UriLiteral of parsed token containing type and value of UriLiteral 
   */
  private void appendEdmTypedToken(Vector<Token> tokens, int iv_position, TokenKind iv_kind, UriLiteral uriLiteral)
  {
    Token token = new Token(iv_kind, iv_position, uriLiteral.getType(), uriLiteral.getLiteral());
    tokens.add(token);
    return;
  }

  /**
   * Tokenizes an expression as defined per OData specification 
   * @param Expression 
   * @return Token list 
   * @throws TokenizerException
   */
  public Vector<Token> tokenize(String iv_expression) throws TokenizerException
  {
    Vector<Token> tokens = Token.CreateTokenList();

    UriLiteral uriLiteral;
    int curPosition = 0;
    int curPositionPlus1;
    final int expressionLength;

    int oldPosition;
    char curCharacter;
    //String lv_cur_char_str;
    int lv_token_len;
    String token;
    String rem_expr;
    String expression_sub;
    Matcher matcher;

    expressionLength = iv_expression.length();
    while (curPosition < expressionLength)
    {
      oldPosition = curPosition;
      curCharacter = iv_expression.charAt(curPosition);

      switch (curCharacter)
      {
      case ' '://count whitespaces and move pointer to next non-whitespace char
        while ((curCharacter == ' ') && (curPosition < expressionLength))
        {
          curPosition = curPosition + 1;
          if (curPosition < expressionLength)
            curCharacter = iv_expression.charAt(curPosition);
        }

        lv_token_len = curPosition - oldPosition;

        if (flagIncludeWhitespace == true)
        {
          expression_sub = iv_expression.substring(oldPosition, oldPosition + lv_token_len);
          appendToken(tokens, oldPosition, TokenKind.WHITESPACE,  expression_sub);
        }
        break;
      case '(':
        curPosition = curPosition + 1;
        appendToken(tokens, oldPosition, TokenKind.OPENPAREN,  curCharacter);
        break;
      case ')':
        curPosition = curPosition + 1;
        appendToken(tokens, oldPosition, TokenKind.CLOSEPAREN,  curCharacter);
        break;
      case '\'':
        //read up to single ' and move pointer to the following char
        token = Character.toString(curCharacter);
        while (curPosition < expressionLength)
        {
          curPosition = curPosition + 1;
          if (curPosition < expressionLength)
          {
            curCharacter = iv_expression.charAt(curPosition);
            //lv_token = lv_token && lv_cur_char.
            token = token + curCharacter;

            curPositionPlus1 = curPosition + 1;
            if ((curCharacter == '\'') && (curPositionPlus1 < expressionLength)) //there is another char
            {
              curPosition = curPosition + 1;
              curCharacter = iv_expression.charAt(curPosition);
              if (curCharacter == '\'')
              {
                //double '
                //lv_token = lv_token && lv_cur_char.
                token = token + curCharacter;
              }
              else
              {
                break; //EXIT while loop. "done (single ')
              }
            }
          }
        } //end while

        try
        {
          uriLiteral = typeDectector.parse(token);
        } catch (UriParserException ex)
        {
          // TODO:  create method for InvalidStringToken ID
          TokenizerException tEx = new TokenizerException(TokenizerException.ParseStringToken);
          tEx.setPosition(curPosition);
          tEx.setToken(token);
          tEx.setPrevious(ex);
          throw tEx;
        }
        assert uriLiteral.getType() != null;

        appendEdmTypedToken(tokens, oldPosition, TokenKind.TYPED_LITERAL, uriLiteral);

        break;
      case ',':
      case '=':
      case '/':
      case '?':
      case '.':
      case '*':

        curPosition = curPosition + 1;
        appendToken(tokens, oldPosition, TokenKind.SYMBOL,  curCharacter);

        break;
      default:
        rem_expr = iv_expression.substring(curPosition); //remaining expression

        Pattern AND_SUB = Pattern.compile("^(and|or|eq|ne|lt|gt|le|ge)'");
        matcher = AND_SUB.matcher(rem_expr);

        if (matcher.find())
        {
          token = matcher.group(1);
          curPosition = curPosition + token.length();
          appendToken(tokens, oldPosition, TokenKind.LITERAL,  token);
        }

        //"check for special types" +
        Pattern X_BINARY = Pattern.compile("^(X|binary|guid|datetime|datetimeoffset|time)'");
        matcher = X_BINARY.matcher(rem_expr);
        token = "";

        if (matcher.find())
        {
          token = matcher.group(1);
          curPosition = curPosition + token.length();
          curCharacter = iv_expression.charAt(curPosition); //"should  be '

          //"read up to single ' and move pointer to the following char" +
          token = token + curCharacter;

          while (curPosition < expressionLength)
          {
            curPosition = curPosition + 1;
            if (curPosition < expressionLength)
            {
              curCharacter = iv_expression.charAt(curPosition);
              //lv_token = lv_token && lv_cur_char." 
              token = token + curCharacter;

              curPositionPlus1 = curPosition + 1;
              if ((curCharacter == '\'') && (curPositionPlus1 < expressionLength)) //"there is another char" 
              {
                curPosition = curPosition + 1;
                curCharacter = iv_expression.charAt(curPosition);
                if (curCharacter == '\'')
                {
                  //double '/
                  //lv_token = lv_token && lv_cur_char.
                  token = token + curCharacter;
                }
                else {
                  break;//"done (single ')
                }
              }
            }
          } //while

          try
          {
            uriLiteral = typeDectector.parse(token);

          } catch (UriParserException ex)
          {
            TokenizerException tEx = new TokenizerException(TokenizerException.ParseStringToken);
            tEx.setPosition(curPosition);
            tEx.setToken(token);
            tEx.setPrevious(ex);
            throw tEx;
          }

          appendEdmTypedToken(tokens, oldPosition, TokenKind.TYPED_LITERAL, uriLiteral);
          break;
        }// matcher matches

        Pattern AND_SUB1 = Pattern.compile("^(add|sub|mul|div|mod|not)'");
        matcher = AND_SUB1.matcher(rem_expr);

        if (matcher.find())
        {
          token = matcher.group(1);
          curPosition = curPosition + token.length();
          appendToken(tokens, oldPosition, TokenKind.LITERAL,  token);
          break;
        }

        Pattern FUNK = Pattern.compile("^(startswith|endswith|substring|substring|substringof|indexof|replace|tolower|toupper|trim|concat|length|year|mounth|day|hour|minute|second|round|ceiling|floor)( *)\\(");
        matcher = FUNK.matcher(rem_expr);

        if (matcher.find())
        {
          token = matcher.group(1);
          curPosition = curPosition + token.length();
          appendToken(tokens, oldPosition, TokenKind.LITERAL,  token);
          break;
        }

        Pattern OTHER_LIT = Pattern.compile("^([[A-Za-z0-9]._~%!$&*+;:@-]+)");
        matcher = OTHER_LIT.matcher(rem_expr);
        if (matcher.find())
        {
          token = matcher.group(1);
          try
          {
            uriLiteral = typeDectector.parse(token);
            curPosition = curPosition + token.length();
            appendEdmTypedToken(tokens, oldPosition, TokenKind.TYPED_LITERAL, uriLiteral);
            break;
          } catch (UriParserException ex)
          {}

          if (curCharacter == '-')
          {
            curPosition = curPosition + 1;
            appendToken(tokens, oldPosition, TokenKind.SYMBOL,  curCharacter);

            break;
          }

          curPosition = curPosition + token.length();
          appendToken(tokens, oldPosition, TokenKind.LITERAL,  token);

          break;
        }
        TokenizerException tEx = new TokenizerException(TokenizerException.ParseStringToken);
        tEx.setPosition(curPosition);
        tEx.setToken(token);
        throw tEx;
      } //ENDCASE.
    } //ENDWHILE.

    return tokens;
  }
}
