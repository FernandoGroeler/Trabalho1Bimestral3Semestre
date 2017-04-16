package br.univel.apresentacao;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;
import br.univel.anotacao.Coluna;
import br.univel.comum.Execute;

public class Tabela extends AbstractTableModel {
	private static final long serialVersionUID = 7395886541922199714L;
	private Class<? extends Object> cl;
	private ResultSet resultSet;

	public Tabela(Object o) {
		cl = o.getClass();
		Refresh(o);
	}
	
	public void Refresh(Object o) {
		Execute ex = new Execute();
		try {
			resultSet = ex.executeSelectAll(o);
			resultSet.first();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public ResultSet getResultSet() {
		return this.resultSet;
	}
	
	@Override
	public int getRowCount() {
		try {
			if (resultSet != null) {
				resultSet.last();
				return resultSet.getRow();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int getColumnCount() {		
		Field[] atributos = cl.getDeclaredFields();
		return atributos.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			if (resultSet != null) {
				this.resultSet.absolute(rowIndex + 1);
				return this.resultSet.getString(columnIndex + 1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String getColumnName(int column) {
		Field[] atributos = cl.getDeclaredFields();
		Field field = atributos[column];
		Coluna anotacaoColuna = field.getAnnotation(Coluna.class);
		return anotacaoColuna.label();
	}
}