import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConversionERaAutomata 
{
	protected String estadoInicial ;
	protected String estadoAceptacion;	
	protected String descripcionLenguaje;
	protected String alfabeto;
	protected int    cantTransiciones; 
	protected LinkedList<Estados> listaEstados;      
	protected Map<String, Estados> mapaTransiciones;  
	public    int numeradorNombre = 0;  //para el nombre de los estados
	public    int numerador       = 0;  //para las transiciones afn
	public    int numeradorE      = 0;  //para las transiciones lambda
	
	
	
	/**
	 * Convierte una Expresion Regular en un AFND	 
	 * @param ER Variable de tipo String
	 * @return Variable de tipo Automata
	 */
	public Automata ConversionER(String ER)
	{  
		//obtener el alfabeto 
		//armar metodo que separe las cadenas 
		//condici�n base de la recursividad: fin de la expresion
		//Arma automatas basicos de izquierda a derecha		
		//en cada retorno de la recursividad aplicar la operacion que corresponda (*, |, .)		
		//armar todas las combinaciones entre simbolos, operadores y separadores
		
		alfabeto = ObtenerAlfabeto(ER); 
		descripcionLenguaje = "Conversion de ER a Automata";
		  
		return armaAutomata(ER);  
	}  
	
	
	/**
	 * M�todo recursivo que genera el automata a partir de una Expresi�n Regular	 
	 * @param ER Variable de tipo String
	 * @return Variable de tipo Automata
	 */	
	 public Automata armaAutomata(String ER)	
	 {
		 char[] er = ER.toCharArray();  
		 
		 if(ER.length()==1)  //==1 siempre tiene que ser un simbolo
		 {
			 return CrearAutomataSimbolo(er[0]);
		 }
		 
		 if(ER.length()==2)
		 {
			 Pattern patron  = Pattern.compile("[|*()]");	 
		     Matcher valida0  = patron.matcher(String.valueOf(er[0])); 
		     Matcher valida1  = patron.matcher(String.valueOf(er[1]));   
		     if(!valida0.find() &&  er[1]=='*')    //simbolo, *    a*
		     { 				  
			    return EstrellaAutomataAFN(CrearAutomataSimbolo(er[0])); 
			 }
		     
		     if(!valida0.find() &&  !valida1.find())    //simbolo, simbolo    ab
		     { 				  
			    return ConcatenacionAutomataAFN(CrearAutomataSimbolo(er[0]),CrearAutomataSimbolo(er[0])); 
			 } 
		 } 
			 
		 if(ER.length()>=3)	 
		 {  
			 Pattern patron  = Pattern.compile("[|*()]");	 
		     Matcher valida0  = patron.matcher(String.valueOf(er[0])); 
		     Matcher valida1  = patron.matcher(String.valueOf(er[1])); 
		     Matcher valida2  = patron.matcher(String.valueOf(er[2]));
		     
		     if(!valida0.find())  //primer caracter es simbolo
		     {
		    	 if (er[1]=='*' && er[2]=='|')   //simbolo, * , uni�n
			     {   
			         String subER = dividirER(er, 3); 
			    	 return UnionAutomataAFN(EstrellaAutomataAFN(CrearAutomataSimbolo(er[0])), armaAutomata(subER)); 
			     }
			      
			     if (er[1]=='*' && (er[2]=='(' || !valida2.find()))   //simbolo, * , ( � simbolo
			     {   
			         String subER = dividirER(er, 2); 
			    	 return ConcatenacionAutomataAFN(EstrellaAutomataAFN(CrearAutomataSimbolo(er[0])), armaAutomata(subER)); 
			     } 	 	  
			    
			     if (er[1]=='|')   //simbolo, |
			     {   
			         String subER = dividirER(er, 2); 
			    	 return UnionAutomataAFN(CrearAutomataSimbolo(er[0]), armaAutomata(subER)); 
			     } 
			     
			     if(!valida1.find() || er[1]=='(')
			     {
			    	 String subER = dividirER(er, 1); 
			    	 return ConcatenacionAutomataAFN(CrearAutomataSimbolo(er[0]), armaAutomata(subER));  
			     }  
		     }
		     else  //si el primer caracter es un '('
		     { 
		    	 String [] subCadena = AnalizaParentesis(ER); 
		    	 
		    	 patron  = Pattern.compile("[|*()]");	 
		    	 if(subCadena[1].length()>0)
		    	 {
		    		 valida0 = patron.matcher(String.valueOf(subCadena[1].substring(0,1)));  //primera posicion de la cadena 2   //no se esta usando
		    		 if(subCadena[1].length()>1)
		    		 {
		    			 valida1 = patron.matcher(String.valueOf(subCadena[1].substring(1,1)));  //segunda posicion de la cadena 2		    			 
		    		 } 
		    	 }			     
		    	 
		    	 if(subCadena[1].length()==0)
		    	 {
		    		 String subER1 = subCadena[0].substring(1,subCadena[0].length()-1); //la cadena que esta entre parentesis, pero sin los parentesis de los extremos		    		 
		    		 return armaAutomata(subER1); 
		    	 }
		    	 else
		    	 {  
		    		 if(subCadena[1].substring(0,1).equals("|"))     //(......) |
			    	 {
			    		 String subER1 = subCadena[0].substring(1,subCadena[0].length()-1); //la cadena que esta entre parentesis, pero sin los parentesis de los extremos
			    		 String subER2 = subCadena[1].substring(1,subCadena[1].length());   //no incluye el '|', cadenas a partir del | en adelante hasta el final
			    		 return UnionAutomataAFN(armaAutomata(subER1), armaAutomata(subER2));      //recursiva en ambos lados
			    	 }
		    		 
		    		 if(subCadena[1].substring(0,1).equals("("))     //(......)(
			    	 {
			    		 String subER1 = subCadena[0].substring(1,subCadena[0].length()-1); //la cadena que esta entre parentesis, pero sin los parentesis de los extremos
			    		 String subER2 = subCadena[1];   // toda la subcadena 2
			    		 return ConcatenacionAutomataAFN(armaAutomata(subER1), armaAutomata(subER2));      //recursiva en ambos lados
			    	 }
		    		 
			    	 
		    		 if(subCadena[1].length()>1)
		    		 {
		    			 if(subCadena[1].substring(0,1).equals("*") &&  subCadena[1].substring(1,1).equals("|")  )     //(......)*|
				    	 {
				    		 String subER1 = subCadena[0].substring(1,subCadena[0].length()-1); //la cadena que esta entre parentesis, pero sin los parentesis de los extremos
				    		 String subER2 = subCadena[1].substring(2,subCadena[1].length());   //no incluye el '|', cadenas a partir del | en adelante hasta el final
				    		 return UnionAutomataAFN(EstrellaAutomataAFN(armaAutomata(subER1)), armaAutomata(subER2));  //recursiva en ambos lados
				    	 }
				    	 
				         //(...), * , ( � simbolo 
				    	 if(subCadena[1].substring(0,1).equals("*") &&  (subCadena[1].substring(1,1).equals("(")  || !valida1.find()))     //(......)*(  //(...)*a
				    	 { 
				    		 String subER1 = subCadena[0].substring(1,subCadena[0].length()-1); //la cadena que esta entre parentesis, pero sin los parentesis de los extremos
				    		 String subER2 = subCadena[1].substring(1,subCadena[1].length());   //contine la cadena con (...) de la segunda parte		    		  
				    		 return ConcatenacionAutomataAFN(EstrellaAutomataAFN(armaAutomata(subER1)), armaAutomata(subER2));  //recursiva en ambos lados		    		 
				    	 }  
		    		 } 
			    	 
			    	 if(subCadena[1].substring(0,1).equals("*") &&  subCadena[1].length()==1)     //(......)*     -> * es el ultimo caracter de la expresion
				     {
				    	 String subER1 = subCadena[0].substring(1,subCadena[0].length()-1); //la cadena que esta entre parentesis, pero sin los parentesis de los extremos			    		 		    		  
				    	 return EstrellaAutomataAFN(armaAutomata(subER1));   //aplica estrella a la ultima parte		    		 
				     }
			    	 
			    	 if(!valida0.find())     //(......)simbolo
			    	 {
			    		 String subER1 = subCadena[0].substring(1,subCadena[0].length()-1); //la cadena que esta entre parentesis, pero sin los parentesis de los extremos
			    		 String subER2 = subCadena[1].substring(0,subCadena[1].length());   //la segunda cadena comienza con un simbolo	    		  
			    		 return ConcatenacionAutomataAFN(armaAutomata(subER1), armaAutomata(subER2));  //recursiva en ambos lados		    		 
			    	 }  
		    	 } 
		     } 
		 } 
	     return null; //devuelve un automata		 
	 }
	
	 

	/**
	 * Elimina de la Expresion Regular todos los operadores y separadores, dejando solo los simbolos	 
	 * @param expresion Variable de tipo String
	 * @return Variable de tipo String
	 */	
	 public String limpiarER(String expresion)
	    {
	    	String REGEX   = "[()|*+.]";
	    	String REPLACE = "";
	    	Pattern p = Pattern.compile(REGEX);         
	        Matcher m = p.matcher(expresion);
	        expresion = m.replaceAll(REPLACE);    	    
	        return expresion;
	        
	        //referencia de regex:
	        //https://docs.oracle.com/javase/tutorial/essential/regex/matcher.html
	    }	 
	
	/**
	 * Obtiene el alfabeto desde la cadena de ER limpia(sin operadores ni separadores)
	 * (elimina los simbolos repetidos)	 
	 * @param expresion Variable de tipo String
	 * @return Variable de tipo String
	 */
	public String ObtenerAlfabeto(String expresion)
    {
		expresion = limpiarER(expresion);
    	char[] cadena = expresion.toCharArray();
    	LinkedList<String> lCadena = new LinkedList<String>();  
    	
    	for(int i=0; i < cadena.length; i++)
    	{
    		boolean existe = false;
    		for(int j=0; j < lCadena.size(); j++)
    		{    			
    			String c = String.valueOf(cadena[i]);
    			if (lCadena.get(j).equals(c))
    			{
    				existe = true;
    				j= lCadena.size();
    			} 
    		}
    		if(!existe)
    		{
    			lCadena.add(String.valueOf(cadena[i]));
    		}
    	}    	
    	
    	String alfabeto = "";
    	for(int k=0; k < lCadena.size(); k++)
    	{
    		alfabeto = alfabeto+lCadena.get(k);
    	}
    	 
    	return alfabeto;    	
    }	
	
	
	/**
	 * Union de dos automatas y retorna un automata AFN 
	 * @param aut1 Variable de tipo Automata
	 * @param aut2 Variable de tipo Automata
	 * @return Variable de tipo Automata
	 */
	public Automata UnionAutomataAFN(Automata aut1, Automata aut2)
    {
		LinkedList<Estados>     NuevaListaEstados     = new LinkedList<Estados>();	  //para unificar las listas de estados
		HashMap<String,Estados> NuevoMapaTransiciones = new HashMap<String,Estados>(); 
		
		Estados estadoNuevo = new Estados();
				
		String NuevoNombreEstado ="";  //nuevo estado inicial, su nombre es la concatenacion de los nombres de los estados iniciales de cada automata  		 
		NuevoNombreEstado = estadoNuevo.BuscaEstadoInicial(aut1.listaEstados).getNombre();
		NuevoNombreEstado = NuevoNombreEstado + estadoNuevo.BuscaEstadoInicial(aut2.listaEstados).getNombre();
		
		//Set al Nuevo estado inicial
		estadoNuevo.setNombre(NuevoNombreEstado);
		estadoNuevo.setEstadoAceptacion(false);
		estadoNuevo.setEstadoInicial(true);
		
		NuevaListaEstados.add(estadoNuevo); 
		
		String claveMapNueva  = NuevoNombreEstado + "|" + "?"+ "_" + numeradorE;
		numeradorE++;
		NuevoMapaTransiciones.put(claveMapNueva, estadoNuevo.BuscaEstadoInicial(aut1.listaEstados));  //nueva transici�n del inicial nuevo al inicial aut1
		
		
		claveMapNueva  = NuevoNombreEstado + "|" + "?"+ "_" + numeradorE;
		numeradorE++;
		NuevoMapaTransiciones.put(claveMapNueva, estadoNuevo.BuscaEstadoInicial(aut2.listaEstados));  //nueva transici�n del inicial nuevo al inicial aut2
		
		//Los estados iniciales de cada automata original dejan de ser iniciales
		estadoNuevo.BuscaEstadoInicial(aut1.listaEstados).setEstadoAceptacion(false);
		estadoNuevo.BuscaEstadoInicial(aut2.listaEstados).setEstadoAceptacion(false);
				
		//Unificaci�n de las listas de estados
		for(int i=0; i < aut1.listaEstados.size(); i++)  //carga la lista de estados del automata 1 en la nueva lista de estados
		{
			NuevaListaEstados.add(aut1.listaEstados.get(i));
		}
		
		for(int i=0; i < aut2.listaEstados.size(); i++)  //carga la lista de estados del automata 2 en la nueva lista de estados 
		{
			NuevaListaEstados.add(aut2.listaEstados.get(i));
		}
		
		//Unificar las transiciones en el nuevo mapa de transiciones
		Iterator it1 = aut1.mapaTransiciones.entrySet().iterator();
		while (it1.hasNext())
	  	{
		  	Map.Entry e = (Map.Entry)it1.next();   
			NuevoMapaTransiciones.put((String)e.getKey(), (Estados)e.getValue()); 
	  	}
		
		Iterator it2 = aut2.mapaTransiciones.entrySet().iterator();
		while (it2.hasNext())
	  	{
		  	Map.Entry e = (Map.Entry)it2.next();   
			NuevoMapaTransiciones.put((String)e.getKey(), (Estados)e.getValue()); 
	  	}
		  
       //String descripcion = "Union - "+aut1.descripcionLenguaje+" - "+aut2.descripcionLenguaje;
       int cantidadTr = NuevoMapaTransiciones.size();
		
	   Automata NuevoAutomataAFN = new Automata(alfabeto, NuevaListaEstados, NuevoMapaTransiciones, descripcionLenguaje, cantidadTr);	 
		
       return NuevoAutomataAFN;  //debe retornar el automata resultante AFN    	
    }		
	
	
	/**
	 * Concatenacion de dos automatas y retorna un automata AFN 
	 * @param aut1 Variable de tipo Automata
	 * @param aut2 Variable de tipo Automata
	 * @return Variable de tipo Automata
	 */
	public Automata ConcatenacionAutomataAFN(Automata aut1, Automata aut2)
    {
    	 
		LinkedList<Estados>     NuevaListaEstados     = new LinkedList<Estados>();	  //para unificar las listas de estados
		HashMap<String,Estados> NuevoMapaTransiciones = new HashMap<String,Estados>(); 
		
		Estados estadoAux = new Estados();	 
		
		for(int i=0; i < aut1.listaEstados.size(); i++)
		{
			if(estadoAux.VerificaEstadoAceptacion(aut1.listaEstados, aut1.listaEstados.get(i)))
			{
				String claveMapNueva  = aut1.listaEstados.get(i).getNombre() + "|" + "?"+ "_" + numeradorE;
				numeradorE++; 
				
				NuevoMapaTransiciones.put(claveMapNueva, estadoAux.BuscaEstadoInicial(aut2.listaEstados));	//crea el enlace con el automata 2			
				aut1.listaEstados.get(i).setEstadoAceptacion(false); //desmarca los estados de aceptacion del automata 1 (dejan de ser aceptacion)
			}
		}
		
		
		//desmarca el estado inicial del automata 2 (deja de ser inicial)
		estadoAux.BuscaEstadoInicial(aut2.listaEstados).setEstadoInicial(false);
		 		
		//Unificaci�n de las listas de estados
		for(int i=0; i < aut1.listaEstados.size(); i++)  //carga la lista de estados del automata 1 en la nueva lista de estados
		{
			NuevaListaEstados.add(aut1.listaEstados.get(i));
		}
		
		for(int j=0; j < aut2.listaEstados.size(); j++)  //carga la lista de estados del automata 2 en la nueva lista de estados 
		{
			NuevaListaEstados.add(aut2.listaEstados.get(j));
		}
		
		//Unificar las transiciones en el nuevo mapa de transiciones
		Iterator it1 = aut1.mapaTransiciones.entrySet().iterator();
		while (it1.hasNext())
	  	{
		  	Map.Entry e = (Map.Entry)it1.next();   
			NuevoMapaTransiciones.put((String)e.getKey(), (Estados)e.getValue());  
	  	}
		
		Iterator it2 = aut2.mapaTransiciones.entrySet().iterator();
		while (it2.hasNext())
	  	{
		  	Map.Entry e2 = (Map.Entry)it2.next();   
			NuevoMapaTransiciones.put((String)e2.getKey(), (Estados)e2.getValue());  
	  	} 
       
       int cantidadTr = NuevoMapaTransiciones.size();
		
	   Automata NuevoAutomataAFN = new Automata(alfabeto, NuevaListaEstados, NuevoMapaTransiciones, descripcionLenguaje, cantidadTr);	  
	    
       return NuevoAutomataAFN;  //retorna un nuevo automata (automata 1 concatenado con automata 2) 	
    }		
	
	
	/**
	 * Aplica Estrella (*) al automata de entrada y retorna el mismo automata AFN con nuevas transiciones * 
	 * @param aut1 Variable de tipo Automata	 
	 * @return Variable de tipo Automata
	 */
	public Automata EstrellaAutomataAFN(Automata aut1)
    {
    	Estados estadoAux = new Estados();		
		
		for(int i=0; i < aut1.listaEstados.size(); i++)
		{
			if(estadoAux.VerificaEstadoAceptacion(aut1.listaEstados, aut1.listaEstados.get(i)))
			{ 
				//Formato: (E|S_n)  ejemplo: (q0|a_1) 
				String claveMapNueva  = aut1.listaEstados.get(i).getNombre() + "|" + "?"+ "_" + numeradorE;
				numeradorE++;
				aut1.mapaTransiciones.put(claveMapNueva, estadoAux.BuscaEstadoInicial(aut1.listaEstados)); 
			}
		}
		
		estadoAux.BuscaEstadoInicial(aut1.listaEstados).setEstadoAceptacion(true); 
		
    	return aut1;  //retorna el mismo automata con la lista de transiciones actualizada (con la estrella de klenne, con lo cual, el aut1 se conviente en AFN)    	
    }			
	
	
	/**
	 * Crea el automata b�sico, el de la hoja del arbol (2 estados, uno inicial,
	 * uno de aceptacion y una transici�n con el simbolo)
	 * 
	 * @param simbolo
	 *            Variable de tipo String
	 * @return Variable de tipo Automata
	 */
	public Automata CrearAutomataSimbolo(char simbolo)
    {
    	//debe crear automatas con estados identificables univocamente
		 
		LinkedList<Estados>     NuevaListaEstados     = new LinkedList<Estados>();	  //para unificar las listas de estados
		HashMap<String,Estados> NuevoMapaTransiciones = new HashMap<String,Estados>(); 
				 
		Estados estado0 = new Estados("q"+numeradorNombre, true, false);	
		numeradorNombre++;
		Estados estado1 = new Estados("q"+numeradorNombre, false, true);
		numeradorNombre++;
		
		NuevaListaEstados.add(estado0);
		NuevaListaEstados.add(estado1);
		 
		//Formato: (E|S_n)  ejemplo: (q0|a_1) 
		String clave1  = estado0.getNombre() + "|" + simbolo + "_" + numerador; 
		numerador++;
		//String clave1  = estado0.getNombre() + simbolo;  
		NuevoMapaTransiciones.put(clave1, estado1);  
		
		//String descripcion = "Aut "+ simbolo + " - " + estado0.getNombre() + " - " + estado1.getNombre(); 
		int cantidadTr = NuevoMapaTransiciones.size();
		
		Automata automataInicial = new Automata(alfabeto, NuevaListaEstados, NuevoMapaTransiciones,descripcionLenguaje, cantidadTr);	  
    	return automataInicial;  //debe retornar el automata de la hoja - minima expresion    	
    }		 
	
	 
	/**
	 * Subdivide la ER desde una posicion indicada para su posterior an�lisis  
	 * @param er Variable de tipo char[]
	 * @param posDesde Variable de tipo int	 
	 * @return Variable de tipo String
	 */
	public String dividirER(char[] er, int posDesde)
	{
		String subER = "";
		for(int i=posDesde; i < er.length; i++)
		{
			subER = subER + er[i];
		} 
		return subER; 
	}
	
	/**
	 * Metodo que recibe un mapa de transiciones y un simbolo y retorna la secuencia del simbolo
	 * @param mapTransiciones de tipo HashMap
	 * @param clave de tipo String
	 * @return Variable de tipo int
	 */     
	    public int BuscaNumeradorAFN(HashMap<String, Estados> mapTransiciones, String clave)
	    {
	    	int numerador    = 0; 
	    	Iterator it = mapTransiciones.entrySet().iterator();
	    	
	    	if(!mapTransiciones.isEmpty())
	    	{
	    		while (it.hasNext())
	    		{
	    			Map.Entry e = (Map.Entry)it.next();  
	                String mapKey = e.getKey().toString();    
	                String [] k = mapKey.split("_"); 
	                
	    	        if(clave.equals(k[0]))
	    	        {
	    	        	int num = Integer.parseInt(k[1]);
	    	        	if(numerador <= num)
	    		        {
	    		        	numerador = num+1;
	    		        }
	    	        } 
	    		} 
	    	} 
	       	return numerador;    
	    }    
	    
	    /**
		 * Metodo que analiza las cadenas que se encuentran entre parentesis
		 * Recibe una cadena y retorna la primera subcadena que esta entre parentesis equilibrados, y el resto de la cadena
		 * @param expresion Variable de tipo String 
		 * @return Variable de tipo String
		 */     
		    public String [] AnalizaParentesis(String expresion)
		    {	//se considera que la expresion tiene parentesis equilibrados   
		    	//la cadena que recibe comienza con '('
		    	String [] cadena = new String[2];
		    	int control = 0;
		    	
		    	for(int i=0; i < expresion.length(); i++)
		    	{
		    		if(expresion.charAt(i) == '(')
		    		{
		    			control++;
		    		}
		    		else
		    		{
		    			if(expresion.charAt(i) == ')')
		    			{
		    				control--;
		    			}
		    		}
		    		if(control == 0)
		    		{ 
		    			String sCadena = expresion;
		    			cadena[0] = sCadena.substring(0,i+1);
		    			cadena[1] = sCadena.substring(i+1, (expresion.length()));
		    			i = expresion.length();
		    		} 
		    	}
		    	
		    	if(control<0 || control>0)
		    	{
		    		System.out.println("Error en la cadena - los parentesis no estan equilibrados");
		    	} 
		    	
		    	return cadena;
		    } 
	    
}
