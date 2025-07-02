import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        //il costruttore inizializza il parser:
        lex = l;  // con un lexer
        pbr = br; // un lettoredi buffer
        move(); // metodo per ottenere il primo token dal lexer
    }

    void move() {
        // chiama il metodo lexical_scan del lexer per ottenere il token successivo
        look = lex.lexical_scan(pbr);
        // Il token viene memorizzato in look, e viene stampato a scopo di debug
        System.out.println("token = " + look);
    }

    void error(String s) {
       // viene chiamato quando per errori di sintassi nel parser
	     throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) { //controlla se si è alla fine del file o se ci sono errori
       // si controlla se il tipo del token corrente è quello che ci si aspetta
	     if (look.tag == t) {
          // Se i tag corrispondono, viene chiamato move() per ottenere il token successivo
	        if (look.tag != Tag.EOF) move();
          // altrimenti viene generato un errore di sintassi
	     } else error("syntax error");
    }

    public void start() {
	     if ( look.tag == '(' || look.tag == Tag.NUM){
         expr();
         match(Tag.EOF);
       } else error("error in syntax");
    }

    private void expr() {
      if ( look.tag == '(' || look.tag == Tag.NUM){ // GUIDA
      term();
      exprp();
    } else error("error in <expr>");
    }

    private void exprp() {
	     switch (look.tag) {

  	      case '+':
            match('+');
            term();
            exprp();
            break;

          case '-':
            match('-');
            term();
            exprp();
            break;

          case ')':
            break;

          case Tag.EOF:
            break;
	     }
     }


    private void term() {
      if ( look.tag == '(' || look.tag == Tag.NUM){ // GUIDA
      fact();
      termp();
    } else error("error in <term>");
    }

    private void termp() {
       switch (look.tag) {

          case '*':
            match('*');
            fact();
            termp();
            break;

          case '/':
            match('/');
            fact();
            termp();
            break;

          case '+':
  				    break;

  			  case '-':
  				    break;

          case ')':
            break;

          case Tag.EOF:
            break;
       }
    }

    private void fact() {
        switch(look.tag){// GUIDA

          case '(':
            match('(');
            expr();
            match(')');
            break;

          case Tag.NUM:
            match(Tag.NUM);
            break;

          default:
            error("error in <fact>");
            break;
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/veronicabosso/Desktop/LFT/Lab/4/4.1/Prova.text"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
