// 1 bis
public class es1_NonTreZeri{
  public static boolean scan(String s){ //prende in input una stringa s
	  int state = 0;   //stato dell'automa
	  int i = 0;   //indice del prossimo carattere della strina da analizzare

	  while (state >= 0 && i < s.length()){
      final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
		   if (ch == '0')
		    state = 1;
		   else if (ch == '1')
		    state = 0;
		   else
		    state = -1;
		   break;

	    case 1:
		   if (ch == '0')
		    state = 2;
		   else if (ch == '1')
		    state = 0;
		   else
		    state = -1;
		   break;

	    case 2:
		   if (ch == '0')
		    state = 3;
		   else if (ch == '1')
		    state = 0;
		   else
		    state = -1;
		   break;

	    case 3:
		   if (ch == '0' || ch == '1')
		    state = 3;
       else
		    state = -1;
		  break;
	    }
	  }

	return state != 3 && state != -1;   /* restituisce un valore booleano che indica se la
                          stringa appartiene al DFA */
  }

  public static void main(String[] args){
	   System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
