package br.com.dasa.relatoriousuario.model;

import java.util.List;

public class RelatorioPositivacao {

	private String possibilidadeGanho;
	private String mediaDesconto;
	private String ganho;
	private String valorNaoConvertido;
	private String mediaNaoConversao;

	// PRIMEIRA ABA => Operador
	private List<ItemPorOperador> itensPorOperador;

	// SEGUNDA ABA => Unidade
	private List<ItemPorUnidade> itensPorUnidade;

	// TERCEIRA ABA => Especialidade
	private List<ItemPorEspecialidade> itemPorEspecialidade;
	private String dataInicial;
	private String dataFinal;
	private String marca;

	public String getPossibilidadeGanho() {
		return possibilidadeGanho;
	}

	public void setPossibilidadeGanho(String possibilidadeGanho) {
		this.possibilidadeGanho = possibilidadeGanho;
	}

	public String getMediaDesconto() {
		return mediaDesconto;
	}

	public void setMediaDesconto(String mediaDesconto) {
		this.mediaDesconto = mediaDesconto;
	}

	public String getGanho() {
		return ganho;
	}

	public void setGanho(String ganho) {
		this.ganho = ganho;
	}

	public String getValorNaoConvertido() {
		return valorNaoConvertido;
	}

	public void setValorNaoConvertido(String valorNaoConvertido) {
		this.valorNaoConvertido = valorNaoConvertido;
	}

	public String getMediaNaoConversao() {
		return mediaNaoConversao;
	}

	public void setMediaNaoConversao(String mediaNaoConversao) {
		this.mediaNaoConversao = mediaNaoConversao;
	}

	public List<ItemPorOperador> getItensPorOperador() {
		return itensPorOperador;
	}

	public void setItensPorOperador(List<ItemPorOperador> itensPorOperador) {
		this.itensPorOperador = itensPorOperador;
	}

	public List<ItemPorUnidade> getItensPorUnidade() {
		return itensPorUnidade;
	}

	public void setItensPorUnidade(List<ItemPorUnidade> itensPorUnidade) {
		this.itensPorUnidade = itensPorUnidade;
	}

	public List<ItemPorEspecialidade> getItemPorEspecialidade() {
		return itemPorEspecialidade;
	}

	public void setItemPorEspecialidade(List<ItemPorEspecialidade> itemPorEspecialidade) {
		this.itemPorEspecialidade = itemPorEspecialidade;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}
}