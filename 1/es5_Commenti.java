public class es5_Commenti{
  public static boolean scan(String s){ //prende in input una stringa s
	  int state = 0;   //stato dell'automa
	  int i = 0;   //indice del prossimo carattere della strina da analizzare

	  while (state >= 0 && i < s.length()){
      final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
		   if (ch == '/')
		    state = 1;
		   else
		    state = -1;
		   break;

	    case 1:
		   if (ch == '*')
		    state = 2;
		   else
		    state = -1;
		   break;

	    case 2:
      if (ch == 'a' || ch == '/')
       state = 2;
      else if (ch == '*' )
       state = 3;
      else
       state = -1;
      break;

	    case 3:
      if (ch == '/')
       state = 4;
      else if (ch == '*' )
       state = 3;
      else if (ch == 'a' )
        state = 2;
       else
        state = -1;
        break;

      case 4:
       state = -1;
	    }
	  }

	return state == 4;
  }

  public static void main(String[] args){
	   System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
