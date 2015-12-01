import java.util.LinkedList;

 
/**
 * 
 * @author Mauricio Nishida y  Carlos Charris
 *
 */
public class Estados 
{ 
	protected String nombre;
	protected boolean estadoInicial ;
	protected boolean estadoAceptacion;
	 
//Constructores
	/**
	 * Constructor 	 
	 * @param nombre Variable de tipo String
	 * @param estadoInicial Variable de tipo boolean
	 * @param estadoAceptacion Variable de tipo boolean
	 */
	public Estados(String nombre, boolean estadoInicial, boolean estadoAceptacion)
	{ 
		this.nombre = nombre;
		this.estadoInicial = estadoInicial;		
		this.estadoAceptacion = estadoAceptacion;
	}
	
	/**
	 * Constructor 	 
	 * @param nombre Variable de tipo String	 
	 */
	public Estados(String nombre)
	{ 
		this.nombre= nombre;
		this.estadoInicial= false;
		this.estadoAceptacion= false;
	}
	
	/**
	 * Constructor por defecto 
	 */
	public Estados()
	{ 
		this.nombre= null;
		this.estadoInicial= false;
		this.estadoAceptacion= false;
	}
	
	/**
	 * Metodo que lee la variable nombre
	 * @return Variable de tipo String
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Metodo que escribe la variable nombre
	 * @param nombre Variable de tipo String
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Metodo que lee la variable estadoInicial
	 * @return Variable de tipo boolean
	 */
	public boolean isEstadoInicial() {
		return estadoInicial;
	}
	/**
	 * Metodo que escribe la variable estadoInicial
	 * @param estadoInicial Variable de tipo boolean
	 */
	public void setEstadoInicial(boolean estadoInicial) {
		this.estadoInicial = estadoInicial;
	}
	
	/**
	 * Metodo que lee la variable estadoAceptacion
	 * @return Variable de tipo boolean
	 */
	public boolean isEstadoAceptacion() {
		return estadoAceptacion;
	}

	/**
	 * Metodo que escribe la variable estadoAceptacion
	 * @param estadoAceptacion Variable de tipo boolean
	 */
	public void setEstadoAceptacion(boolean estadoAceptacion) {
		this.estadoAceptacion = estadoAceptacion;
	} 
	
	/**
	 * Metodo que Busca el Estado inicial en la lista de estados   
	 * @param listaEstados Variable de tipo LinkedList
	 * @return Variable de tipo Estados
	 */
	    public Estados BuscaEstadoInicial(LinkedList<Estados> listaEstados)
		{
			for(int i=0; i< listaEstados.size(); i++)
			{
				if(listaEstados.get(i).isEstadoInicial())
				{
					return listaEstados.get(i);
				}
			}
			return null;
		} 
	    
	 /**
	  * Metodo que Verifica si es estado de aceptación     
	  * @param estado Variable de tipo Estados
	  * @param listaEstados Variable de tipo LinkedList
	  * @return Variable de tipo boolean
	  */    
	    public boolean VerificaEstadoAceptacion(LinkedList<Estados> listaEstados,Estados estado)
		{
			for(int i=0; i< listaEstados.size(); i++)
			{  
				if(listaEstados.get(i).getNombre().equals(estado.getNombre()) && listaEstados.get(i).isEstadoAceptacion())
				{					
					return true;
				}
			}
			return false;
		}   
	
		/**
		 * Metodo que Busca los Estados de Aceptacion en la lista de estados  
		 * @param listaEstados Variable de tipo LinkedList
		 * @return Variable de tipo Estados
		 */
		    public Estados BuscaEstadosAceptacion(LinkedList<Estados> listaEstados)
			{
				for(int i=0; i< listaEstados.size(); i++)
				{
					if(listaEstados.get(i).isEstadoAceptacion())
					{
						return listaEstados.get(i);
					}
				}
				return null;
			} 
	    
	    public String toString(){
	    	return " Estado: "+getNombre()+"\n Aceptacion: "+isEstadoAceptacion()+"\n Inicial: "+ isEstadoInicial()+"\n";
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
}
