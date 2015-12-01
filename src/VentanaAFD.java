/** 
 * @author Mauricio Nishida y  Carlos Charris - 
 * Proyecto de aula - Autómatas Gramáticas y Lenguajes
 * @version Interface gráfica
 * 
 */
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
 
public class VentanaAFD extends JFrame
{
	 private JButton btnProcesarCadena;
     private JButton btnImportarArchivo;
     private JButton btnCargarArchivoAutomatas;
     private JButton btnUnion;
     private JButton btnInterseccion;
     private JButton btnConversion;
     private ButtonGroup grupoBotonesOpcion;                
     private JTextArea areaParametros, areaCadena;
     private Container cntAreas, cntOperaciones;
     private JLabel lblNewLabel_2;
     private JLabel lblNewLabel_3;
     private JLabel lblNewLabel_4;
     private JScrollPane scrollPane_3;
     private JTextArea areaSalida;
     private JPanel panel;
     private JPanel panelCodificacion;
     private JPanel panelCodificacion_1;        
     private JLabel lblSalida; 
     private int numeroAutomata = 0;

     private AFD AFD;              
     private JButton  btnReiniciar;
     private JButton  btnMinimizar;
     private JButton  btnERaAutomata;
     private String[] listaAutomatasBox;
     private JComboBox comboBoxSeleccionAutomata;
     private JLabel labelSeleccionAutomata;
     private DefaultComboBoxModel modeloCombo; 
     private JButton btnCargarArchivoER;
     private JLabel labelFiller1;
     private JButton btnGraficadorER;     
        
