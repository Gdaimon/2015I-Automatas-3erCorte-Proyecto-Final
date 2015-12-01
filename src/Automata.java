import java.util.LinkedList;
import java.util.Map;

/**
 * 
 * @author Mauricio Nishida y  Carlos Charris
 *
 */
public class Automata
{  
	protected String estadoInicial ;
	protected String estadoAceptacion;
	
	protected String descripcionLenguaje;
	protected String alfabeto;
	protected int    cantTransiciones; 
	protected LinkedList<Estados> listaEstados;      
	protected Map<String, Estados> mapaTransiciones;  
	 
//Constructores
	/**
	 * Constructor 	 
	 * @param alfabeto Variable de tipo String 
	 * @param listaEstados Variable de tipo LinkedList
	 * @param mapaTransiciones Variable de tipo  Map
	 * @param descripcionLenguaje Variable de tipo String
	 * @param cantTransiciones Variable de tipo int
	 */
	public Automata( String alfabeto, LinkedList<Estados> listaEstados, Map<String, Estados> mapaTransiciones,  			            
			         String descripcionLenguaje, int cantTransiciones)
	{  		
	    this.alfabeto			 = alfabeto;
	    this.listaEstados		 = listaEstados;   	
	    this.mapaTransiciones	 = mapaTransiciones;   
		this.descripcionLenguaje = descripcionLenguaje;
		this.cantTransiciones	 = cantTransiciones; 
	}
  
    
    // Getters & Setters 
     
	public String getEstadoInicial() {
		return estadoInicial;
	}

	public void setEstadoInicial(String estadoInicial) {
		this.estadoInicial = estadoInicial;
	}

	public String getEstadoAceptacion() {
		return estadoAceptacion;
	}

	public void setEstadoAceptacion(String estadoAceptacion) {
		this.estadoAceptacion = estadoAceptacion;
	}

	public String getDescripcionLenguaje() {
		return descripcionLenguaje;
	}

	public void setDescripcionLenguaje(String descripcionLenguaje) {
		this.descripcionLenguaje = descripcionLenguaje;
	}

	public String getAlfabeto() {
		return alfabeto;
	}

	public void setAlfabeto(String alfabeto) {
		this.alfabeto = alfabeto;
	}

	public int getCantTransiciones() {
		return cantTransiciones;
	}

	public void setCantTransiciones(int cantTransiciones) {
		this.cantTransiciones = cantTransiciones;
	}

	public LinkedList<Estados> getListaEstados() {
		return listaEstados;
	}

	public void setListaEstados(LinkedList<Estados> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public Map<String, Estados> getMapaTransiciones() {
		return mapaTransiciones;
	}

	public void setMapaTransiciones(Map<String, Estados> mapaTransiciones) {
		this.mapaTransiciones = mapaTransiciones;
	}
	
	
	
	
}