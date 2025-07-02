public class es10_Nome {
    public static boolean scan(String s){ //prende in input una stringa s
        int state = 0;   //stato dell'automa
        int i = 0;   //indice del prossimo carattere della strina da analizzare

        while (state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (ch == 'V')
                        state = 1;
                    else
                        state = 9;
                    break;

                case 1:
                    if (ch == 'e')
                        state = 2;
                    else
                        state = 10;
                    break;

                case 2:
                    if (ch == 'r')
                        state = 3;
                    else
                        state = 11;
                    break;

                case 3:
                    if (ch == 'o')
                        state = 4;
                    else
                        state = 12;
                    break;

                case 4:
                    if (ch == 'n')
                        state = 5;
                    else
                        state = 13;
                    break;

                case 5:
                    if (ch == 'i')
                        state = 6;
                    else
                        state = 14;
                    break;

                case 6:
                    if (ch == 'c')
                        state = 7;
                    else
                        state = 15;
                    break;

                case 7:
                    if (ch != ' ')
                        state = 8;
                    else
                        state = -1;
                    break;

                case 9:
                    if (ch == 'e')
                        state = 10;
                    else
                        state = -1;
                    break;

                case 10:
                    if (ch == 'r')
                        state = 11;
                    else
                        state = -1;
                    break;

                case 11:
                    if (ch == 'o')
                        state = 12;
                    else
                        state = -1;
                    break;

                case 12:
                    if (ch == 'n')
                        state = 13;
                    else
                        state = -1;
                    break;

                case 13:
                    if (ch == 'i')
                        state = 14;
                    else
                        state = -1;
                    break;

                case 14:
                    if (ch == 'c')
                        state = 15;
                    else
                        state = -1;
                    break;

                case 15:
                    if (ch == 'a')
                        state = 8;
                    else
                        state = -1;
                    break;

          }
        }

      return state == 8 ;   /* restituisce un valore booleano che indica se la
                            stringa appartiene al DFA */
    }

    public static void main(String[] args){
         System.out.println(scan(args[0]) ? "OK" : "NOPE");
      }
}