     /**
   	  * Constructor de la clase
   	  */    
    public VentanaAFD() {

            super("Automatas Gramática y Lenguajes - Proyecto de aula - Entrega Final"); 
            
            AFD = new AFD();
            
            getContentPane().setLayout(new BorderLayout(3,3));
            getContentPane().setBackground(Color.cyan);
           
            grupoBotonesOpcion = new ButtonGroup();
           
            areaParametros = new JTextArea();
            areaParametros.setWrapStyleWord(true);
            areaParametros.setLineWrap(true);
            areaCadena = new JTextArea();
            areaCadena.setLineWrap(true);
            areaCadena.setEditable(false);            
           
            cntAreas = new Container();
            cntOperaciones = new Container();            
            cntOperaciones.setLayout(new GridLayout(3,3)); 
            
            btnImportarArchivo = new JButton("Importar Archivo");
            btnImportarArchivo.setForeground(Color.BLUE);
            btnImportarArchivo.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnImportarArchivo.addActionListener( new ManejadorBotonOpcion() );
            cntOperaciones.add(btnImportarArchivo);
            
            btnUnion = new JButton("Uni\u00F3n de Automatas");
            btnUnion.setForeground(Color.DARK_GRAY);
            btnUnion.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnUnion.addActionListener( new ManejadorBotonOpcion() );
            
            btnProcesarCadena = new JButton("Procesar Cadena");
            btnProcesarCadena.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnProcesarCadena.setForeground(Color.BLUE);            
            btnProcesarCadena.addActionListener( new ManejadorBotonOpcion());
            cntOperaciones.add(btnProcesarCadena);  
            
            lblNewLabel_2 = new JLabel("");
            cntOperaciones.add(lblNewLabel_2);
            cntOperaciones.add(btnUnion);
            
            btnMinimizar = new JButton("Minimizaci\u00F3n");
            btnMinimizar.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnMinimizar.setForeground(Color.DARK_GRAY);
            cntOperaciones.add(btnMinimizar);
            btnMinimizar.addActionListener( new ManejadorBotonOpcion() );
            
            btnCargarArchivoAutomatas = new JButton("Cargar Archivo Automatas");
            btnCargarArchivoAutomatas.addActionListener( new ManejadorBotonOpcion() );
            btnCargarArchivoAutomatas.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnCargarArchivoAutomatas.setForeground(Color.BLUE);
            cntOperaciones.add(btnCargarArchivoAutomatas);
            
            btnInterseccion = new JButton("Intersecci\u00F3n de automatas");
            btnInterseccion.setForeground(Color.DARK_GRAY);
            btnInterseccion.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnInterseccion.addActionListener( new ManejadorBotonOpcion() );
            
            labelFiller1 = new JLabel("");
            cntOperaciones.add(labelFiller1);
            
            labelSeleccionAutomata = new JLabel("Seleccionar automata :  ");
            labelSeleccionAutomata.setHorizontalAlignment(SwingConstants.LEFT);
            labelSeleccionAutomata.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
            labelSeleccionAutomata.setForeground(Color.GRAY);
            cntOperaciones.add(labelSeleccionAutomata);
            cntOperaciones.add(btnInterseccion);
            modeloCombo 			= new DefaultComboBoxModel();  
            
            btnCargarArchivoER = new JButton("Cargar Expresi\u00F3n Regular");
            btnCargarArchivoER.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnCargarArchivoER.setForeground(Color.BLUE);
            
            
            btnConversion = new JButton("Conversi\u00F3n a AFD");
            btnConversion.setForeground(Color.DARK_GRAY);
            btnConversion.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnConversion.addActionListener( new ManejadorBotonOpcion() );
            cntOperaciones.add(btnConversion);
            cntOperaciones.add(btnCargarArchivoER);
            btnCargarArchivoER.addActionListener( new ManejadorBotonOpcion() ); 
            
            btnReiniciar = new JButton("Reiniciar");
            btnReiniciar.setFont(new Font("Tahoma", Font.BOLD, 12));
            btnReiniciar.setForeground(Color.RED);
            cntOperaciones.add(btnReiniciar);
            btnReiniciar.addActionListener( new ManejadorBotonOpcion() );
            
            comboBoxSeleccionAutomata = new JComboBox();
            comboBoxSeleccionAutomata.setModel(new DefaultComboBoxModel(new String[] {"<nro de automata>"}));
            comboBoxSeleccionAutomata.setToolTipText("Seleccion de automata a procesar\r\n");
            comboBoxSeleccionAutomata.setFont(new Font("Tahoma", Font.BOLD, 11));
            comboBoxSeleccionAutomata.setForeground(Color.GRAY);
            cntOperaciones.add(comboBoxSeleccionAutomata);
            
            btnERaAutomata = new JButton("Conversi\u00F3n de ER a Automata");             
            btnERaAutomata.setForeground(Color.DARK_GRAY);
            btnERaAutomata.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnERaAutomata.addActionListener( new ManejadorBotonOpcion());
            cntOperaciones.add(btnERaAutomata);
           
            cntAreas.setLayout(new GridLayout(0,3,5,5));
            JScrollPane scrollPane_2 = new JScrollPane(areaParametros);
            cntAreas.add(scrollPane_2);
            
            lblNewLabel_4 = new JLabel("Parametros de entrada");
            lblNewLabel_4.setBackground(Color.YELLOW);
            lblNewLabel_4.setFont(new Font("Tahoma", Font.ITALIC, 15));
            lblNewLabel_4.setForeground(Color.BLUE);
            lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
            scrollPane_2.setColumnHeaderView(lblNewLabel_4);
            JScrollPane scrollPane_1 = new JScrollPane(areaCadena);
            cntAreas.add(scrollPane_1);
            
            lblNewLabel_3 = new JLabel("Cadenas a procesar");
            lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_3.setForeground(Color.BLUE);
            lblNewLabel_3.setFont(new Font("Tahoma", Font.ITALIC, 15));
            scrollPane_1.setColumnHeaderView(lblNewLabel_3);
           
            getContentPane().add(cntOperaciones, BorderLayout.NORTH);
            
            btnGraficadorER = new JButton("Graficador ER");
            btnGraficadorER.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
            btnGraficadorER.setForeground(Color.BLUE);
            btnGraficadorER.addActionListener( new ManejadorBotonOpcion());
            cntOperaciones.add(btnGraficadorER);
            getContentPane().add(cntAreas, BorderLayout.CENTER); 
            
            scrollPane_3 = new JScrollPane();
            cntAreas.add(scrollPane_3);
            
            areaSalida = new JTextArea();
            scrollPane_3.setViewportView(areaSalida);
            
            panel = new JPanel();
            scrollPane_3.setRowHeaderView(panel);
            panel.setLayout(new GridLayout(0, 1, 0, 0));
            
            lblSalida = new JLabel("Salida ");
            lblSalida.setForeground(Color.BLUE);
            lblSalida.setFont(new Font("Tahoma", Font.ITALIC, 15));
            lblSalida.setHorizontalAlignment(SwingConstants.CENTER);
            scrollPane_3.setColumnHeaderView(lblSalida);
            
            panelCodificacion = new JPanel();
            getContentPane().add(panelCodificacion, BorderLayout.SOUTH);
            panelCodificacion.setLayout(new GridLayout(2, 1, 0, 0));
            
            panelCodificacion_1 = new JPanel();
            panelCodificacion.add(panelCodificacion_1);
            panelCodificacion_1.setLayout(new GridLayout(2, 1, 0, 0));
           
            setSize(1250,700);
            setVisible(true);
        } 
     
