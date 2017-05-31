package br.com.dasa.relatoriousuario.model;

public class ItemPorEspecialidade {
	private String data;
	private String especialidade;
	private Double valor;
	private Double desconto;
	private Double valorDesconto;
	private String convertido;
	private Double valorNaoConvertido;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Double getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public String getConvertido() {
		return convertido;
	}

	public void setConvertido(String convertido) {
		this.convertido = convertido;
	}

	public Double getValorNaoConvertido() {
		return valorNaoConvertido;
	}

	public void setValorNaoConvertido(Double valorNaoConvertido) {
		this.valorNaoConvertido = valorNaoConvertido;
	}
}