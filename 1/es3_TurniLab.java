// 3
public class es3_TurniLab{
  public static boolean scan(String s){ //prende in input una stringa s
	  int state = 0;   //stato dell'automa
	  int i = 0;   //indice del prossimo carattere della strina da analizzare

	  while (state >= 0 && i < s.length()){
      final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
		   if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
		    state = 3;
       else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
        state = 1;
       else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch =='9')
        state = 2;
		   else
		    state = -1;
		   break;

	    case 1:
		   if (ch >= 'A' && ch <= 'K')
		    state = 4;
       else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
        state = 1;
       else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch =='9')
        state = 2;
		   else
		    state = -1;
		   break;

	    case 2:
      if (ch >= 'L' && ch <= 'Z')
       state = 4;
      else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
       state = 1;
      else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch =='9')
       state = 2;
      else
       state = -1;
      break;

	    case 3:
        break;

      case 4:
      if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
       state = 4;
      break;
	    }
	  }

	return state == 4;   /* restituisce un valore booleano che indica se la
                          stringa appartiene al DFA */
  }

  public static void main(String[] args){
	   System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
