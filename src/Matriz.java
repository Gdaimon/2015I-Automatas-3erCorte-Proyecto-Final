
public class Matriz 
{
	protected String estadoFila;
	protected String estadoColumna ;
	protected String marca;
	 
//Constructores
	/**
	 * Constructor 	 
	 * @param estadoFila Variable de tipo String
	 * @param estadoColumna Variable de tipo String
	 * @param marca Variable de tipo String
	 */
	public Matriz(String estadoFila, String estadoColumna, String marca)
	{ 
		this.estadoFila = estadoFila;
		this.estadoColumna = estadoColumna;		
		this.marca = marca;
	}
	
	public Matriz()
	{ 
		this.estadoFila = null;
		this.estadoColumna = null;		
		this.marca = null;
	}

	public String getEstadoFila() {
		return estadoFila;
	}

	public void setEstadoFila(String estadoFila) {
		this.estadoFila = estadoFila;
	}

	public String getEstadoColumna() {
		return estadoColumna;
	}

	public void setEstadoColumna(String estadoColumna) {
		this.estadoColumna = estadoColumna;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	} 
}
