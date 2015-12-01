

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader; 
import java.text.Collator;
import java.util.*;  
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import java.util.Iterator.*;
/**
 * 
 * @author Mauricio Nishida y  Carlos Charris - 
 * 
 * Proyecto de aula - Autómatas Gramáticas y Lenguajes
 * @version Por consola
 * 
 */
public class AFD 
{
    public String descripcionLenguaje;
    public String alfabeto;
	public int    cantTransiciones;
    public int    cantCadenas;      
    public String ER;
    
    public LinkedList <String>   listaParametros;      //Para la interface grafica
    public LinkedList <String>   listaSalidas;         //Para la interface grafica  
    public LinkedList <String>   listaCadenas;         //Proceso interno y para interface grafica    
    public LinkedList<Estados>   listaEstados;      
    public LinkedList<String>    listaTransiciones;    
    public Map<String, Estados>  mapaTransiciones;  
    public LinkedList<Automata>  listaAutomatas;  
    public MinimizarAutomata     minAuto;
    public UnionInterseccion     unionInterAuto;   
    public ConversionDeAutomata  converAuto;
    public ConversionERaAutomata converER;
    
    public int LineaHasta;  
 
/**
 * Constructor de la clase
 */  	 
    public AFD()
    {
      Inicializar();    
    } 
    
/**
 * Metodo que inicializa las variables de la clase (inicializa el constructor)
 */    
    public void Inicializar()
	{  
       this.descripcionLenguaje   = null;
 	   this.alfabeto              = null;
 	   this.cantTransiciones      = 0;
 	   this.cantCadenas           = 0;    
 	   this.ER                    = null;
 	   this.listaParametros       = new LinkedList<String>();	  
 	   this.listaSalidas          = new LinkedList<String>();	
 	   this.listaCadenas          = new LinkedList<String>();	  
       this.listaEstados          = new LinkedList<Estados>();	
       this.listaTransiciones     = new LinkedList<String>();	
 	   this.LineaHasta            = 0;
 	   this.mapaTransiciones      = new HashMap<String,Estados>();
 	   this.listaAutomatas        = new LinkedList<Automata>();
 	   this.minAuto               = new MinimizarAutomata();
 	   this.unionInterAuto        = new UnionInterseccion(); 
 	   this.converAuto            = new ConversionDeAutomata();
 	   this.converER              = new ConversionERaAutomata();
	} 
    
/**
 * Metodo que carga y lee el archivo de entrada
 * @return Variable de tipo boolean
 */    
    public boolean CargarArchivo()
    {
    	File archivo = new File("./DATA/automata.txt"); 
    	BufferedReader in = null;
    	int contadorLinea =1;    	 
    	
    	try 
    	{
		  in = new BufferedReader(new FileReader(archivo));
		  String inputLine; 
				
		  LinkedList<Estados>      lisEst   = new LinkedList<Estados>();	
		  LinkedList<String>       lisTran  = new LinkedList<String>();	
		  HashMap<String, Estados> mapTran  = new HashMap<String,Estados>();
		  
		  while ((inputLine = in.readLine()) != null) 
		  {
			  GrabarCadaLinea(contadorLinea, inputLine, lisEst, mapTran, lisTran);	
			  contadorLinea++;
		  } 
		  
		  mapTran = GrabaTransicionesAFN(lisTran, lisEst, alfabeto); 
		  listaParametros.add("----------------------------------------------\n");  //separador de parametros	 
		  
		  //genera el automata completo
		  Automata automata = new Automata(alfabeto, lisEst, mapTran, descripcionLenguaje, cantTransiciones);	 
		  listaAutomatas.add(automata);		 
		  
		  return true;
    	}
 	    catch (Exception e) 
 	    {
			e.printStackTrace();
			return false;
		} 
    	finally
    	{
    		try 
    		{
				if (in != null)
				{
					in.close();
					return true;
				}
				else
				{
					String mensaje ="El archivo esta vacio...";
					System.out.println(mensaje);
					return false;
				}					
			} 
    		catch (Exception e2) 
    		{
				e2.printStackTrace();
			}
		} 
    }
     
/**
 * Metodo que Graba cada línea leída del archivo en las diferentes listas y variables
 * @param Linea Variable de tipo int
 * @param inputLine Variable de tipo String
 * @param lisEst Variable de tipo LinkedList 
 * @param mapTran Variable de tipo HashMap 
 * @param lisTran Variable de tipo LinkedList 
 */     
    public void GrabarCadaLinea(int Linea, String inputLine, LinkedList<Estados> lisEst, HashMap<String, Estados> mapTran, LinkedList<String> lisTran)
    { 
    	if(Linea == 1)
    	{
    		descripcionLenguaje = inputLine;
    		listaParametros.add(descripcionLenguaje);
    	}
    	
    	if(Linea == 2)
    	{
    		alfabeto = inputLine;
    		listaParametros.add(alfabeto);
    	}
    	
    	//graba los estados en una lista de estados
    	if(Linea == 3)
    	{    	 
    		 String [] arrNombreDeEstados = inputLine.split(",");	
    		 for(int i=0; i<arrNombreDeEstados.length; i++)
    		 {
    			Estados estados = new Estados(arrNombreDeEstados[i]);
    			lisEst.add(estados);
    		 }
    		 listaParametros.add(inputLine);
    	}
    	
    	//busca el estado en la lista y lo marca como estado inicial
    	if(Linea == 4)
    	{
    		for(int j=0; j< lisEst.size();j++)		 
    		{    			
    			if(lisEst.get(j).nombre.equals(inputLine))
    			{
    				lisEst.get(j).setEstadoInicial(true);
    				j=lisEst.size();  	 
    			}
    		}
    		listaParametros.add(inputLine);
    	}
    	
    	//busca los estados y los marca como estados de aceptación
    	if(Linea == 5)
    	{
    		String [] arrEstadosAceptacion = inputLine.split(",");	
    		for(int k=0; k<arrEstadosAceptacion.length; k++)
    		{
    			for(int l=0; l< lisEst.size();l++)		 
        		{    			
        			if(lisEst.get(l).nombre.equals(arrEstadosAceptacion[k]))
        			{
        				lisEst.get(l).setEstadoAceptacion(true);
        				l=lisEst.size();
        			}
        		}
    		}
    		listaParametros.add(inputLine);
    	}
    	
    	//Graba la cantidad de transiciones
    	if(Linea == 6)
    	{    		 
    		cantTransiciones = Integer.parseInt(inputLine);
    		LineaHasta = Linea + cantTransiciones;
    		listaParametros.add(inputLine);  
    	}
    	
    	//Graba todas las transiciones en un HashMap - La llave del HasMap es Nombre del estado + el simbolo relacionado    	 
    	if(Linea >= 7 && Linea <= LineaHasta)
    	{
    		lisTran.add(inputLine);    //graba las transiciones en la lista para luego grabarlas en el HashMap    		 
    		listaParametros.add(inputLine);    	 
    	}
    	
    	//Guarda el números de cadenas que se leerán del archivo
    	if(Linea > 7 && Linea == LineaHasta+1)
    	{    		
    		cantCadenas = Integer.parseInt(inputLine);    	
    		listaParametros.add(inputLine);    		
    	}
    	
    	//Graba cada cadena en la lista de cadenas "listaCadenas"
    	if(Linea > LineaHasta+1 && Linea <=LineaHasta+1+cantCadenas)
    	{
    		listaCadenas.add(inputLine); 
    	}  
    }
    
 
/**
 * Metodo que Busca los estados en la lista de estados
 * @param nombreEstado Variable de tipo String
 * @param listaE Variable de tipo LinkedList
 * @return Variable de tipo Estados
 */        
    public Estados BuscarEstado(String nombreEstado, LinkedList<Estados> listaE )
    {
    	for(int m=0; m< listaE.size();m++)		 
		{    			
			if(listaE.get(m).nombre.equals(nombreEstado))
			{				 
				return listaE.get(m);				
			}
		}
    	return null;    
    } 
       
