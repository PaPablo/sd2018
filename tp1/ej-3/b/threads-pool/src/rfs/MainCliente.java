package rfs;

import java.awt.EventQueue;
import java.io.DataOutputStream;
import java.net.*;

import gui.VentanaPrincipalCliente;
public class MainCliente {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipalCliente frame = 
							new VentanaPrincipalCliente();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
