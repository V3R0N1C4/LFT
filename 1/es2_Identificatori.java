// 2
public class es2_Identificatori{
  public static boolean scan(String s){ //prende in input una stringa s
	  int state = 0;   //stato dell'automa
	  int i = 0;   //indice del prossimo carattere della strina da analizzare

	  while (state >= 0 && i < s.length()){
      final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
		   if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
		    state = 1;
		   else if (ch == '_')
		    state = 3;
       else if (ch >= '0' && ch <= '9')
        state = 2;
		   else
		    state = -1;
		   break;

	    case 1:
		   if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
		    state = 1;
		   else if (ch == '_')
		    state = 1;
       else if (ch >= '0' && ch <= '9')
        state = 1;
		   else
		    state = -1;
		   break;

	    case 2:
      if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
       state = 2;
      else if (ch == '_')
       state = 2;
      else if (ch >= '0' && ch <= '9')
       state = 2;
      else
       state = -1;
      break;

	    case 3:
       if (ch == '_')
        state = 3;
       else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
        state = 1;
       else if (ch >= '0' && ch <= '9')
        state = 1;
       else
        state = -1;
      break;
	    }
	  }

	return state == 1;   /* restituisce un valore booleano che indica se la
                          stringa appartiene al DFA */
  }

  public static void main(String[] args){
	   System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
