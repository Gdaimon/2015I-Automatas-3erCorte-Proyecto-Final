import java.util.*;


public class MinimizarAutomata 
{
	protected String estadoInicial ;
	protected String estadoAceptacion;	
	protected String descripcionLenguaje;
	protected String alfabeto;
	protected int    cantTransiciones; 
	protected LinkedList<Estados> listaEstadosNew;      
	protected Map<String, Estados> mapaTransicionesNuevo;   
	public    LinkedList<String> listaEstadosFinales;
	public    LinkedList<String> listaEstadosNoFinales; 
	public    LinkedList<LinkedList<Matriz>> listaMatrizFila;
	

	public Automata minimizar(Automata automata) 
	{ 
		listaMatrizFila       = new LinkedList<LinkedList<Matriz>>();
		listaEstadosFinales   = new LinkedList<String>();
		listaEstadosNoFinales = new LinkedList<String>();	
		mapaTransicionesNuevo = new HashMap<String,Estados>(); 
		
		//realiza una copia del automata		
		LinkedList<Estados> listaEstadosOriginal = new LinkedList<Estados>(); 
		listaEstadosOriginal = (LinkedList<Estados>) automata.listaEstados.clone(); 
		
		Automata AutomataCopia = new Automata(automata.alfabeto, listaEstadosOriginal, automata.mapaTransiciones, automata.descripcionLenguaje, automata.cantTransiciones);			   
		char[] arrAlfabeto = AutomataCopia.alfabeto.toCharArray();	 
		 
		EliminaEstadosInaccesibles(AutomataCopia);
		GeneraListaEstadosFinalesYNoFinales(AutomataCopia); 
	
		ArmaMatrizLista(AutomataCopia);		
		MarcaEstadosDistinguibles();  //identifica estados distinguibles
		
		//proceso principal 1 - Analiza y marca cada elemento de la matriz principal
		for(int i=0; i < listaMatrizFila.size(); i++)
		{
			for(int j=0; j < listaMatrizFila.get(i).size(); j++)
			{ 
				String marca = listaMatrizFila.get(i).get(j).getMarca();
				 
				if(!marca.equals("*"))    //descarta los distinguibles
				{ 
					for(int k=0; k < arrAlfabeto.length; k++)
					{
						String claveMap = listaMatrizFila.get(i).get(j).getEstadoFila()+arrAlfabeto[k];
						if (AutomataCopia.mapaTransiciones.containsKey(claveMap))
		    			{ 
		    				String estadoDest1 = AutomataCopia.mapaTransiciones.get(claveMap).getNombre(); 
		    				claveMap = listaMatrizFila.get(i).get(j).getEstadoColumna()+arrAlfabeto[k];  
		    				
		    				if (AutomataCopia.mapaTransiciones.containsKey(claveMap))
			    			{   
		    					Estados est= new Estados();
		    					est=AutomataCopia.mapaTransiciones.get(claveMap);
		    					String estadoDest2 = est.nombre; 
		    					
		    					if(!estadoDest1.equals(estadoDest2))
		    					{
		    						String marcaDest =BuscarRelacionEstado(estadoDest1, estadoDest2);
		    						if(marcaDest.equals("*"))  //si esta marcada como distinguible
		    						{
		    							if(!marca.equals("NULL")) //verifica si tiene otra marca (relacion origen)
		    							{  
		    								String [] ms= marca.split(";");
		    								
		    								for(int s=0; s < ms.length; s++)
		    								{
		    									String [] e = ms[s].split("-");
			    								MarcarRelacionEstado(e[0], e[1], "*");
		    								} 
		    							}
		    							listaMatrizFila.get(i).get(j).setMarca("*"); //lo marca como distinguible
		    						}
		    						else
		    						{
		    							String marcaAux=listaMatrizFila.get(i).get(j).getEstadoFila()+ "-" + listaMatrizFila.get(i).get(j).getEstadoColumna();
		    							MarcarRelacionEstado(estadoDest1, estadoDest2, marcaAux); 
		    						} 
		    					} 
			    			}
		    				else
			    			{
			    				String mensaje = "Transición no encontrada - Limbo 2 - verificar si todas las transiciones estan correctamente cargadas";
			    				System.out.println(mensaje+"\n");		    				 
			    			}     	
		    			}
		    			else
		    			{
		    				String mensaje = "Transición no encontrada - Limbo 1 - verificar si todas las transiciones estan correctamente cargadas";
		    				System.out.println(mensaje+"\n");		    				 
		    			}     		
					}
				}  
			}  
		} 
		
		//MostrarMatriz();
		
		//Proceso pricipal 2 - Genera nuevos estados compuestos y elimina los individuales que formaron parte de la compuesta
		
		for(int i=0; i < listaMatrizFila.size(); i++)
		{
			for(int j=0; j < listaMatrizFila.get(i).size(); j++)
			{  
				//System.out.println("marcas   "+listaMatrizFila.get(i).get(j).getEstadoFila()+"  -  "+listaMatrizFila.get(i).get(j).getEstadoColumna()+"   -  "+listaMatrizFila.get(i).get(j).getMarca());
								
				if(!listaMatrizFila.get(i).get(j).getMarca().equals("*"))
				{
					//genero nuevos estados compuestos
					Estados estadoNuevo = new Estados();
					estadoNuevo.setNombre(listaMatrizFila.get(i).get(j).getEstadoFila()+"-"+listaMatrizFila.get(i).get(j).getEstadoColumna());
					
					//System.out.println(estadoNuevo.getNombre() + "  los vacios");	 
					 
					if((estadoNuevo.VerificaEstadoAceptacion(automata.listaEstados, estadoNuevo.BuscarEstado(listaMatrizFila.get(i).get(j).getEstadoFila(), automata.listaEstados))) ||
					   (estadoNuevo.VerificaEstadoAceptacion(automata.listaEstados, estadoNuevo.BuscarEstado(listaMatrizFila.get(i).get(j).getEstadoColumna(), automata.listaEstados))))
					{ //verifica si uno de los dos estados es de aceptacion
						estadoNuevo.setEstadoAceptacion(true); 
					}
					else
					{
						estadoNuevo.setEstadoAceptacion(false); 
					}
					
					if((estadoNuevo.BuscarEstado(listaMatrizFila.get(i).get(j).getEstadoFila(), automata.listaEstados).isEstadoInicial()) ||
					   (estadoNuevo.BuscarEstado(listaMatrizFila.get(i).get(j).getEstadoColumna(), automata.listaEstados).isEstadoInicial()))
							{ //verifica si uno de los dos estados es de inicio
								estadoNuevo.setEstadoInicial(true); 
							}
							else
							{
								estadoNuevo.setEstadoInicial(false); 
							} 
					
					AutomataCopia.listaEstados.add(estadoNuevo); 
					
					EliminaEstados(AutomataCopia, listaMatrizFila.get(i).get(j).getEstadoFila());	
					EliminaEstados(AutomataCopia, listaMatrizFila.get(i).get(j).getEstadoColumna());
				} 
			}
		} 
		
	//MostrarListaEstados(AutomataCopia.listaEstados);	 
	 
	ArmaNuevoMapaTransiciones(AutomataCopia);	  //graba en mapaTransicionesNuevo
	descripcionLenguaje = "Minimizado - " + AutomataCopia.descripcionLenguaje; 
	cantTransiciones	= mapaTransicionesNuevo.size();
	Automata AutomataNuevo = new Automata(automata.alfabeto, AutomataCopia.listaEstados, mapaTransicionesNuevo, descripcionLenguaje, cantTransiciones);	 
	
	return AutomataNuevo;	 
	}
		
