package br.univel.apresentacao;

import java.awt.Container;
import java.awt.EventQueue;
import java.sql.SQLException;

import javax.swing.JFrame;

import br.univel.comum.Execute;
import br.univel.entidades.Vendedor;

public class FrameVendedor extends Frame {
	private static final long serialVersionUID = -572585322566244523L;
	private Vendedor vendedor;

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
		vendedor = new Vendedor();
		Execute ex = new Execute();
		
		try {
			ex.executeCreateTable(vendedor);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setContentPane(getMeuContentPane());
	}
	
	private Container getMeuContentPane() {
		UtilTela u = new UtilTela();
		return u.gerarTela(vendedor);
	}

}
