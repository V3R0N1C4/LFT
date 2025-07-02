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
                peek = ' ';
                return Token.div;

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
                            + " after : : "  + peek);
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

            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek)) {
                  	String s = new String();
                    do {
                      s = s + peek;
						          readch(br);
                    } while (Character.isLetterOrDigit(peek));

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
                    return new NumberTok(Tag.NUM, n);
                } else {
                        System.err.println("Erroneous character: " + peek );
                        return null;
                }
         }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer(); //creo un oggetto di tipo Lexer
        String path = "/Users/veronicabosso/Desktop/LFT/Lab/2/2.1/Prova.text"; // il percorso del file da leggere

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