	 /**
      *Metodo que elimina los estados inaccesibles y sus transiciones asociadas     
	  *@param auto Variable de tipo Automata		  
	  */   
	public void EliminaEstadosInaccesibles(Automata auto)
	{
		char[] arrAlfabeto = auto.alfabeto.toCharArray();	
		
		//Identifica estados inaccesibles y sus transiciones
		 
		for(int i=0; i < auto.listaEstados.size(); i++)
		{ 
			boolean encontro = false;
			if(!auto.listaEstados.get(i).estadoInicial)  //descarta el estado inicial
			{
				Iterator it = auto.mapaTransiciones.entrySet().iterator();
				while (it.hasNext())
			  	{
				  	Map.Entry e = (Map.Entry)it.next();   
			        Estados mapEstado = new Estados();
			        mapEstado = (Estados)e.getValue();
			        String clave = (String) e.getKey();
			               
			        if(mapEstado == auto.listaEstados.get(i))
			        {
			        	//System.out.println("tiene destino "+mapEstado.getNombre() + "Clave "+clave);
			        	encontro = true;
			        }
			  	}
				if(!encontro)
				{
					//System.out.println("inaccesible " + auto.listaEstados.get(i).getNombre());
					
					for(int j=0; j < arrAlfabeto.length; j++)
					{
						String clave = auto.listaEstados.get(i).getNombre()+arrAlfabeto[j];
						if (auto.mapaTransiciones.containsKey(clave))
						{
							auto.mapaTransiciones.remove(clave);
						} 
					} 
					auto.listaEstados.remove(i);   //elimino el estado inaccesible.
				}    	
			} 
		}  
	}	  
	