    /**
	 * Metodo que Analiza la cadena
	 * @param cadena Variable de tipo String
	 * @param nroCadena Variable de tipo int	
	 * @param afd Variable de tipo Automata  
	 */        
    public void AnalizaCadena(Automata afd, String cadena, int nroCadena)
    {
    	String mensaje = "Procesando cadena "+ nroCadena+": "+ cadena;
    	//System.out.println(mensaje);
    	listaSalidas.add(mensaje);
    	
    	boolean ok = true;
    	
    	char [] arrCadena = new char[cadena.length()];        	 		
		arrCadena = cadena.toCharArray();	 
    	 
    	Estados estadoActual = new Estados();
    	estadoActual = estadoActual.BuscaEstadoInicial(afd.listaEstados); 
    			
    	for(int i=0; i<arrCadena.length;i++)
    	{         		 
    		if(afd.alfabeto.contains(String.valueOf(arrCadena[i])))   //verifica que el simbolo sea del alfabeto	
    		{  
     			String claveMap1 = estadoActual.getNombre() + arrCadena[i];  
    			
    			if (afd.mapaTransiciones.containsKey(claveMap1))
    			{
    				mensaje = "("+estadoActual.getNombre()+","+arrCadena[i]+")";
    				estadoActual = afd.mapaTransiciones.get(claveMap1);
    				mensaje = mensaje + " => "+estadoActual.getNombre();
    				//System.out.println(mensaje);
    				listaSalidas.add(mensaje);
    			}
    			else
    			{
    				mensaje = "Transición no encontrada - Limbo";
    				//System.out.println(mensaje+"\n");
    				listaSalidas.add(mensaje);
    				i= arrCadena.length;
    				ok= false;
    			}     		 
    		}
    		else
    		{ 
    			i= arrCadena.length;
    			ok= false;
    		}
    	}
    	
    	if(estadoActual.VerificaEstadoAceptacion(afd.listaEstados, estadoActual) && ok)   //verifica si el ultimo estado fue de aceptación
    	{
    		mensaje = "La cadena '" + cadena + "' pertenece al lenguaje L.";
    		//System.out.println(mensaje+"\n");
    		listaSalidas.add(mensaje+"\n");
    	}
    	else
    	{
    		mensaje = "La cadena '" + cadena + "' no pertenece al lenguaje L.";
    		//System.out.println(mensaje+"\n");
    		listaSalidas.add(mensaje+"\n");
    	}  
    }  
    
    
/**
 * Metodo que recibe una lista de transiciones en formato String y lo graba en un HashMap, retorna un mapa
 * @param listaTransiciones de tipo LinkedList
 * @param listaEstados Variable de tipo LinkedList
 * @param alfabeto Variable de tipo String 
 * @return Variable de tipo Map
 */        
    public HashMap<String, Estados> GrabaTransicionesAFN(LinkedList<String> listaTransiciones, LinkedList<Estados> listaEstados, String alfabeto)
    { 
    	HashMap<String, Estados> mapTransiciones  = new HashMap<String,Estados>();
    	int numerador = 0;
    	String claveMap = "";
    	boolean esAFN = VerificaEsAFN(listaTransiciones, alfabeto);
    	
      	for(int m=0; m< listaTransiciones.size();m++)		 
      	{    			
      		String [] arrTransiciones = listaTransiciones.get(m).split(",");
      		char simbolo = arrTransiciones[1].charAt(0);    	 
      		
      		if (esAFN)
      		{
      			String clave = arrTransiciones[0]+ "|" + arrTransiciones[1];
      			numerador = BuscaNumeradorAFN(mapTransiciones, clave);
      		    //Formato: (E|S_n)  ejemplo: (q0|a_1) 
      			claveMap  = BuscarEstado(arrTransiciones[0], listaEstados).getNombre() + "|" + simbolo + "_" + numerador;      			
      		}
      		else
      		{
      			//Formato: Clave = (Estado+Simbolo)
      			claveMap  = BuscarEstado(arrTransiciones[0], listaEstados).getNombre() + simbolo;     
      		}
      		 
      		mapTransiciones.put(claveMap, BuscarEstado(arrTransiciones[2], listaEstados));  
    	}
       	return mapTransiciones;    
    }     
    
/**
 * Metodo que recibe un mapa de transiciones y un simbolo y retorna la secuencia del simbolo
 * @param mapTransiciones de tipo HashMap
 * @param clave Variable de tipo String 
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
 * Metodo que verifica si el automata es AFN-e
 * @param listaTransiciones de tipo LinkedList
 * @param alfabeto Variable de tipo String
 * @return Variable de tipo boolean
 */     
        public boolean VerificaEsAFN(LinkedList<String> listaTransiciones, String alfabeto)
        {
        	boolean esAFN = false;
        	
        	//ordena la lista de transiciones
        	Collections.sort(listaTransiciones, new Comparator<String>() 
        	    {
	                @Override
	                public int compare(String o1, String o2) 
	                {
	                    return Collator.getInstance().compare(o1, o2);
                    }
                });
        	
        	 
        	//verifica claves repetidas
        	for(int i=0; i < listaTransiciones.size()-1; i++)
        	{ 
        	  String [] arrTransiciones = listaTransiciones.get(i).split(",");	
        	  String clave1 = arrTransiciones[0]+arrTransiciones[1];	
        	  
        	  if(arrTransiciones[1].equals("?"))
        	  { 
        		  return true;
        	  } 
        	  
        	  arrTransiciones = listaTransiciones.get(i+1).split(",");	
        	  String clave2 = arrTransiciones[0]+arrTransiciones[1];
        	  
        	  if(arrTransiciones[1].equals("?"))
        	  { 
        		  return true;
        	  }  
        		
   	    	  if(clave1.equals(clave2))
   	    	  { 
   	    		  return true;
   	    	  }
        	} 
        	return esAFN;
        }            
     
/**
 * Metodo que muestra el mapa - Este metodo no es usado en el proceso, solo esta para controlar las claves guardadas    	 
 */     
    public void mostrarMap()
     {
	     Set< String > claves = mapaTransiciones.keySet(); // obtiene las claves    	    
	     TreeSet< String > clavesOrdenadas = new TreeSet< String >( claves );     // ordena las claves
	     System.out.println( "El mapa contiene:\nClave\t\tValor" );    
	     
	     for ( String clave : claves )  // genera la salida para cada clave en el mapa
	     {
		     System.out.printf( "%-10s%10s\n", clavesOrdenadas , mapaTransiciones.get(clave));    
		     System.out.printf( "\nsize:%d\nisEmpty:%b\n", mapaTransiciones.size(), mapaTransiciones.isEmpty());
	     }
     } // fin del método mostrarMap  
     
     
    
/**
 * Metodo que carga los parametros del nuevo automata en la lista de parametros a mostrar en pantalla    
 * @param automataNuevo Variable de tipo Automata  
 */
    public void CargaAutomataParaMostrar(Automata automataNuevo)
    {
    //carga en las listas de parametros  
    
    //linea 1: 
    	listaParametros.add(automataNuevo.getDescripcionLenguaje());   
    	
    //linea 2:
    	listaParametros.add(automataNuevo.getAlfabeto());		
    	
    //linea 3:
    	String linea3 = "";
    	for(int i=0; i < automataNuevo.listaEstados.size(); i++)
    	{
    		linea3 = linea3 +  automataNuevo.listaEstados.get(i).getNombre();
    		if(i != automataNuevo.listaEstados.size()-1)
    		{
    			linea3 = linea3 + ",";
    		}
    	}
    	listaParametros.add(linea3);		
    
    //linea 4:
    	Estados e = new Estados();    	 
    	String estadoInicial = e.BuscaEstadoInicial(automataNuevo.listaEstados).getNombre(); 
    	listaParametros.add(estadoInicial);	
    
    //linea 5:
    	String linea5 = "";
    	for(int i=0; i < automataNuevo.listaEstados.size(); i++)
    	{
    		if(automataNuevo.listaEstados.get(i).estadoAceptacion)
    		{
    			linea5 = linea5 +  automataNuevo.listaEstados.get(i).getNombre();
        		if(i != automataNuevo.listaEstados.size()-1)
        		{
        			linea5 = linea5 + ",";
        		}
    		}    		
    	}
    	listaParametros.add(linea5);	
    	
    //linea 6: 
    	String cantTransiciones = String.valueOf(automataNuevo.getCantTransiciones());
    	listaParametros.add(cantTransiciones);
    	
    //linea 7:    	 
    	boolean esAFN = VerificaEsAFN(automataNuevo);  
    	
    	Iterator<Entry<String, Estados>> it = automataNuevo.mapaTransiciones.entrySet().iterator();
    	String linea7 = "";
		while (it.hasNext())
		{			
			Map.Entry m          = (Map.Entry)it.next(); 	     //CONTROLAR ESTA WARNING  
            String clave         = m.getKey().toString();    
            
            if (!esAFN)
            {
            	char[] c             = clave.toCharArray();;
    	        String estadoOrigen  = "";
    	        char simbolo         = c[c.length-1];
    	        
    	        for(int k=0; k < c.length-1; k++)
    	        {
    	        	estadoOrigen+=c[k];
    	        }
    			    	        	
    	       	Estados valorMapa = (Estados) m.getValue();
    	       	String estadoDestino = valorMapa.getNombre();
    	        	
    	       	linea7 = estadoOrigen + "," + simbolo + "," + estadoDestino;    
            }
            else
            { 
            	String []  t         = clave.split("_");  
            	char[] c             = t[0].toCharArray();; 
            	String estadoOrigen = "";
            	char simbolo         = c[c.length-1];
            	
            	for(int i=0; i < c.length-2; i++)
     	        {
     	        	estadoOrigen+=c[i];
     	        } 
            	
    	       	Estados valorMapa   = (Estados) m.getValue();
    	       	String estadoDestino = valorMapa.getNombre();
    	        	
    	       	linea7 = estadoOrigen + "," + simbolo + "," + estadoDestino;    
            } 
    		listaParametros.add(linea7);	 
		}   
    }    
     
    
    /**
     * Metodo que verifica si el automata es un AFN     
     * @param automata Variable de tipo Automata 
     * @return Variable de tipo boolean
     */
        public boolean VerificaEsAFN(Automata automata)
        {
        	boolean esAFN = false;
        	Iterator<Entry<String, Estados>> it = automata.mapaTransiciones.entrySet().iterator();
        	while (it.hasNext())
    		{			
    			Map.Entry m      = (Map.Entry)it.next(); 	      
                String clave     = m.getKey().toString();     
    	        Pattern patron   = Pattern.compile("[|_?]");
			    Matcher valida   = patron.matcher(clave);
			    if (valida.find())
			    {  
			       //esAFN = true;
			       return true;
			    } 
    		}
        	return esAFN;        	
        }    
        

