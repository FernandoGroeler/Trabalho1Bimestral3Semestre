package br.univel.apresentacao;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Field;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import br.univel.anotacao.Coluna;

public class UtilTela {
	public JPanel gerarTela(Object o) {
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gridBagLayout);		
		
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
		
		createButton(contentPane, "Buscar", 9, 6);
		createButton(contentPane, "Excluir", 10, 6);
		createButton(contentPane, "Gravar", 11, 6);	
		
		JScrollPane scrollPane = createScrollPane(contentPane, 0, 7);
		createTable(scrollPane, o);

		return contentPane;
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
	
	/* todo:
	private boolean getColunaObrigatoria(Field field) {
		boolean obrigatorio = false;
		
		if (field.isAnnotationPresent(Coluna.class)) {
			Coluna anotacaoColuna = field.getAnnotation(Coluna.class);
			obrigatorio = anotacaoColuna.obrigatorio();
		}
		
		return obrigatorio;		
	}
	*/
	
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
		
		Tabela tabela = new Tabela(o);
		table.setModel(tabela);
		return table;
	}
}
