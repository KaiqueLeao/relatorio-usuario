package br.com.dasa.relatoriousuario.model;

public class Exame {
	
	private String exameCodigo;
	private String convenioCodigo;
	private String unidade;
	private String quantidade;
	private Double valorUnitario;
	private Double percentualDesconto;
	private Double valor;

	public String getExameCodigo() {
		return exameCodigo;
	}

	public void setExameCodigo(String exameCodigo) {
		this.exameCodigo = exameCodigo;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getConvenioCodigo() {
		return convenioCodigo;
	}

	public void setConvenioCodigo(String convenioCodigo) {
		this.convenioCodigo = convenioCodigo;
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Double getPercentualDesconto() {
		return percentualDesconto;
	}

	public void setPercentualDesconto(Double percentualDesconto) {
		this.percentualDesconto = percentualDesconto;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unidade == null) ? 0 : unidade.hashCode());
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
		Exame other = (Exame) obj;
		if (unidade == null) {
			if (other.unidade != null)
				return false;
		} else if (!unidade.equals(other.unidade))
			return false;
		return true;
	}

}