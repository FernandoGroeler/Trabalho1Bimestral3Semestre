package br.univel.apresentacao;

import java.awt.Container;
import java.awt.EventQueue;
import javax.swing.JFrame;
import br.univel.entidades.Vendedor;

public class FrameVendedor extends Frame {
	private static final long serialVersionUID = -572585322566244523L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameVendedor frame = new FrameVendedor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */	
	public FrameVendedor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setContentPane(getMeuContentPane());
	}
	
	private Container getMeuContentPane() {
		UtilTela u = new UtilTela();
		Vendedor v = new Vendedor();
		
		return u.gerarTela(v);
	}

}
