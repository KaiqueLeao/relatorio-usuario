package br.com.dasa.relatoriousuario.model;

public class AgendaOrcamento{
	
	private String codAgenda; 
	private String agData; 
	private String agHora; 
	private String dataLigacao; 
	private String horaLigacao; 
	private String usuarioCodigo; 
	private String exameCodigo;
	private String unidadeCodigo;
	private String pacienteCodigo;
		
	public String getCodAgenda() {
		return codAgenda;
	}
	public void setCodAgenda(String codAgenda) {
		this.codAgenda = codAgenda;
	}
	public String getAgData() {
		return agData;
	}
	public void setAgData(String agData) {
		this.agData = agData;
	}
	public String getAgHora() {
		return agHora;
	}
	public void setAgHora(String agHora) {
		this.agHora = agHora;
	}
	public String getDataLigacao() {
		return dataLigacao;
	}
	public void setDataLigacao(String dataLigacao) {
		this.dataLigacao = dataLigacao;
	}
	public String getHoraLigacao() {
		return horaLigacao;
	}
	public void setHoraLigacao(String horaLigacao) {
		this.horaLigacao = horaLigacao;
	}
	public String getUsuarioCodigo() {
		return usuarioCodigo;
	}
	public void setUsuarioCodigo(String usuarioCodigo) {
		this.usuarioCodigo = usuarioCodigo;
	}
	public String getExameCodigo() {
		return exameCodigo;
	}
	public void setExameCodigo(String exameCodigo) {
		this.exameCodigo = exameCodigo;
	}
	public String getUnidadeCodigo() {
		return unidadeCodigo;
	}
	public void setUnidadeCodigo(String unidadeCodigo) {
		this.unidadeCodigo = unidadeCodigo;
	}
	public String getPacienteCodigo() {
		return pacienteCodigo;
	}
	public void setPacienteCodigo(String pacienteCodigo) {
		this.pacienteCodigo = pacienteCodigo;
	}
	
}
