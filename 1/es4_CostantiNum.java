public class es4_CostantiNum{
  public static boolean scan(String s){ //prende in input una stringa s
	  int state = 0;   //stato dell'automa
	  int i = 0;   //indice del prossimo carattere della strina da analizzare

	  while (state >= 0 && i < s.length()){
      final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
		   if (ch >= '0' && ch <= '9')
		    state = 1;
       else if (ch == '+' || ch == '-')
        state = 2;
       else if (ch == '.')
        state = 3;
		   else
		    state = -1;
		   break;

	    case 1:
		   if (ch >= '0' && ch <= '9')
		    state = 1;
       else if (ch == '.')
        state = 3;
       else if (ch == 'e')
        state = 5;
		   else
		    state = -1;
		   break;

	    case 2:
      if (ch == '.')
       state = 3;
      else if (ch >= '0' && ch <= '9')
       state = 1;
      else
       state = -1;
      break;

	    case 3:
      if (ch >= '0' && ch <= '9')
       state = 4;
       else
        state = -1;
        break;

      case 4:
      if (ch >= '0' && ch <= '9')
       state = 4;
       else if(ch == 'e')
        state = 5;
       else
        state = -1;
      break;

      case 5:
      if (ch >= '0' && ch <= '9')
       state = 6;
      else
        state = -1;
      break;

      case 6:
      if (ch >= '0' && ch <= '9')
       state = 6;
      else if( ch == '.')
        state = 7;
      else
        state = -1;
      break;

      case 7:
      if (ch >= '0' && ch <= '9')
       state = 8;
      else if( ch == '+' || ch == '-')
        state = 9;
      else
        state = -1;
      break;

      case 8:
      if (ch >= '0' && ch <= '9')
       state = 8;
      else
        state = -1;
      break;

      case 9:
      if (ch >= '0' && ch <= '9')
       state = 8;
      else
        state = -1;
      break;
	    }
	  }

	return state == 1 || state == 4 || state == 6 || state == 8 ;
  }

  public static void main(String[] args){
	   System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
