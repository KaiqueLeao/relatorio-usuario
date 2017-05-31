package br.com.dasa.relatoriousuario.model;

import org.hibernate.validator.constraints.NotBlank;

public class RelatorioForm {
	
	@NotBlank(message = "Data inicio obrigatória.")
	private String dataInicio;
	
	@NotBlank(message = "Data final obrigatória.")
	private String dataFinal;
	
	@NotBlank(message = "Marca obrigatória.")
	private String marca;
	
	public String getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
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
