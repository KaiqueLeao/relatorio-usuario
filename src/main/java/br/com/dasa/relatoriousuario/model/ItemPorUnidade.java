package br.com.dasa.relatoriousuario.model;

import java.util.List;

public class ItemPorUnidade {

	private String unidadeCodigo;
	private String unidadeNome;
	private List<ItemPorUnidadeItem> itens;

	public String getUnidadeCodigo() {
		return unidadeCodigo;
	}

	public void setUnidadeCodigo(String unidadeCodigo) {
		this.unidadeCodigo = unidadeCodigo;
	}

	public String getUnidadeNome() {
		return unidadeNome;
	}

	public void setUnidadeNome(String unidadeNome) {
		this.unidadeNome = unidadeNome;
	}

	public List<ItemPorUnidadeItem> getItens() {
		return itens;
	}

	public void setItens(List<ItemPorUnidadeItem> itens) {
		this.itens = itens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unidadeCodigo == null) ? 0 : unidadeCodigo.hashCode());
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
		ItemPorUnidade other = (ItemPorUnidade) obj;
		if (unidadeCodigo == null) {
			if (other.unidadeCodigo != null)
				return false;
		} else if (!unidadeCodigo.equals(other.unidadeCodigo))
			return false;
		return true;
	}
	
	
}