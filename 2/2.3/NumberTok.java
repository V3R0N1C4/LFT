public class NumberTok extends Token {
  
    public String lexeme = " ";

    public NumberTok(int tag, String s) {
		    super(tag); //eredito tag da Token
        lexeme = s;
	  }

    public String toString() {
		    return "<" + tag + ", " + lexeme + ">";
	  }
}