	/**
	 *Metodo que genera la lista de estados finales y no finales     
	 *@param auto Variable de tipo Automata		  
	 */  	 
	public void GeneraListaEstadosFinalesYNoFinales(Automata auto)
	{
		for(int i=0; i < auto.listaEstados.size(); i++)
		{
			if(auto.listaEstados.get(i).isEstadoAceptacion())
			{
				listaEstadosFinales.add(auto.listaEstados.get(i).getNombre());
			}
			else
			{
				listaEstadosNoFinales.add(auto.listaEstados.get(i).getNombre());
			}
		}
	}

/**
 *Metodo que arma la semi-matriz     
 *@param auto Variable de tipo Automata		  
 */ 
	public void ArmaMatrizLista(Automata auto)
	{  
		//graba cada listacolumna en la lista principal (fila) 
		for(int i=0; i < auto.listaEstados.size()-1; i++)
		{
			LinkedList<Matriz> listaMatrizColumna = new LinkedList<Matriz>();
			for(int j=auto.listaEstados.size(); j > i+1; j--)
			{  
				Matriz dato = new Matriz(auto.listaEstados.get(i).getNombre(), auto.listaEstados.get(j-1).getNombre(),"NULL"); 
				listaMatrizColumna.add(dato);
			} 
			listaMatrizFila.add(listaMatrizColumna);
		} 		 
	}

/**
 *Metodo que marca los estados distinguibles (relacion fila-columna)      	  
 */ 
	public void MarcaEstadosDistinguibles()
	{
		for(int i=0; i < listaMatrizFila.size(); i++)
		{
			for(int j=0; j < listaMatrizFila.get(i).size(); j++)
			{
				String estadoF = listaMatrizFila.get(i).get(j).getEstadoFila();
				String estadoC = listaMatrizFila.get(i).get(j).getEstadoColumna();
				if(VerificaEstadoFinal(estadoF) && VerificaEstadoFinal(estadoC))
				{
					listaMatrizFila.get(i).get(j).setMarca("NULL");
					//System.out.println("marca distinguibles  " + estadoF + " -- "+ estadoC);
				}
				else
				{
					if(VerificaEstadoFinal(estadoF) || VerificaEstadoFinal(estadoC))
					{
						listaMatrizFila.get(i).get(j).setMarca("*");
						//System.out.println("marca distinguibles  " + estadoF + " -- "+ estadoC);
					}
				} 
			}  
		} 
	}

	/**
	 *Metodo que busca si el estado esta en la lista de finales     
	 *@param estado Variable de tipo String
	 *@return Variable de tipo boolean 	  
	 */ 
		public boolean VerificaEstadoFinal(String estado)
		{
			boolean esFinal = false;
			for(int i=0; i < listaEstadosFinales.size(); i++)
			{ 		
				if(listaEstadosFinales.get(i).equals(estado))
				{
					esFinal = true;
					i= listaEstadosFinales.size();
				}				 
			} 			
			return esFinal;
		} 
		
		
		/**
		 *Metodo que marca una determinada relacion (elemento fila-columna) de la matriz  
		 *@param estadoDest1 Variable de tipo String
		 *@param estadoDest2 Variable de tipo String 	
		 *@param marca Variable de tipo String  
		 */ 	
		public void MarcarRelacionEstado(String estadoDest1, String estadoDest2, String marca)
		{
			for(int i=0; i < listaMatrizFila.size(); i++)
			{
				for(int j=0; j < listaMatrizFila.get(i).size(); j++)
				{ 
					if((listaMatrizFila.get(i).get(j).getEstadoFila().equals(estadoDest1) && listaMatrizFila.get(i).get(j).getEstadoColumna().equals(estadoDest2)) || 
					   (listaMatrizFila.get(i).get(j).getEstadoFila().equals(estadoDest2) && listaMatrizFila.get(i).get(j).getEstadoColumna().equals(estadoDest1))) 	
					{
						if(marca.equals("*"))
						{
							listaMatrizFila.get(i).get(j).setMarca(marca); 
						}
						else
						{
							if(listaMatrizFila.get(i).get(j).getMarca().equals("NULL"))
							{
								listaMatrizFila.get(i).get(j).setMarca(marca);
							}
							else
							{ 
								listaMatrizFila.get(i).get(j).setMarca(listaMatrizFila.get(i).get(j).getMarca()+";"+marca);
							} 
						}
						 
					} 
				}
			} 
		}
		
