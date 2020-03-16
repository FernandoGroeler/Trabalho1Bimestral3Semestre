package br.univel.comum;

public class ConexaoPostgreSQL extends Conexao {

	@Override
	public void conectar() {
		setUrl("jdbc:postgresql://localhost:5432/TrabalhoBimestral");
		setUsuario("postgres");
		setSenha("12345");
		setDriver("org.postgresql.Driver");
		super.conectar();
	}

}
