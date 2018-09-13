package gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.SwingConstants;

public class PanelSettingsServidorCliente extends JPanel {
	private JTextField textFieldHost;
	private JTextField textFieldPort;
	
	private static String host = "localhost";
	private static int port = 8080;

	/**
	 * Create the panel.
	 */
	public PanelSettingsServidorCliente() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelPrincipal = new JPanel();
		add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(new BorderLayout(0, 0));
		
		JPanel panelCentral = new JPanel();
		panelPrincipal.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new GridLayout(2, 2, 0, 0));
		
		JPanel panelHost = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelHost.getLayout();
		flowLayout.setVgap(30);
		flowLayout.setHgap(10);
		panelCentral.add(panelHost);
		
		JLabel labelHost = new JLabel("Host");
		panelHost.add(labelHost);
		labelHost.setHorizontalAlignment(SwingConstants.CENTER);
		
		textFieldHost = new JTextField();
		textFieldHost.setText(host);
		panelHost.add(textFieldHost);
		textFieldHost.setColumns(10);
		
		JPanel panelPort = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panelPort.getLayout();
		flowLayout_1.setVgap(30);
		flowLayout_1.setHgap(10);
		panelCentral.add(panelPort);
		
		JLabel labelPort = new JLabel("Puerto");
		panelPort.add(labelPort);
		labelPort.setHorizontalAlignment(SwingConstants.CENTER);
		
		textFieldPort = new JTextField();
		textFieldPort.setText(String.format("%d", port));
		panelPort.add(textFieldPort);
		textFieldPort.setColumns(10);

	}
	
	public void update() throws NumberFormatException{
		port = Integer.parseInt(this.textFieldPort.getText());
		host = this.textFieldHost.getText();
	}
	
	public static int getPort(){
		return port;
	}
	
	public static String getHost() {
		return host;
	}
	

}
