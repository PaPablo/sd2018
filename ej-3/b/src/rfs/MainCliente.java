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
		
//		String s = "   HoLa qUe haceS   todo bIEN?";
//		for (String string : s.toLowerCase().trim().split(" ")) {
//			System.out.println(string);
//		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipalCliente frame = new VentanaPrincipalCliente();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
