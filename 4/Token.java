public class Token {
    public final int tag;

    public Token(int t) { //costruttore
      tag = t;
      //tag prende il valore in ASCII del simbolo che ritorna da Lexer
    }

    public String toString() {
      return "<" + tag + ">";
      //stampa il valore in ASCII
    }
    
    public static final Token
	     not = new Token('!'),
	     lpt = new Token('('),
	     rpt = new Token(')'),
	     lpq = new Token('['),
	     rpq = new Token(']'),
	     lpg = new Token('{'),
	     rpg = new Token('}'),
	     plus = new Token('+'),
	     minus = new Token('-'),
	     mult = new Token('*'),
	     div = new Token('/'),
	     semicolon = new Token(';'),
	     comma = new Token(',');
}