    /**
	 * Verifica si los parentesis de la ER esten equilibrados
	 * Verifica si existen operadores iguales en forma consecutiva (**, ||)
	 * Verifica si existen espacios en blanco entre la expresion
	 * Verifica si en la primera posicion hay un '*' o un '|'               
	 * Verifica si en la ultima posicion hay un '|'   
	 * Verifica si existe la combinacion '|*'                                
	 * Retorna un mensaje de error si detecta un problema, caso contrario, retorna null
	 * @param expresion Variable de tipo String
	 * @return variable de tipo String
	 */		
	public String validadorExpresionRegular (String expresion)  
	{ 	  
		String mensajeError = "SINERROR"; 
		
		if(expresion.length() == 0)
		{
			mensajeError = "Ha ingresado una expresión vacía";
		}
		else
		{ 
			String REGEX   = "[()|*+.]";
	    	String REPLACE = "";
	    	Pattern p = Pattern.compile(REGEX);         
	        Matcher m = p.matcher(expresion);
	        String expresion2 = m.replaceAll(REPLACE);  
			
			if(expresion2.length()==0)
			{
				mensajeError = "No ha ingresado ningún simbolo";
			}
			else
			{ 
				 //verifica inicio de la cadena
			     Pattern patron = Pattern.compile("[|*+.]");
			     Matcher validaInicio  = patron.matcher(String.valueOf(expresion.charAt(0)));
			     if (validaInicio.find())
			     {  
			    	  mensajeError = "La expresion no pude comenzar con un operador";
			     } 
			     else
			     {
			    	//verifica fin de la cadena
				     Pattern patron2 = Pattern.compile("[|+.]");
				     Matcher validaFin  = patron2.matcher(String.valueOf(expresion.charAt(expresion.length()-1)));
				     if (validaFin.find())
				     {  
				    	  mensajeError = "La expresion no pude finalizar con un operador";
				     }  
				     else
				     {
				    	//valida si existe operadores iguales en forma consecutiva 
					     Pattern patron3 = Pattern.compile("([|]+[|]|[*]+[*]|[ ])");
					     Matcher buscarRepetidos  = patron3.matcher(expresion);
					     if (buscarRepetidos.find())
					     {  
					    	  mensajeError = "Hay operadores iguales consecutivos y/o espacios en blanco";
					     }
					     else
					     {	  
					    	   //verifica si los parentesis estan equilibrados
							    Stack<Integer> pila = new Stack<>();
						        for (int i = 0; i < expresion.length(); i++)
						        {
						            if (expresion.charAt(i) == '(') 
						            {
						                pila.push(1);
						            } 
						            else 
						            {
						            	if (expresion.charAt(i) == ')')
						            	{
							                try 
							                {
							                    pila.pop();
							                } 
							                catch (Exception e) 
							                {
							                	System.out.println("Error de excepción en la pila - validadorExpresionRegular");
							                	return mensajeError = "Los parentesis no estan equilibrados";
							                }
						            	}  	
						            }
						        }
						        if (pila.empty()) 
						        { 	
						        	mensajeError = "SINERROR";
						        } 
						        else 
						        { 
						        	mensajeError = "Los paréntesis no estan equilibrados";
						        }	       	   
						}
				     } 
			     } 
			}
		} 
	    return mensajeError;	 		
	} 
	
    
/**
 * Metodo de union e interseccion de automatas     
 * @param operacion Variable de tipo int 
 */
    public void UnionInterseccionAutomata(int operacion)
    {
    	if(listaAutomatas.size()>=2)
    	{
    		Automata automataInterseccion = unionInterAuto.UnionInterseccionDeAutomatas(listaAutomatas.get(0),listaAutomatas.get(1), operacion);	 
           	listaAutomatas.add(automataInterseccion);	 	   
           	CargaAutomataParaMostrar(automataInterseccion);	  
    	} 
    }          
    
/**
 * Metodo de conversion de automatas     
 * @param numeroAutomata Variable de tipo int 
 */
    public void ConversionAutomata(int numeroAutomata)
    {
    	if(listaAutomatas.size()>=1)
    	{
    		Automata automataConvertido = converAuto.ConversionDeAutomata(listaAutomatas.get(numeroAutomata)) ;	 
           	listaAutomatas.add(automataConvertido);	 	   
           	CargaAutomataParaMostrar(automataConvertido);	  
    	} 
    }           
     
/**
 * Metodo de conversion de ER a un automata      
 */
    public void ConversionERAutomata()
    {    	 
    	if(ER!=null)
    	{
    		listaParametros.add(ER);
        	listaParametros.add("----------------------------------------------\n");  //separador de parametros	
          	Automata ERConvertido = converER.ConversionER(ER);	 
           	listaAutomatas.add(ERConvertido);	 	   
           	CargaAutomataParaMostrar(ERConvertido);	   
    	} 
    }       
    
/**
 * Metodo que minimiza un automata   
 * @param nroAutomata Variable de tipo int    
 */
    public void MinimizarAutomata(int nroAutomata)
    { 
    	if(listaAutomatas.size()>=1)
    	{
    		Automata automataMinimizado = minAuto.minimizar(listaAutomatas.get(nroAutomata));	  
        	listaAutomatas.add(automataMinimizado);		
        	CargaAutomataParaMostrar(automataMinimizado);    
    	}
    }        
    
//-------------------------------------------------------------------------------------------------
// Bloque de proceso por consola desabilitada    
/*    
*//**
 * Metodo del proceso principal    - por consola
 *//*   
    public void ProcesoPrincipal()
    {
    	//Elegir tipo de operacion: union-interseccion-conversion
    	int operacion =0;  	//0 = posicion 0 de la lista de automatas (automata original 1) 
    						//1 = posicion 1 de la lista de automatas (automata original 2)
		 					//2 = posicion 2 de la lista de automatas (automata resultante - Union/Inteseccion/Convertido)
    	
    	if(CargarArchivo())
    	{    		
    		String mensaje ="Lenguaje L: "+ descripcionLenguaje+ "n\n";
        	System.out.println(mensaje); 
        	listaSalidas.add(mensaje);
        	
        	if(!listaCadenas.isEmpty())
        	{ 
        		for(int i=0; i< listaCadenas.size(); i++)
        		{ 
           	   	  AnalizaCadena(listaAutomatas.get(operacion), listaCadenas.get(i), i+1);   
        		}
        	}
        	else
        	{
        		mensaje ="No hay cadenas para procesar...";
        		System.out.println(mensaje);
        		listaSalidas.add(mensaje);
        	} 
    	} 
    	else
    	{
    		String mensajeError = "Error de archivo...";
    		System.out.println(mensajeError);
    		listaSalidas.add(mensajeError);
    	}
    } 
    
    
*//**
 * Metodo que ejecuta todo el programa creando una instancia de la clase AFD
 * @param args de tipo String[]
 *//*	
    public static void main(String args[]) 
    { 
    	 AFD a = new AFD(); 
    	 a.ProcesoPrincipal();
    	 
     } 
     */
}