    /**
	 * Metodo que escucha todos los eventos del programa
	 */
    private class ManejadorBotonOpcion implements ActionListener 
    { 
       // manejar eventos de botón de opción
       public void actionPerformed( ActionEvent evento ) 
       { 
          if(evento.getSource().equals(btnProcesarCadena))	
          {
        	    numeroAutomata = comboBoxSeleccionAutomata.getSelectedIndex();    
	        	areaSalida.setText("");
	            String salida = " "; 
	            AFD.listaSalidas.clear();
	            
	          	if(!AFD.listaCadenas.isEmpty())
	          	{ 
	          		for(int i=0; i< AFD.listaCadenas.size(); i++)
	          		{ 
	              		AFD.AnalizaCadena(AFD.listaAutomatas.get(numeroAutomata), AFD.listaCadenas.get(i), i+1);
	          		}	          		
	          		
	          		for(int k=0; k< AFD.listaSalidas.size();k++)		 
	          		{    			
	          			salida = salida + AFD.listaSalidas.get(k)+"\n";
	          		} 
	                areaSalida.setText(salida);    
	          	}
	          	else
	          	{
	          		salida ="No hay cadenas para procesar...";
	          		areaSalida.setText(salida);   
	          	}    
          }
          else
          {
                if(evento.getSource().equals(btnImportarArchivo))    
                {      
	            	try 
	            	{
	            	    JFileChooser chooser = new JFileChooser(); 
	           	        chooser.showOpenDialog(null);
	           	        
		            	File inFile = chooser.getSelectedFile();  
		            	File outFile = new File("./DATA/automata.txt");
		            	FileInputStream in = new FileInputStream(inFile);
		            	FileOutputStream out = new FileOutputStream(outFile);
		            	
		            	int c;
		            	while( (c = in.read() ) != -1)
		            	out.write(c);
		            	in.close();
		            	out.close();
	            	} 
	            	catch(IOException e) 
	            	{
	            		JOptionPane.showMessageDialog(null, "Error en la carga del archivo");
	            	}  
                }
                else
                {
                	 if(evento.getSource().equals(btnCargarArchivoAutomatas))    
                     {      
     //           		 AFD.Inicializar();            inicializa todo en cada carga
                 		if(AFD.CargarArchivo())
                     	{
                 			 areaParametros.setText(""); 
                 			 areaSalida.setText("");
                              String parametros = "";         		                     
                              for(int j=0; j< AFD.listaParametros.size();j++)		 
                      		 {    			
                      			 parametros = parametros + AFD.listaParametros.get(j)+"\n";
                      		 } 
                              areaParametros.setText(parametros);  
                              
                              areaCadena.setText("");
                              String cadenas = " ";
                              for(int k=0; k< AFD.listaCadenas.size();k++)		 
                      		 {    			
                      			 cadenas = cadenas + AFD.listaCadenas.get(k)+"\n";
                      		 } 
                              areaCadena.setText(cadenas);   
                               
                             String mensaje = "Lenguaje L: "+ AFD.descripcionLenguaje+ "\n";                     	 
                             AFD.listaSalidas.add(mensaje);      
                            
                             cargarListaAutomatas(AFD.listaAutomatas);
                     	}	
                 		else
                 		{
                     		String mensajeError = "Error de archivo...";
                     		System.out.println(mensajeError);
                     	} 
                     }
                	 else
                	 {
                		 if(evento.getSource().equals(btnUnion))    
                		 { 
                			 AFD.UnionInterseccionAutomata(1);   //parametro: 1 Union
                			 //Actualizar pantalla de parametros
                			 areaParametros.setText(""); 
                 			 areaSalida.setText("");
                             String parametros = "";         		                     
                             for(int j=0; j< AFD.listaParametros.size();j++)		 
                      		 {    			
                      		 	 parametros = parametros + AFD.listaParametros.get(j)+"\n";
                      		 } 
                             areaParametros.setText(parametros);   
                             cargarListaAutomatas(AFD.listaAutomatas);
                		 }
                		 else
                		 {
                			 if(evento.getSource().equals(btnInterseccion))    
                    		 {  
                				 AFD.UnionInterseccionAutomata(2);    //parametro: 2 Interseccion
                    			 //Actualizar pantalla de parametros
                    			 areaParametros.setText(""); 
                     			 areaSalida.setText("");
                                 String parametros = "";         		                     
                                 for(int j=0; j< AFD.listaParametros.size();j++)		 
                          		 {    			
                          		 	 parametros = parametros + AFD.listaParametros.get(j)+"\n";
                          		 } 
                                 areaParametros.setText(parametros);   
                                 cargarListaAutomatas(AFD.listaAutomatas);
                    		 }
                			 else
                			 {
                				 if(evento.getSource().equals(btnConversion))    
                        		 {  
                					 numeroAutomata = comboBoxSeleccionAutomata.getSelectedIndex();  
                					 AFD.ConversionAutomata(numeroAutomata);
                        			 //Actualizar pantalla de parametros
                        			 areaParametros.setText(""); 
                         			 areaSalida.setText("");
                                     String parametros = "";         		                     
                                     for(int j=0; j< AFD.listaParametros.size();j++)		 
                              		 {    			
                              		 	 parametros = parametros + AFD.listaParametros.get(j)+"\n";
                              		 } 
                                     areaParametros.setText(parametros);    
                                     cargarListaAutomatas(AFD.listaAutomatas);
                        		 }                				 
                				 else
                    			 {
                			  	     if(evento.getSource().equals(btnReiniciar))    
                            	     {                            			 
                					   AFD.Inicializar();          //  inicializa todo. 
                					   areaParametros.setText(""); 
                           			   areaSalida.setText(""); 
                           			   areaCadena.setText("");
                           			   cargarListaAutomatas(AFD.listaAutomatas);
                            	     } 
                			  	     else
                          		     {
                          			    if(evento.getSource().equals(btnMinimizar))    
                              		    {          
                          			       numeroAutomata = comboBoxSeleccionAutomata.getSelectedIndex();   
                          				   AFD.MinimizarAutomata(numeroAutomata);
                              			   //Actualizar pantalla de parametros
                              			   areaParametros.setText(""); 
                               			   areaSalida.setText("");
                                           String parametros = "";         		                     
                                           for(int j=0; j< AFD.listaParametros.size();j++)		 
                                    	   {    			
                                    	  	 parametros = parametros + AFD.listaParametros.get(j)+"\n";
                                    	   } 
                                           areaParametros.setText(parametros);  
                                           cargarListaAutomatas(AFD.listaAutomatas);
                              		    }
                          			    else
                          			    {
                          			    	if(evento.getSource().equals( btnERaAutomata))    
                          			    	{                                					 
	                           					 AFD.ConversionERAutomata();
	                                   			 //Actualizar pantalla de parametros
	                                   			 areaParametros.setText(""); 
	                                    		 areaSalida.setText("");
	                                             String parametros = "";         		                     
	                                             for(int j=0; j< AFD.listaParametros.size();j++)		 
	                                         	 {    			
	                                         	 	 parametros = parametros + AFD.listaParametros.get(j)+"\n";
	                                         	 } 
	                                             areaParametros.setText(parametros);    
	                                             cargarListaAutomatas(AFD.listaAutomatas);
                                   		    }  
                          			        else
                               			    {
                          			        	if(evento.getSource().equals(btnGraficadorER))    
                               			    	{      
     	                           					try
     	                           					{
														Runtime.getRuntime().exec("java -jar ./JAR/GraficadorER.jar");
													} 
     	                           					catch (IOException e)
     	                           					{														 
														e.printStackTrace();
													} 
                                        	    }     
                          			        	else
                                   			    {
                                   			        if(evento.getSource().equals(btnCargarArchivoER))    
                                                    {   
                                   			        	String cadenaER = null;
                                   			        	cadenaER = JOptionPane.showInputDialog( 
                                   			        			     "Ingrese la Expresión Regular utilizando los siguientes símbolos:\n" +                                   			        			    
                                   			     					 "   Estrella       =  *                                                \n" + 
                                   			     					 "   Unión          =  |  	                                          \n" +
                                   			     					 "   Separador = ( )                                              \n" );
                                   			        			 	
                                   						if(cadenaER !=null)  //botón = aceptar
                                   						{     
                                   							String mensajeError = AFD.validadorExpresionRegular(cadenaER);  
                                   							if(mensajeError.equals("SINERROR"))
                                   							{
                                   								areaParametros.setText(cadenaER);
                                   								AFD.ER = cadenaER; 	
                                   							}
                                   							else
                                   							{
                                   								JOptionPane.showMessageDialog(null,mensajeError);
                                   							}
                                   						}                                    						 
                                                    } 	 
                                   			    } 
                               			    }
                          			    }   
                          		     }
                    			 }
                			 }
                		 }                		 
                	 } 
                } 
       		}      	           
        }
    }
    
    /**
	 * Metodo que carga la lista de automatas de la seleccion del box 	
	 * @param listaAutomatas Variable de tipo LinkedList
	 */	
    public void cargarListaAutomatas(LinkedList<Automata> listaAutomatas)
	{
    	listaAutomatasBox = new String[listaAutomatas.size()];
		modeloCombo.removeAllElements();
		for(int i=0; i<listaAutomatasBox.length; i++)
		{
			listaAutomatasBox[i] = "Automata " + (i+1);
			modeloCombo.addElement(listaAutomatasBox[i]); 
		}
		comboBoxSeleccionAutomata.setModel(modeloCombo); 
	}
     
    
    
    /**
	 * Metodo que ejecuta todo el programa creando la ventana principal	
	 * @param args de tipo String[]
	 */	
    public static void main(String args[]) 
    { 
            JFrame.setDefaultLookAndFeelDecorated(true);
            VentanaAFD ventana = new VentanaAFD();
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     } 
}
