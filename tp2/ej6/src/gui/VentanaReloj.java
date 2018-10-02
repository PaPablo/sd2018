package gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import reloj.IRelojListener;

public class VentanaReloj extends JFrame implements IRelojListener{

	private JPanel contentPane;
    private JLabel labelReloj;

	/**
	 * Create the frame.
	 */
	public VentanaReloj() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblReloj = new JLabel("00:00:00");
		lblReloj.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblReloj, BorderLayout.CENTER);

        this.labelReloj = lblReloj;
	}

    public void updateClock(String text) {
        this.labelReloj.setText(text);
    }

}
