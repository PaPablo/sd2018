package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import exceptions.CouldNotReadFileException;
import exceptions.CouldNotWriteFileException;
import rfs.Client;
import rfs.ClienteRFS;

import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class VentanaPrincipalCliente extends JFrame {
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public VentanaPrincipalCliente() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelPrincipal = new JPanel();
		contentPane.add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(new BorderLayout(20, 0));
		
		JPanel panelCentral = new JPanel();
		panelPrincipal.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50));
		
		JButton leerBtn = new JButton("Leer archivo");
		leerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String filename = JOptionPane
						.showInputDialog(
								"Ingrese el nombre del archivo a leer"
								);
				if (filename == null) return;
				try {
					Client client = new Client();
					client.readFile(filename);
				} catch (CouldNotReadFileException e) {
					JOptionPane.showMessageDialog(
							null,
							String.format(
									"No se pudo leer el archivo %s [%s]",
									filename,
									e.getMessage()));
					System.out.println(e);
				} 
			}
		});
		panelCentral.add(leerBtn);
		
		JButton escribirBtn = new JButton("Escribir archivo");
		escribirBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(panelCentral);
				if (result == JFileChooser.APPROVE_OPTION) {
				    // user selects a file
					File selectedFile = fileChooser.getSelectedFile();
					try {
						Client client = new Client();
						client.writeFile(selectedFile);
					} catch (CouldNotWriteFileException e) {
						JOptionPane.showMessageDialog(
								null,
								String.format(
										"No se pudo escribir el archivo %s",
										selectedFile.getName()));
						System.out.println(e);
					}


				} else {
					System.out.println("No ingresaste ningún archivo");
				}
			}
		});
		panelCentral.add(escribirBtn);
		
		JPanel panelInferior = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panelInferior.getLayout();
		flowLayout_1.setVgap(10);
		panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
		
		JLabel lblNewLabel_1 = new JLabel("Desarrollado por Luciano Serruya Aloisi y Pablo Toledo Margalef");
		panelInferior.add(lblNewLabel_1);
		
		JPanel panelSuperior = new JPanel();
		panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel panelMenu = new JPanel();
		FlowLayout fl_panelMenu = (FlowLayout) panelMenu.getLayout();
		fl_panelMenu.setAlignment(FlowLayout.LEFT);
		panelSuperior.add(panelMenu);
		
		JMenuBar menuBar = new JMenuBar();
		panelMenu.add(menuBar);
		
		JMenu menuPropiedades = new JMenu("Propiedades");
		menuBar.add(menuPropiedades);
		
		JMenuItem menuItemServidor = new JMenuItem("Servidor");
		menuItemServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			      PanelSettingsServidorCliente panelSettingsServidor = new PanelSettingsServidorCliente();

			      int result = JOptionPane.showConfirmDialog(
			    		  null, 
			    		  panelSettingsServidor, 
			    		  "Conexión con el servidor", 
			    		  JOptionPane.OK_CANCEL_OPTION);
			      
			      if (result == JOptionPane.OK_OPTION) {
			    	  try {
						int port = panelSettingsServidor.getPort();
						String host = panelSettingsServidor.getHost();
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(
								null, 
								"Debe ingresar un puerto válido", 
								"Error en el puerto", 
								JOptionPane.ERROR_MESSAGE);
					}
			      }
//				String name = JOptionPane.showInputDialog("Type your name please");
//				JOptionPane.showMessageDialog(null, "Hello " + name);
			}
		});
		menuPropiedades.add(menuItemServidor);
		
		JMenuItem menuItemSalir = new JMenuItem("Salir");
		menuPropiedades.add(menuItemSalir);
		
		JPanel panelDescripcion = new JPanel();
		panelSuperior.add(panelDescripcion);
		
		JLabel lblNewLabel = new JLabel("RFS Client - Sistemas Distribuidos - 2018");
		panelDescripcion.add(lblNewLabel);
	}

}