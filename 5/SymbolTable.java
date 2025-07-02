import java.util.*;

public class SymbolTable {
  //Mappa che associa nomi di variabili agli indirizzi di memoria
     Map <String, Integer> OffsetMap = new HashMap <String,Integer>();

	public void insert( String s, int address ) {
    //Inserisce una variabile nella tabella dei simboli
            if( !OffsetMap.containsValue(address) )
                OffsetMap.put(s,address);
            else
                throw new IllegalArgumentException("Reference to a memory location already occupied by another variable");
	}

	public int lookupAddress ( String s ) {
    //Restituisce l'indirizzo di memoria associato a una variabile
            if( OffsetMap.containsKey(s) )
                return OffsetMap.get(s);
            else
                return -1;
	}
}
