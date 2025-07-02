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

    public void prog() {
	     if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT
                  || look.tag == Tag.READ || look.tag == Tag.FOR
                  || look.tag == Tag.IF || look.tag == '{' ){
         statlist();
         match(Tag.EOF);
       } else error("error in syntax");
    }

    private void statlist(){
      if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT
          || look.tag == Tag.READ || look.tag == Tag.FOR
          || look.tag == Tag.IF ||look.tag == '{' ){
        stat();
        statlistp();
      } else error("error in statlist");
    }

    private void stat(){
      switch (look.tag) {

        case Tag.ASSIGN:
          match(Tag.ASSIGN);
          assignlist();
          break;

        case Tag.PRINT:
          match(Tag.PRINT);
          match('(');
          exprlist();
          match(')');
          break;

        case Tag.READ:
          match(Tag.READ);
          match('(');
          idlist();
          match(')');
          break;

        case Tag.FOR:
          match(Tag.FOR);
          match('(');
          metodoFor();
          match(')');
          match(Tag.DO);
          stat();
          break;

        case Tag.IF:
          match(Tag.IF);
          match('(');
          bexpr();
          match(')');
          stat();
          ifelse();
          break;

        case '{':
          match('{');
          statlist();
          match('}');
          break;
      }
    }

    private void metodoFor(){
      if(look.tag == Tag.ID){
        match(Tag.ID);
        match(Tag.INIT);
        expr();
        match(';');
        bexpr();
      }else if(look.tag == Tag.RELOP){
        bexpr();
      }else{
        error("error in metodoFor");
      }
    }

    private void bexpr(){
      if (look.tag == Tag.RELOP){
        match(Tag.RELOP);
        expr();
        expr();
      } else error("error in bexpr");
    }

    private void ifelse(){
      if (look.tag == Tag.ELSE){
        match(Tag.ELSE);
        stat();
        match(Tag.END);
      } else{
        match(Tag.END);
      }
    }

    private void statlistp(){
        switch(look.tag){

          case ';':
            match(';');
            stat();
            statlistp();
            break;

          case Tag.EOF:
            match(Tag.EOF);

      }
    }

    private void assignlist(){
      if (look.tag == '['){
        match('[');
        expr();
        match(Tag.TO);
        idlist();
        match(']');
        assignlistp();
      } else {
        error("error assignlist");
      }
    }

    private void assignlistp(){
        switch (look.tag) {

          case '[':
            match('[');
            expr();
            match(Tag.TO);
            idlist();
            match(']');
            assignlist();
            break;

          case Tag.EOF:
            break;
    }
  }

    private void exprlist(){
      if (look.tag == '+' || look.tag == '-' || look.tag == '*'
       || look.tag == '/' || look.tag == Tag.NUM || look.tag == Tag.ID){
        expr();
        exprlistp();
      }
    }

    private void exprlistp(){
        switch (look.tag) {

          case ',':
            match(',');
            expr();
            exprlistp();
            break;

          case Tag.EOF:
            match(Tag.EOF);
            break;

      }
    }

    private void expr() {
      switch(look.tag){ // GUIDA

        case '+':
          match('+');
          match('(');
          exprlist();
          match(')');
          break;

        case '-':
          match('-');
          expr();
          expr();
          break;

        case '*':
          match('*');
          match('(');
          exprlist();
          match(')');
          break;

        case '/':
          match('/');
          expr();
          expr();
          break;

        case Tag.NUM:
          match(Tag.NUM);
          break;

        case Tag.ID:
          match(Tag.ID);
          break;

        default:
          error("error in expr");
          break;
    }
    }

    private void idlist(){
      if(look.tag == Tag.ID){
        match(Tag.ID);
        idlistp();
      } else if (look.tag == Tag.NUM){
        match(Tag.NUM);
        idlistp();
      }
        else error ("error in idlist");
    }

    private void idlistp(){
        switch(look.tag){

          case ',':
            match(',');
            match(Tag.ID);
            idlistp();
            break;

          case Tag.EOF:
            match(Tag.EOF);
            break;

      }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/veronicabosso/Desktop/LFT/Lab/3/3.2/Prova.text"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