		/**
		 *Metodo que busca la relacion (elemento fila-columna) en la matriz  
		 *@param estadoDest1 Variable de tipo String
		 *@param estadoDest2 Variable de tipo String 	
		 *@return Variable de tipo String  
		 */ 	
		public String BuscarRelacionEstado(String estadoDest1, String estadoDest2)
		{
			String marca = "";
			for(int i=0; i < listaMatrizFila.size(); i++)
			{
				for(int j=0; j < listaMatrizFila.get(i).size(); j++)
				{ 
					if((listaMatrizFila.get(i).get(j).getEstadoFila().equals(estadoDest1) && listaMatrizFila.get(i).get(j).getEstadoColumna().equals(estadoDest2)) || 
					   (listaMatrizFila.get(i).get(j).getEstadoFila().equals(estadoDest2) && listaMatrizFila.get(i).get(j).getEstadoColumna().equals(estadoDest1))) 	
					{
						marca = listaMatrizFila.get(i).get(j).getMarca();
						return marca;
					} 
				}
			}
			return marca; 
		}
	
		/**
		 *Metodo que elimina estados de la lista de estados  
		 *@param estado Variable de tipo String 
		 *@param auto de tipo Automatas  
		 */ 	
		public void EliminaEstados(Automata auto, String estado)
		{
			for(int i=0; i < auto.listaEstados.size(); i++)
			{
				if(auto.listaEstados.get(i).getNombre().equals(estado))
				{
					auto.listaEstados.remove(i);
				}
			} 
		}
		
		/**
		 *Metodo que arma el nuevo mapa de transiciones   
		 *@param auto de tipo Automatas  
		 */ 		
		public void ArmaNuevoMapaTransiciones(Automata auto)
		{
			char[] arrAlfabeto = auto.alfabeto.toCharArray();	
			for(int i=0; i < auto.listaEstados.size(); i++)
			{
				String [] estado = auto.listaEstados.get(i).getNombre().split("-");  
				
				for(int k=0; k < estado.length; k++)
				{
					for(int j=0; j < arrAlfabeto.length; j++)
					{
						String claveMap = estado[k]+ arrAlfabeto[j];  
		    			
		    			if (auto.mapaTransiciones.containsKey(claveMap))
		    			{
		    				//System.out.println("Clave mapa "+claveMap+"--");
		    				
		    				String estadoDestino = auto.mapaTransiciones.get(claveMap).getNombre(); 
		    				String claveNueva    = auto.listaEstados.get(i).getNombre() + arrAlfabeto[j]; 
		    				
		    				if (!mapaTransicionesNuevo.containsKey(claveNueva))
		    				{
		    					mapaTransicionesNuevo.put(claveNueva, BuscarEstado2(estadoDestino, auto.listaEstados));  			    			 
		    				} 
		    			}
		    			else
		    			{
		    				String mensaje = "Transición no encontrada - Limbo - ver ArmaNuevoMapaTransiciones";
		    				System.out.println(mensaje+"\n"); 
		    			}    
					}
				} 
			} 
		}
		
		
		/**
		 *Metodo que busca un estado en la lista de estados   
		 *@param nombreEstado Variable de tipo String
		 *@param listaE Variable de tipo LinkedList  
		 *@return Variable de tipo Estados
		 */ 			
		 public Estados BuscarEstado2(String nombreEstado, LinkedList<Estados> listaE )
		 {
			 for(int m=0; m< listaE.size();m++)		 
	    		{    			
	        		String [] e = listaE.get(m).getNombre().split("-");
	        		
	        		for( int i=0; i < e.length; i++)
	        		{
	        			if(e[i].equals(nombreEstado))
		    			{				 
		    				return listaE.get(m);				
		    			}
	        		} 
	    		}
	       	return null;    	
	     }  
		 
		
		 	 
		 public void MostrarMatriz()
		 {
			 System.out.println("matriz marcada inicio");
			 for(int i=0; i < listaMatrizFila.size(); i++)
				{
					for(int j=0; j < listaMatrizFila.get(i).size(); j++)
					{ 
						    String estadoFila =  listaMatrizFila.get(i).get(j).estadoFila;
						    String estadoColumna =  listaMatrizFila.get(i).get(j).estadoColumna;
							String marca = listaMatrizFila.get(i).get(j).getMarca();
							System.out.println(estadoFila+" - "+estadoColumna+" - "+marca); 							
					}
				} 
			 System.out.println("matriz marcada fin");
		 }
		 
		 public void MostrarListaEstados(LinkedList<Estados> lista)
		 {
			 System.out.println("Lista nueva inicio");
			 for(int i=0; i < lista.size(); i++)
				{ 
						    String estado =  lista.get(i).getNombre();
						    boolean inicio =  lista.get(i).isEstadoInicial();
							boolean acepta =  lista.get(i).isEstadoAceptacion();
							System.out.println(estado+" - "+inicio+" - "+acepta); 	 
				} 
			 System.out.println("Lista nueva fin");
		 }
		 
}
