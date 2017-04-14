package br.univel.comum;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import br.univel.entidades.Vendedor;
import br.univel.anotacao.Coluna;
import br.univel.anotacao.Tabela;

public class Execute {
	private String getNomeTabela(Class<?> cl) {
		String nomeTabela;
		
		if (cl.isAnnotationPresent(Tabela.class)) {
			Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
			nomeTabela = anotacaoTabela.value();
		} else
			nomeTabela = cl.getSimpleName().toUpperCase();

		return nomeTabela;
	}
	
	private String getTipoColuna(Field field) {
		Class<?> tipo = field.getType();
		
		if (tipo.equals(int.class)) {
			return "INT";
		} else if ((tipo.equals(Double.class)) || (tipo.equals(Float.class)) || (tipo.equals(BigDecimal.class))) {
			return "NUMERIC(10,4)";
		} else {
			return "VARCHAR(100)";
		}		
	}	
	
	private String getNomeColuna(Field field) {
		String nomeColuna;

		if (field.isAnnotationPresent(Coluna.class)) {
			Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

			if (anotacaoColuna.nome().isEmpty())
				nomeColuna = field.getName().toUpperCase();
			else
				nomeColuna = anotacaoColuna.nome();
		} else
			nomeColuna = field.getName().toUpperCase();
		
		return nomeColuna;
	}
	
	private StringBuilder getColunaCreate(Class<?> cl) {
		StringBuilder sb = new StringBuilder();	
		
		Field[] atributos = cl.getDeclaredFields();
		
		for (int i = 0; i < atributos.length; i++) {
			Field field = atributos[i];

			String nomeColuna = getNomeColuna(field);
			String tipoColuna = getTipoColuna(field);

			if (i > 0)
				sb.append(",");

			sb.append("\n\t").append(nomeColuna).append(' ').append(tipoColuna);
		}
		
		return sb;
	}
	
	private StringBuilder getColunaInsert(Class<?> cl) {
		StringBuilder sb = new StringBuilder();	
		
		Field[] atributos = cl.getDeclaredFields();
		
		for (int i = 0; i < atributos.length; i++) {
			Field field = atributos[i];

			String nomeColuna = getNomeColuna(field);

			if (i > 0)
				sb.append(",");

			sb.append(nomeColuna);
		}
		
		return sb;		
	}
	
	private StringBuilder getColunaUpdate(Class<?> cl) {
		StringBuilder sb = new StringBuilder();	
		
		Field[] atributos = cl.getDeclaredFields();
		
		for (int i = 0; i < atributos.length; i++) {
			Field field = atributos[i];

			String nomeColuna = getNomeColuna(field);

			if (i > 0)
				sb.append(",\n\t");

			sb.append(nomeColuna).append(" = ").append("?");
		}
		
		return sb;		
	}	
	
	private StringBuilder getPrimaryKey(Class<?> cl) {
		StringBuilder sb = new StringBuilder();
		
		Field[] atributos = cl.getDeclaredFields();
		
		for (int i = 0, achou = 0; i < atributos.length; i++) {
			Field field = atributos[i];

			if (field.isAnnotationPresent(Coluna.class)) {
				Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

				if (anotacaoColuna.pk()) {
					if (achou > 0)
						sb.append(", ");

					if (anotacaoColuna.nome().isEmpty())
						sb.append(field.getName().toUpperCase());
					else
						sb.append(anotacaoColuna.nome());

					achou++;
				}
			}
		}

		return sb;
	}
	
	public String getCreateTable(Class<?> cl) {
		try {
			StringBuilder sb = new StringBuilder();

			String nomeTabela = getNomeTabela(cl);
			sb.append("CREATE TABLE ").append(nomeTabela).append(" (");
			sb.append(getColunaCreate(cl).toString());
			sb.append(",\n\tPRIMARY KEY( ");
			sb.append(getPrimaryKey(cl).toString());
			sb.append(" )");
			sb.append("\n);");			

			return sb.toString();
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	private PreparedStatement getPreparedStatement(Connection con, Object obj, String sql, Class<?> cl) {
		Field[] atributos = cl.getDeclaredFields();
		
		PreparedStatement ps = null;	
		try {
			ps = con.prepareStatement(sql);

			for (int i = 0; i < atributos.length; i++) {
				Field field = atributos[i];

				field.setAccessible(true);

				if (field.getType().equals(int.class))
					ps.setInt(i + 1, field.getInt(obj));
				else if (field.getType().equals(String.class))
					ps.setString(i + 1, String.valueOf(field.get(obj)));
				else if (field.getType().equals(BigDecimal.class)) {
					ps.setBigDecimal(i + 1, new BigDecimal(String.valueOf(field.get(obj))));
				}
				else
					throw new RuntimeException("Tipo não suportado, falta implementar.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return ps;		
	}

	public PreparedStatement getPreparedStatementForInsert(Connection con, Object obj) {
		Class<? extends Object> cl = obj.getClass();

		StringBuilder sb = new StringBuilder();
		
		String nomeTabela = getNomeTabela(cl);
		sb.append("INSERT INTO ").append(nomeTabela).append(" (");
		sb.append(getColunaInsert(cl).toString());
		sb.append(") VALUES (");
		
		Field[] atributos = cl.getDeclaredFields();
		
		for (int i = 0; i < atributos.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append('?');
		}
		sb.append(')');		

		String strSql = sb.toString();
		System.out.println("SQL GERADO: " + strSql);

		PreparedStatement ps = getPreparedStatement(con, obj, strSql, cl);
		return ps;
	}
	
	public PreparedStatement getPreparedStatementForUpdate(Connection con, Object obj) {
		Class<? extends Object> cl = obj.getClass();

		StringBuilder sb = new StringBuilder();
		
		String nomeTabela = getNomeTabela(cl);
		sb.append("UPDATE ").append(nomeTabela).append(" SET\n\t");
		sb.append(getColunaUpdate(cl).toString());
		sb.append("\nWHERE\n\t");
		
		Field[] atributos = cl.getDeclaredFields();
		
		for (int i = 0, achou = 0; i < atributos.length; i++) {
			Field field = atributos[i];

			if (field.isAnnotationPresent(Coluna.class)) {
				Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

				if (anotacaoColuna.pk()) {
					if (achou > 0)
						sb.append("AND\n\t ");

					if (anotacaoColuna.nome().isEmpty())
						sb.append(field.getName().toUpperCase()).append(" = ").append("?");
					else
						sb.append(anotacaoColuna.nome()).append(" = ").append("?");

					achou++;
				}
			}
		}		

		String strSql = sb.toString();
		System.out.println("SQL GERADO: " + strSql);

		PreparedStatement ps = getPreparedStatement(con, obj, strSql, cl);
		return ps;
	}
	
	public Execute() {
		String strCreateTable = getCreateTable(Vendedor.class);
		System.out.println(strCreateTable);
		
		BigDecimal comissao = new BigDecimal(10);

		Vendedor vendedor = new Vendedor(1, "vendedor", comissao);

		Connection con = null;
		try {
			con = new ConexaoFalsa();

			PreparedStatement ps = getPreparedStatementForInsert(con, vendedor);
			ps.executeUpdate();
			
			PreparedStatement ps1 = getPreparedStatementForUpdate(con, vendedor);
			ps1.executeUpdate();

			ps.close();
			ps1.close();
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public static void main(String[] args) {

		new Execute();

	}	
}
