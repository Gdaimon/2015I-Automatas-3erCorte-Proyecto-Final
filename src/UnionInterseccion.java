import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class UnionInterseccion
{
	protected String estadoInicial ;
	protected String estadoAceptacion;	
	protected String descripcionLenguaje;
	protected String alfabeto;
	protected int    cantTransiciones; 
	protected LinkedList<Estados> NuevaListaEstados;      
	protected Map<String, Estados> NuevoMapaTransiciones;  
	

	/**
	 * Metodo que une dos automatas   
	 * @param automata1 Variable de tipo Automata
	 * @param automata2 Variable de tipo Automata
	 * @param operacion Variable de tipo int
	 * @return Variable de tipo Automata
	 */
	public Automata UnionInterseccionDeAutomatas(Automata automata1, Automata automata2, int operacion)
	{  
    	NuevaListaEstados = new LinkedList<Estados>();	
    	NuevoMapaTransiciones = new HashMap<String,Estados>();
    	String NuevoNombreEstado ="";     	
    	boolean estadoInicial    = true;
    	boolean estadoAceptacion = true;	     	
    	
    	//leo cada estado del automata 1 y lo combino con cada estado del automata 2 y lo guardo en una lista    
    	for(int i=0; i < automata1.listaEstados.size(); i++)
    	{
    		for(int j=0; j < automata2.listaEstados.size(); j++)
    		{
    			NuevoNombreEstado = automata1.listaEstados.get(i).nombre + "-" +  automata2.listaEstados.get(j).nombre;
    			
    			if(automata1.listaEstados.get(i).estadoInicial && automata2.listaEstados.get(j).estadoInicial)
    			{
    				estadoInicial = true;
    			}
    			else
    			{
    				estadoInicial = false;
    			}
    			if(operacion == 1)   //UNION
    			{
    				if(automata1.listaEstados.get(i).estadoAceptacion || automata2.listaEstados.get(j).estadoAceptacion)
        			{
        				estadoAceptacion = true;
        			}
        			else
        			{
        				estadoAceptacion = false;
        			}
    			}
    			
    			if(operacion == 2)    //INTERSECCION
    			{
    				if(automata1.listaEstados.get(i).estadoAceptacion && automata2.listaEstados.get(j).estadoAceptacion)
        			{
        				estadoAceptacion = true;
        			}
        			else
        			{
        				estadoAceptacion = false;
        			}
    			} 
    			
    			Estados NuevoEstado = new Estados(NuevoNombreEstado, estadoInicial, estadoAceptacion);
    			NuevaListaEstados.add(NuevoEstado);    			
    		}
    	} 
	    
    	//Crear transiciones - combino la transicion del automata 1 con el automata 2   	
    	for(int l=0; l<NuevaListaEstados.size();l++)
    	{
    		String[] subEstado = NuevaListaEstados.get(l).nombre.split("-");    		 
    		char[] alfa = automata1.getAlfabeto().toCharArray(); 
    		
    		for(int n=0; n < alfa.length; n++)
    		{
    			boolean control = true;
    			String estadoDestinoA1 = "";   
    			String estadoDestinoA2 = "";  
    			
        		String claveMap1 = subEstado[0] + alfa[n]; 
        		String claveMap2 = subEstado[1] + alfa[n];
        		
        		if (automata1.mapaTransiciones.containsKey(claveMap1))
    			{
        			estadoDestinoA1 = automata1.mapaTransiciones.get(claveMap1).getNombre(); 
        		}
    			else
    			{
    				control = false;
    				System.out.println("Transición no encontrada - Error - verificar lista de estados del automata 1"); 
    			}     		 
    			
        		if (automata2.mapaTransiciones.containsKey(claveMap2))
    			{        		  
        			estadoDestinoA2 = automata2.mapaTransiciones.get(claveMap2).getNombre();
        		}
    			else
    			{
    				control = false;
    				System.out.println("Transición no encontrada - Error - verificar lista de estados del automata 2"); 
    			}     	
        		
        		if(control)
        		{
        			String estadoDestino = estadoDestinoA1 + "-" + estadoDestinoA2; 
        			Estados estadoAux = new Estados();
        			String claveMapNueva  = NuevaListaEstados.get(l).nombre + alfa[n];  
        			NuevoMapaTransiciones.put(claveMapNueva, estadoAux.BuscarEstado(estadoDestino, NuevaListaEstados));           
        		}        		 
    		} 
    	}  
	    	
    	//crea el nuevo automata y retorna
    	descripcionLenguaje = automata1.getDescripcionLenguaje() + " -Union- " + automata2.getDescripcionLenguaje();
    	cantTransiciones    = NuevoMapaTransiciones.size();
    	
    	Automata AutomataUnion = new Automata(automata1.getAlfabeto(), NuevaListaEstados, NuevoMapaTransiciones, descripcionLenguaje, cantTransiciones);			   
		return AutomataUnion;				 
	}   
}
