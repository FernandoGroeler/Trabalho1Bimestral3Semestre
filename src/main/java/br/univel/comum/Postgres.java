package br.univel.comum;

public class Postgres extends BancoDados {

	@Override
	void conectar() {
		setUrl("jdbc:postgresql://localhost:5432/TrabalhoBimestral");
		setUsuario("postgres");
		setSenha("pr4gr1m1d4r");
		setDriver("org.postgresql.Driver");
		super.conectar();
	}

}
