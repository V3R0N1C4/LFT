import java.io.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
	     lex = l;
	     pbr = br;
	     move();
    }

    void move() {
      // chiama il metodo lexical_scan del lexer per ottenere il token successivo
      look = lex.lexical_scan(pbr);
      // Il token viene memorizzato in look, e viene stampato a scopo di debug
      System.out.println("token = " + look);
    }

    void error(String s) {
      // viene chiamato quando per errori di sintassi nel parser
      throw new Error("line " + lex.line + ": " + s);
    }

    void match(int t) { //controlla se si è alla fine del file o se ci sono errori
       // si controlla se il tipo del token corrente è quello che ci si aspetta
	     if (look.tag == t) {
          // Se i tag corrispondono, viene chiamato move() per ottenere il token successivo
	        if (look.tag != Tag.EOF) {
            move();
          }
          // altrimenti viene generato un errore di sintassi
	     } else error("syntax error");
    }

    public void start() {
	     int expr_val;
       if(look.tag == '(' || look.tag == Tag.NUM){
    	    expr_val = expr();
	        match(Tag.EOF);
          System.out.println("Risultato = " + expr_val);
       } else {
        error("Errore in start");
      }
    }

    private int expr() {
	     int term_val, exprp_i, expr_val, exprp_val = 0;
       if (look.tag == '(' || look.tag == Tag.NUM){
    	    term_val = term();
          exprp_i = term_val;
	        exprp_val = exprp(term_val);
          expr_val = exprp_val;
       } else {
         error("error in expr");
       }
	   return exprp_val;
    }

    private int exprp(int exprp_i) {
	     int term_val, exprp_val;
	      switch (look.tag) {

	         case '+':
            match('+');
            term_val = term();
            exprp_val = exprp(exprp_i + term_val);
            break;

          case '-':
            match('-');
            term_val = term();
            exprp_val = exprp(exprp_i - term_val);
            break;

          default:
            exprp_val = exprp_i;
            break;
	         }
      return exprp_val;
    }

    private int term() {
      int fact_val, termp_i, termp_val, term_val = 0;
      if (look.tag == '(' || look.tag == Tag.NUM){
         fact_val = fact();
         termp_i = fact_val;
         termp_val = termp(termp_i);
         term_val = termp_val;
      } else {
        error("error in term");
      }
      return term_val;
    }

    private int termp(int termp_i) {
      int fact_val, termp_val;
       switch (look.tag) {

          case '*':
           match('*');
           fact_val = fact();
           termp_val = termp(termp_i * fact_val);
           break;

         case '/':
           match('/');
           fact_val = fact();
           termp_val = termp(termp_i / fact_val);
           break;

         default:
            termp_val = termp_i;
          break;
          }
     return termp_val;
    }

    private int fact() {
      int fact_val, expr_val, num_val;
      if (look.tag == '('){
        match('(');
        expr_val = expr();
        match(')');
        fact_val = expr_val;
      }
      else if(look.tag == Tag.NUM){
        fact_val = Integer.parseInt(((NumberTok)look).lexeme);
        match(Tag.NUM);
      }else{
        fact_val = 0;
      }
      return fact_val;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/veronicabosso/Desktop/LFT/Lab/4/4.1/Prova.text"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
