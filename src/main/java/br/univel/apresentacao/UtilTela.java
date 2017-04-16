package br.univel.apresentacao;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import br.univel.anotacao.Coluna;
import br.univel.comum.Execute;

public class UtilTela {
	Tabela minhaTabela;
	public JPanel gerarTela(Object o) {
		JPanel contentPane = new JPanel();

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gridBagLayout);
		
		gerarCampos(o, contentPane);

		return contentPane;
	}
	
	private void getObjectPreenchido(Object o, JPanel contentPane) {
		Class<? extends Object> cl = o.getClass();
		Field[] vetorFields = cl.getDeclaredFields();
		
		for (Component component : contentPane.getComponents()) {
			if (component instanceof JTextField) {
				JTextField textField = JTextField.class.cast(component);
				
				for (Field field : vetorFields) {
					field.setAccessible(true);
					 
					if (field.getName().equals(textField.getName())) {
						try {
							if ((field.getType().equals(Integer.class)) || (field.getType().equals(int.class))) {
								int valorInt = Integer.parseInt(textField.getText());
								field.set(o, valorInt);
							} else if (field.getType().equals(String.class)) {
								String valorStr = textField.getText();
								field.set(o, valorStr);
							} else if (field.getType().equals(BigDecimal.class)) {
								BigDecimal valorBigDecimal = new BigDecimal(Double.parseDouble(textField.getText()));
								field.set(o, valorBigDecimal);
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						} 
					}
				}
			}
		}
	}
	
	private void atualizarGrade(Object o) {
		minhaTabela.fireTableDataChanged();

		try {
			minhaTabela.getResultSet().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		minhaTabela.Refresh(o);		
	}
	
	private void inserir(Object o, JPanel contentPane) {
		getObjectPreenchido(o, contentPane);
		
		Execute execute = new Execute();
		try {
			execute.executeInsert(o);
			atualizarGrade(o);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void atualizar(Object o, JPanel contentPane) {
		getObjectPreenchido(o, contentPane);
		
		Execute execute = new Execute();
		try {
			execute.executeUpdate(o);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	private void gerarCampos(Object o, JPanel contentPane) {
		Execute execute = new Execute();
		
		JButton btnBuscar;
		JButton btnExlcuir;
		JButton btnGravar;
		
		Class<? extends Object> cl = o.getClass();
		Field[] atributos = cl.getDeclaredFields();
		
		int y = 0;
		
		for (int i = 0; i < atributos.length; i++) {
			Field field = atributos[i];
			
			createLabel(contentPane, field, 0, y);
			y++;

			createTextField(contentPane, field, 0, y);
			y++;
		}

		btnBuscar = createButton(contentPane, "Buscar", 9, y);
		btnBuscar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					execute.executeSelectAll(o);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		btnExlcuir = createButton(contentPane, "Excluir", 10, y);
		btnExlcuir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					execute.executeDelete(o);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		btnGravar = createButton(contentPane, "Gravar", 11, y);
		btnGravar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				inserir(o, contentPane);
			}
		});
		
		y++;
		JScrollPane scrollPane = createScrollPane(contentPane, 0, y);
		createTable(scrollPane, o);		
	}
	
	private String getLabelColuna(Field field) {
		String labelColuna = "Label";

		if (field.isAnnotationPresent(Coluna.class)) {
			Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

			if (!anotacaoColuna.label().isEmpty())
				labelColuna = anotacaoColuna.label();
		}
		
		return labelColuna;
	}	
	
	private int getMaxColuna(Field field) {
		int max = 10;

		if (field.isAnnotationPresent(Coluna.class)) {
			Coluna anotacaoColuna = field.getAnnotation(Coluna.class);
			max = anotacaoColuna.max();
		}
		
		return max;		
	}
	
	private GridBagConstraints createConstraints(int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		return gbc;
	}	
	
	private JLabel createLabel(JPanel contentPane, Field field, int x, int y) {
		JLabel label = new JLabel(getLabelColuna(field));
		GridBagConstraints gbc = createConstraints(x, y);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 0, 5, 5);		
		contentPane.add(label, gbc);
		return label;
	}
	
	private JTextField createTextField(JPanel contentPane, Field field, int x, int y) {
		JTextField textField = new JTextField();
		GridBagConstraints gbc = createConstraints(x, y);
		gbc.gridwidth = 12;		
		gbc.insets = new Insets(0, 0, 5, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;		
		contentPane.add(textField, gbc);
		textField.setColumns(getMaxColuna(field));	
		textField.setName(field.getName());
		return textField;
	}
	
	private JButton createButton(JPanel contentPane, String caption, int x, int y) {
		JButton btn = new JButton(caption);
		GridBagConstraints gbc = createConstraints(x, y);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 0, 5, 5);
		contentPane.add(btn, gbc);
		return btn;
	}
	
	private JScrollPane createScrollPane(JPanel contentPane, int x, int y) {
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc = createConstraints(x, y);
		gbc.gridwidth = 12;
		gbc.insets = new Insets(0, 0, 0, 5);
		gbc.fill = GridBagConstraints.BOTH;
		contentPane.add(scrollPane, gbc);	
		return scrollPane;
	}
	
	private JTable createTable(JScrollPane scrollPane, Object o) {
		JTable table = new JTable();
		scrollPane.setViewportView(table);
		minhaTabela = new Tabela(o);
		table.setModel(minhaTabela);
		return table;
	}
}