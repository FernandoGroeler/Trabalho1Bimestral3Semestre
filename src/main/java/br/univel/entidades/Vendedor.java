package br.univel.entidades;

import br.univel.anotacao.Tabela;
import br.univel.anotacao.Coluna;
import java.math.BigDecimal;

@Tabela("vendedor")
public class Vendedor {
	@Coluna(pk=true, nome="idvendedor", label="Código")
	private int idVendedor;
	
	@Coluna(nome="nome", label="Nome", max=30, obrigatorio=true)
	private String nome;
	
	@Coluna(nome="percentualcomissao", label="% Comissão", obrigatorio=true)
	private BigDecimal percentualComissao;
	
	public int getIdVendedor() {
		return idVendedor;
	}

	public void setIdVendedor(int idVendedor) {
		this.idVendedor = idVendedor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPercentualComissao() {
		return percentualComissao;
	}

	public void setPercentualComissao(BigDecimal percentualComissao) {
		this.percentualComissao = percentualComissao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idVendedor;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((percentualComissao == null) ? 0 : percentualComissao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vendedor other = (Vendedor) obj;
		if (idVendedor != other.idVendedor)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (percentualComissao == null) {
			if (other.percentualComissao != null)
				return false;
		} else if (!percentualComissao.equals(other.percentualComissao))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vendedor [idVendedor=" + idVendedor + ", nome=" + nome + ", percentualComissao=" + percentualComissao + "]";
	}
}
