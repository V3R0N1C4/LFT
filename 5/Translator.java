import java.io.*;

public class Translator {
    private Lexer lex; //Analizzatore lessicale per ottenere i token dal file di input
    private BufferedReader pbr; //Lettore del file di input
    private Token look;

    SymbolTable st = new SymbolTable(); //Tabella dei simboli per tenere traccia delle variabili
    CodeGenerator code = new CodeGenerator(); //Generatore di codice per generare il codice Jasmin
    int count = 0; //Contatore per tenere traccia delle variabili.

    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
      // chiama il metodo lexical_scan del lexer per ottenere il token successivo
      look = lex.lexical_scan(pbr);
      // Il token viene memorizzato in look, e viene stampato a scopo di debug
      System.out.println("Token = " + look);
    } //Avanza al prossimo token nel lexer

    void error(String s) {
      // viene chiamato quando per errori di sintassi nel parser
      throw new Error("Line " + lex.line + ": " + s);
    }

    void match(int t) {//controlla se si è alla fine del file o se ci sono errori
       // si controlla se il tipo del token corrente è quello che ci si aspetta
	     if (look.tag == t) {
          // Se i tag corrispondono, viene chiamato move() per ottenere il token successivo
	        if (look.tag != Tag.EOF) move();
          // altrimenti viene generato un errore di sintassi
	     } else error("syntax error");
    }

    public void prog(){
      if(look.tag == Tag.READ || look.tag == Tag.ASSIGN
      || look.tag == Tag.PRINT|| look.tag == Tag.FOR
      || look.tag == Tag.IF || look.tag == '{'){
        int lnext_prog = code.newLabel();
        statlist(lnext_prog);
        match(Tag.EOF);
        try {
        	code.toJasmin();
        }
        catch(java.io.IOException e) {
        	System.out.println("IO error \n");
        }
      } else {
        System.err.println("Errore in prog");
      }
    }

    public void statlist(int lnext_statlist){
      if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT
          || look.tag == Tag.READ || look.tag == Tag.FOR
          || look.tag == Tag.IF ||look.tag == '{' ){
        int label_sl = code.newLabel();
        stat(label_sl);
        code.emitLabel(label_sl);
        statlistp(lnext_statlist);
      } else error("error in statlist");
    }

    public void statlistp(int lnext_statlistp){
      int label_slp = code.newLabel();
      if(look.tag == ';' || look.tag == Tag.EOF){
        switch (look.tag) {

          case ';':
            match(';');
            stat(label_slp);
            code.emitLabel(label_slp);
            statlistp(lnext_statlistp);
            break;

          case Tag.EOF:
            match(Tag.EOF);
            break;
        }
      }
    }

    public void stat(int lnext_stat) {
        switch(look.tag) {

            case Tag.READ:
                match(Tag.READ);
                match('(');
                code.emit(OpCode.invokestatic, 0);
                idlist();
                match(')');
                code.emit(OpCode.GOto, lnext_stat);
                break;

            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                assignlist();
                code.emit(OpCode.GOto, lnext_stat);
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist();
                match(')');
                code.emit(OpCode.invokestatic, 1);
                code.emit(OpCode.GOto, lnext_stat);
                break;

            case Tag.FOR:
                match(Tag.FOR);
                match('(');
                int label_true_for = code.newLabel();
                int label_false_for = code.newLabel();
                if(look.tag == Tag.ID){
                  match(Tag.ID);
                  match(Tag.INIT);
                  expr();
                  match(';');
                  bexpr(label_true_for, label_false_for);
                }else if(look.tag == Tag.RELOP){
                  bexpr(label_true_for, label_false_for);
                  code.emitLabel(label_true_for);
                }
                match(')');
                match(Tag.DO);
                stat(label_true_for);
                code.emitLabel(label_false_for);
                break;

           case Tag.IF:
                match(Tag.IF);
                match('(');
                int label_true_if = code.newLabel();
                int label_false_if = code.newLabel();
                bexpr(label_true_if, label_false_if);
                match(')');
                code.emitLabel(label_true_if);
                stat(lnext_stat);
                if(look.tag == Tag.ELSE){
                  code.emitLabel(label_false_if);
                  match(Tag.ELSE);
                  stat(lnext_stat);
                }else{
                  code.emitLabel(label_false_if);
                }
                match(Tag.END);
                break;

           case '{':
                match('{');
                statlist(lnext_stat);
                match('}');
                break;
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
             assignlistp();
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
     }else{
       error("errore in exprlist");
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

    private void idlist() {
        if(look.tag == Tag.ID){
            int addr = st.lookupAddress(((Word)look).lexeme);
                if (addr == -1) {
                    addr = count;
                    st.insert(((Word)look).lexeme, addr);
                    count++;
                }
        match(Tag.ID);
        code.emit(OpCode.istore, addr);
        idlistp();
      } else {
        error ("error in idlist");
      }
    	}

    private void idlistp(){
        switch(look.tag){

          case ',':
            match(',');
            if(look.tag == Tag.ID){
                int addr = st.lookupAddress(((Word)look).lexeme);
                    if (addr == -1) {
                        addr = count;
                        st.insert(((Word)look).lexeme, addr);
                        count++;
                    }
            match(Tag.ID);
            code.emit(OpCode.istore, addr);
            }
            idlistp();
            break;

          case Tag.EOF:
            match(Tag.EOF);
            break;
      }
    }

    private void bexpr(int bexpr_T, int bexpr_F){
      switch (look.tag) {

			case Tag.RELOP	:

      if(look == Word.lt){
        match(Tag.RELOP);
				expr();
				expr();
				code.emit(OpCode.if_icmplt, bexpr_T);
				code.emit(OpCode.GOto,bexpr_F);
			 }
       else if (look == Word.gt){
				match(Tag.RELOP);
        expr();
        expr();
        code.emit(OpCode.if_icmpgt, bexpr_T);
        code.emit(OpCode.GOto,bexpr_F);
			  }
       else if(look == Word.eq){
				match(Tag.RELOP);
				expr();
				expr();
				code.emit(OpCode.if_icmpeq, bexpr_T);
				code.emit(OpCode.GOto, bexpr_F);
        }
       else if(look == Word.le){
         match(Tag.RELOP);
         expr();
         expr();
         code.emit(OpCode.if_icmple, bexpr_T);
				 code.emit(OpCode.GOto, bexpr_F);
        }
       else if (look == Word.ne){
				match(Tag.RELOP);
        expr();
        expr();
        code.emit(OpCode.if_icmpne, bexpr_T);
        code.emit(OpCode.GOto, bexpr_F);
      }
       else if (look == Word.ge){
				match(Tag.RELOP);
        expr();
        expr();
        code.emit(OpCode.if_icmpge, bexpr_T);
        code.emit(OpCode.GOto, bexpr_F);
      }
      break;
      }
    }

    private void expr() {
        switch(look.tag) {

            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                count--;
                break;

            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                  code.emit(OpCode.iadd);
                break;

            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                  code.emit(OpCode.imul);
                break;

            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                count--;
                break;

            case Tag.NUM:
                count++;
                int num = Integer.parseInt(((NumberTok)look).lexeme);
                code.emit(OpCode.ldc, num);
                match(Tag.NUM);
                break;

            case Tag.ID:
                int addr = st.lookupAddress(((Word)look).lexeme);
                match(Tag.ID);
                code.emit(OpCode.iload, addr);
                break;
        }
    }

    public static void main(String[] args) {
      Lexer lex = new Lexer();

      String path = "/Users/veronicabosso/Desktop/LFT/Lab/5/Prova1.lft";
      try{
        BufferedReader br = new BufferedReader(new FileReader(path));
        Translator translator = new Translator(lex,br);
        translator.prog();
        br.close();
      } catch (IOException e){e.printStackTrace();}
    }
}
