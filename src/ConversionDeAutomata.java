import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class ConversionDeAutomata 
{
	protected String estadoInicial ;
	protected String estadoAceptacion;	
	protected String descripcionLenguaje;
	protected String alfabeto;
	protected int    cantTransiciones; 
	protected LinkedList<Estados> listaEstadosDef;      
	protected Map<String, Estados> mapaTransicionesDef;  
	public    int numeracionEstado;
	public    LinkedList<String> listaControlEstados;
	public    LinkedList<String> listaControlEstados2;
	
	/**
	 * Constructor  
	 * @param automata1 Variable de tipo Automata
	 * @return Variable de tipo Automata
	 */	
	public Automata ConversionDeAutomata(Automata automata1)
	{ 
		listaControlEstados   = new LinkedList<String>();	
		listaControlEstados2  = new LinkedList<String>();	
		listaEstadosDef       = new LinkedList<Estados>();  
		mapaTransicionesDef   = new HashMap<String,Estados>(); 
		
		//este algoritmo convierte con y sin lambda  
		Convertir_AFNL_a_AFD(automata1);   
		
		cantTransiciones =  mapaTransicionesDef.size();
		descripcionLenguaje = automata1.descripcionLenguaje;
		alfabeto = automata1.alfabeto; 
		
		Automata AutomataAFD = new Automata(alfabeto, listaEstadosDef, mapaTransicionesDef, descripcionLenguaje, cantTransiciones);			   
		return AutomataAFD;	 
	}  

    /**
	 * Metodo que hace conversion de automatas   
	 * @param automata1 Variable de tipo Automata 
	 */	
	public void Convertir_AFNL_a_AFD(Automata automata1)
	{  
		numeracionEstado = 0; 
		char[] arrAlfabeto = automata1.alfabeto.toCharArray();	
		
		Estados estIni = new Estados(); 
	    String estadoInicial = estIni.BuscaEstadoInicial(automata1.listaEstados).getNombre();
		
	    LinkedList<String> listaInicio  = new LinkedList<String>();	
		listaInicio.add(estadoInicial);
		
		LinkedList<String> listaConjuntoEstados  = new LinkedList<String>();
		listaConjuntoEstados = cerraduraL(listaInicio, automata1.mapaTransiciones, '?');
		
		LinkedList<LinkedList<String>> listaDeListaDeEstados = new LinkedList<LinkedList<String>>();	
		listaDeListaDeEstados.add(listaConjuntoEstados); 		
		
		crearEstadoDefinitivo(listaConjuntoEstados, automata1.listaEstados);
		listaInicio.clear();   
		
		for(int n=0; n < listaDeListaDeEstados.size(); n++)
		{
			LinkedList<String> listaConjuntoEstados1  = new LinkedList<String>();
			listaConjuntoEstados1 = listaDeListaDeEstados.get(n);   
			
			for(int m=0; m < arrAlfabeto.length; m++)
			{
				listaInicio.clear();
				for(int k=0; k < listaConjuntoEstados1.size(); k++)
				{
					Iterator it = automata1.mapaTransiciones.entrySet().iterator();
					while (it.hasNext())
				  	{
					  	Map.Entry e = (Map.Entry)it.next();  
				        String mapKey = e.getKey().toString();    
				        String [] key = mapKey.split("_"); 
				               
				        if(key[0].equals(listaConjuntoEstados1.get(k)+"|"+arrAlfabeto[m]))
				        {
					          String estadoDestino = automata1.mapaTransiciones.get(mapKey).getNombre();	
					          
					          boolean existe = false;
					          for(int r=0; r < listaInicio.size(); r++)   //verifica si el estado ya existe en la lista
					          {
					        	  if(listaInicio.get(r).equals(estadoDestino))
					        	  {
					        		 existe = true; 
					        	  }
					          }
					          if(!existe)
					          {
					        	  listaInicio.add(estadoDestino);
					          }				          
				        }  
				 	}	  
				} //end for k
				
				if(!listaInicio.isEmpty())
				{
					LinkedList<String> listaConjuntoEstados2  = new LinkedList<String>();
					listaConjuntoEstados2 = cerraduraL(listaInicio, automata1.mapaTransiciones, '?');
					
					if(!crearEstadoDefinitivo(listaConjuntoEstados2, automata1.listaEstados))
					{
						listaDeListaDeEstados.add(listaConjuntoEstados2); 							 
					} 
					Estados estadoAux = new Estados();
					String estadoInicio  = ObtenerNombreEquivalente(listaConjuntoEstados1);
					String estadoDestino = ObtenerNombreEquivalente(listaConjuntoEstados2); 
					
					String claveMapNueva  = estadoInicio + arrAlfabeto[m];  
        			mapaTransicionesDef.put(claveMapNueva, estadoAux.BuscarEstado(estadoDestino, listaEstadosDef));  
				} 
			} //end for m
			
		}//end for n
		 
		
	/* Buscar el estado inicial de automata
	 * cargar la lista de inicio con el estado inicial para hacer cerradura inicial y generar el estado inicial	  
	 * grabo en la lista estados
	 * proceso la lista secuencialmente hasta el fin  (for hasta .size de la lista)
	 * 	 -leo el estado de la lista (segun indice) 
	 *   *	for(1 a n simbolos del alfabeto)
		 * 		-aplico la relacion con el primer alfabeto
		 * 			-obtengo una lista de estados de llegada
		 * 			-a esta lista, le aplico cerradura
		 * 				-obtengo otra lista de llegadas (une lista inicial con la cerradura y lo ordena) 		 
		 * 			-verifico si este nuevo lista-estado no es vacio
		 * 				-no es vacio
		 * 					- verifico si esta en la lista de lista de estados
		 * 						-si no esta, 
		 * 							-creo el nuevo estado y lo agrega a lista		 * 						
		 * 						-else, no agrega
		 * 					-crea la clave map (con el estado inicial + alfabeto)
		 * 						-verifica si esta la clave ya cargada en el mapa
		 * 							-si no existe
		 * 									-arma el mapa y lo guarda (clave +  estado nuevo)
		 * 							-else,  no hace nada
		 * 	fin-for	 
		 * 	 
		 * Graba en una lista, el conjunto (lista) de estados que conforman un nuevo estado, en paralelo, a este conjunto 
		 * se renombra como q(n) y se lo graba en la lista definitiva de estados para el nuevo automata.
		 * 
		 * Cada vez que se generan los estados nuevos y se conforma la transicion, se graba la transicion (con los nuevos 
		 * nombres de estados) en el mapa de transiciones definitivas. 
		 * 
		 * Para verificar si ya existe un estado en la lista de estados, se utiliza la lista de estados definitivas
		 *   Se conviente el nuevo conjunto de estados a String y se lo busca en la lista de estados definitiva, 
		 *   si no existe, la lista de estados (conjunto) no se graba en la lista de lista de estados.		 
		 */ 
	}   
   
//Metodos-----------------------------------------------------------------------------------------------------------------------
   
   //aplico cerradura con el con vector inicio(con cada elemento)
   //	-grabo en cada iteracion recursiva sobre el misma lista
   //	-si ya existe no graba   
   //unir vector inicial con la lista resultante (llamar metodo unir)
   //ordenar la lista      
   /**
	 *Metodo que aplica cerradura a una conjunto de estados (vectorInicio)       
	 *@param listaInicio Variable de tipo LinkedList
	 *@param mapa Variable de tipo  HashMap
	 *@param simbolo Variable de tipo char
	 *@return Variable tipo LinkedList
	 */    
   public LinkedList<String> cerraduraL(LinkedList<String> listaInicio, Map<String, Estados> mapa, char simbolo)
   { 
	   LinkedList<String> lisEstAlcanzables  = new LinkedList<String>();	 
	   
	   for(int i=0; i < listaInicio.size(); i++)
	   {
		   busquedaRecursiva(listaInicio.get(i), mapa,  simbolo, lisEstAlcanzables);		   
	   }
	   
	   for(int j=0; j < listaInicio.size(); j++)
	   {
		   VerificaGrabaEstadoAlcanzable(lisEstAlcanzables, listaInicio.get(j));
	   }
	   
	 //ordena la lista de estados alcanzables obtenidos
      Collections.sort(lisEstAlcanzables, new Comparator<String>() 
   	    {
               @Override
               public int compare(String o1, String o2) 
               {
                   return Collator.getInstance().compare(o1, o2);
               }
        });
	   
	   return lisEstAlcanzables;   
   }
   
   /**
	 *Metodo recursivo que busca los estados alcanzables desde un determinado estado inicial     
	 *@param lisEstAlcanzables Variable de tipo LinkedList
	 *@param EstadoInicial Variable de tipo String	 
	 *@param mapa Variable de tipo  HashMap
	 *@param simbolo Variable de tipo char
	 */   
   
   public void busquedaRecursiva(String EstadoInicial, Map<String, Estados> mapa, char simbolo, LinkedList<String> lisEstAlcanzables)
   {
	   Iterator it = mapa.entrySet().iterator();
	   while (it.hasNext())
  		{
  			Map.Entry e = (Map.Entry)it.next();  
            String mapKey = e.getKey().toString();    
            String [] k = mapKey.split("_"); 
               
            if(k[0].equals(EstadoInicial+"|"+simbolo))
            {
            	String estadoAlcanzable = mapa.get(mapKey).getNombre();
            	VerificaGrabaEstadoAlcanzable(lisEstAlcanzables, estadoAlcanzable);
            	busquedaRecursiva(estadoAlcanzable, mapa,  simbolo, lisEstAlcanzables);
            }  
  		}	 
   }
   
   /**
	 *Metodo que guarda los estados alcanzables desde un determinado estado     
	 *@param lisEstAlcanzables Variable de tipo LinkedList
	 *@param estadoAlcanzable Variable de tipo String	 
	 */   
   public void VerificaGrabaEstadoAlcanzable(LinkedList<String> lisEstAlcanzables, String estadoAlcanzable)
   {
	   boolean existe = false;
	   for(int i=0; i < lisEstAlcanzables.size(); i++)
	   {
		   if(lisEstAlcanzables.get(i).equals(estadoAlcanzable))
		   {
			   existe = true;
			   i = lisEstAlcanzables.size();
		   }		   
	   } 
	   if(!existe)
	   {
		   lisEstAlcanzables.add(estadoAlcanzable);
	   }
   }
    
   /**
  	 *Metodo que actualiza la lista de estados definitiva 
  	 *@param listaConjuntoEstados Variable de tipo LinkedList
  	 *@param listaEstadosOriginales Variable de tipo LinkedList
  	 *@return Variable de tipo boolean 
  	 */   
   public boolean crearEstadoDefinitivo(LinkedList<String> listaConjuntoEstados, LinkedList<Estados> listaEstadosOriginales)
   {   
	  String nombreEstado = "q"+numeracionEstado;
	  Estados estadoAux   = new Estados();
	  boolean aceptacion  = false;   
	  boolean inicio      = false;  
	  String nombreAux    = "";
	  boolean existe      = false;
	  
	  if(!listaConjuntoEstados.isEmpty())
	  {
		  for(int i=0; i < listaConjuntoEstados.size(); i++)
		  {
			  estadoAux = estadoAux.BuscarEstado(listaConjuntoEstados.get(i), listaEstadosOriginales);
			  
			  if( estadoAux.isEstadoAceptacion() )
			  { 
				  aceptacion = true; 
			  } 
			  if( estadoAux.isEstadoInicial())
			  { 
				  inicio = true; 
			  }  
			  
			  nombreAux = nombreAux+listaConjuntoEstados.get(i);
		  }
	  } 
	  else
	  {
		  System.out.println("ListaControlEstados esta vacia");
	  } 
	  
	  if(!listaControlEstados.isEmpty())
	  {
		  for(int j=0; j < listaControlEstados.size(); j++)
		  {
			  if(listaControlEstados.get(j).equals(nombreAux))
			  {
				  existe = true;
				  j=listaControlEstados.size();
			  }
		  }
	  } 
	  
	  if(!existe)
	  {
		  Estados estado = new Estados(nombreEstado, inicio, aceptacion);
		  listaEstadosDef.add(estado); 
		  listaControlEstados.add(nombreAux);
		  listaControlEstados2.add(nombreEstado);
		  numeracionEstado++;
	  }
	  
	return existe;   //false = graba 
   }
    
   
   /**
 	 *Metodo que desde un conjunto de estados obtiene su nombre equivalente 
 	 *@param listaConjuntoEstados Variable de tipo LinkedList 
 	 *@return Variable de tipo String
 	 */   
  public String ObtenerNombreEquivalente(LinkedList<String> listaConjuntoEstados)
  {    
	  String nombreAux         = "";
	  String nombreEquivalente = "";
	  boolean existe= false;
	  
	  for(int i=0; i < listaConjuntoEstados.size(); i++)
	  { 
		  nombreAux = nombreAux+listaConjuntoEstados.get(i);
	  }
	  
	  for(int j=0; j < listaControlEstados.size(); j++)
	  {
		  if(listaControlEstados.get(j).equals(nombreAux))
		  {
			  nombreEquivalente = listaControlEstados2.get(j);
			  existe = true;
			  j=listaControlEstados.size();
		  }
	  } 
	  
	  if(!existe)
	  {
		  System.out.println("Error - no se ha encontrado nombre equivalente");
	  } 
	return nombreEquivalente; 
  }   
    
    
   /**
 	 *Metodo que recibe una lista, la ordena y la graba en un vector     
 	 *@param lisEstAlcanzables Variable de tipo LinkedList
 	 *@return Variable de tipo String
     */  
   public String[] ordenaGeneraVector(LinkedList<String> lisEstAlcanzables)
   { 
	  Collections.sort(lisEstAlcanzables, new Comparator<String>() 
	    {
	       @Override
	        public int compare(String o1, String o2) 
	        {
	        return Collator.getInstance().compare(o1, o2);
	        }
	    });
	  
	  String[] vectorEstAlcanzables = new String[lisEstAlcanzables.size()];    
	  for(int i=0; i < lisEstAlcanzables.size(); i++)
	  {
		  vectorEstAlcanzables[i] = lisEstAlcanzables.get(i);
	  } 
	   return vectorEstAlcanzables;    
   }
   
   
}
