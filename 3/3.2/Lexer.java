import java.io.*;
import java.util.*;

public class Lexer {

    public static int line = 1; // linea in lettura
    private char peek = ' ';  // carattere in lettura

    private void readch(BufferedReader br) { // lettura prossimo carattere
        try {
            peek = (char) br.read();
            // br.read() restituisce un intero, valore ASCII del carattere letto
            // viene convertito in un carattere (char) e assegnato a peek
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
              //    spazio      tabulatore      nuova riga     ritorno a capo
            if (peek == '\n') line++;
              // se vero incrementa il contatore della linea
            readch(br);
              // legge il prossimo carattere
        }

        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

            case '(':
                peek = ' ';
                return Token.lpt;

            case ')':
                peek = ' ';
                return Token.rpt;

            case '[':
                peek = ' ';
                return Token.lpq;

            case ']':
                peek = ' ';
                return Token.rpq;

            case '{':
                peek = ' ';
                return Token.lpg;

            case '}':
                peek = ' ';
                return Token.rpg;

            case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            case '/':
                readch(br);

                if (Character.isLetterOrDigit(peek)) {
                return Token.div;
                } else {

                switch (peek) {

                  case '/':
                      while (peek != '\n' && peek != (char) -1) {
                          readch(br);
                      }
                      break;

                  case '*':
                      while (true) {
                          readch(br);

                        if (peek == '*') {
                              readch(br);
                              if (peek == '/') {
                                  readch(br);
                                  break;
                              } else {
                                System.err.println("Comment not closed");
                              }
                          }
                      }
                      break;

                  default:
                      System.err.println("Error in comment" + peek);
                      return null;
              }
          }
          return lexical_scan(br);


            case ';':
                peek = ' ';
                return Token.semicolon;

            case ',':
                peek = ' ';
                return Token.comma;

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : " + peek );
                    return null;
                }

            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                                + " after | : " + peek );
                    return null;
                }

            case '<':
                readch(br);
                if (peek == '='){
                  peek = ' ';
                  return Word.le;
                } else if (peek == '>'){
                  peek = ' ';
                  return Word.ne;
                } else {
                  peek = ' ';
                  return Word.lt;
                }

            case ':':
              readch(br);
              if (peek == '='){
                peek = ' ';
                return Word.init;
              } else {
                System.err.println("Erroneous character"
                            + " after : : "  + peek );
                return null;
              }

            case '=':
              readch(br);
              if (peek == '='){
                peek = ' ';
                return Word.eq;
                } else {
                  System.err.println("Erroneous character"
                              + " after = : "  + peek);
                  return null;
                }

            case '>':
                readch(br);
                if (peek == '='){
                  peek = ' ';
                  return Word.ge;
                } else {
                  peek = ' ';
                  return Word.gt;
                }

            case '_':
                String underscore = "_";
                readch(br);
                while (Character.isLetterOrDigit(peek) || peek == '_') {
                  underscore = underscore + peek;
                  readch(br);
                }
                boolean allUnderscores = true;
                for (int i = 0; i < underscore.length(); i++) {
                  if (underscore.charAt(i) != '_') {
                    allUnderscores = false;
                    break;
                  }
                }
                if (allUnderscores) {
                  System.err.println("Identifiers cannot be composed only of '_'");
                  return null;
                }
                return new Word(Tag.ID, underscore);


            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek)) {
                  	String s = new String();
                    do {
                      s = s + peek;
						          readch(br);
                    } while (Character.isLetterOrDigit(peek) || peek == '_');

                      switch (s){

                        case "assign":
                          return Word.assign;

                        case "to":
                          return Word.to;

                        case "if":
                          return Word.iftok;

                        case "else":
                          return Word.elsetok;

                        case "do":
                          return Word.dotok;

                        case "for":
                          return Word.fortok;

                        case "begin":
                          return Word.begin;

                        case "end":
                          return Word.end;

                        case "print":
                          return Word.print;

                        case "read":
                          return Word.read;

                        default:
                          return new Word(Tag.ID,s);
                      }

                } else if (Character.isDigit(peek)) {
                  String n = new String();
                  do {
                    n = n + peek;
                    readch(br);
                  } while (Character.isDigit(peek));
                  if (peek == '_' || Character.isLetter(peek) ) {
                    System.err.println("Identifiers cannot start with a number");
                    return null;
                  }
                    return new NumberTok(Tag.NUM, n);
                } else {
                        System.err.println("Erroneous character: "
                                + peek );
                        return null;
                }
         }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer(); //creo un oggetto di tipo Lexer
        String path = "/Users/veronicabosso/Desktop/LFT/Lab/3/3.2/Prova.text";
                      // il percorso del file da leggere

        try {
            //crea un buffer inserendoci il testo del file
            BufferedReader br = new BufferedReader(new FileReader(path));

            //variabile tok contiene i risultati della scansione lessicale
            Token tok;

            do {
                //scansione lessicale
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
                    //Il ciclo continua finché non viene raggiunto l'End-of-File
            //chiude il buffer
            br.close();

          //Stampa le informazioni sull'errore sulla console
        } catch (IOException e) {e.printStackTrace();}
    }

}
