package br.univel.apresentacao;

import java.lang.reflect.Field;

import javax.swing.table.AbstractTableModel;

public class Tabela extends AbstractTableModel {
	private Class<? extends Object> cl;
	
	public Tabela(Object o) {
		cl = o.getClass();
	}

	@Override
	public int getRowCount() {
		return 0;
	}

	@Override
	public int getColumnCount() {
		Field[] atributos = cl.getDeclaredFields();
		return atributos.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
