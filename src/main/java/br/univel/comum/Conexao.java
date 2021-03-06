package br.univel.comum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
	private Connection con;
	private String url;
	private String usuario;
	private String senha;
	private String driver;
	
	public Connection getConnection() {
		if (con == null)
			conectar();
		
		return con;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getDriver() {
		return driver;
	}
	
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public void conectar() {
		try {
			Class.forName(this.driver);
			con = DriverManager.getConnection(this.url, this.usuario, this.senha);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	public void desconectar() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
