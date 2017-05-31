package br.com.dasa.relatoriousuario.model;

import java.util.List;

public class InformacaoLigacao {

	private String usuarioCodigo;
	private String infData;
	private String infHora;
	private String pacienteCodigo;
	private String infComentarios;
	private String intTipo;
	private String exameEsp;
	private String exameAn;
	private String infCientePreparo;
	private String infValor;
	private String infValorTaxa;
	private String unidadeCodigo;
	private String dataLigacaoOrc;
	private List<Exame> exames;
	private List<Orcamento> orcamentos;

	public String getUsuarioCodigo() {
		return usuarioCodigo;
	}

	public void setUsuarioCodigo(String usuarioCodigo) {
		this.usuarioCodigo = usuarioCodigo;
	}

	public String getInfData() {
		return infData;
	}

	public void setInfData(String infData) {
		this.infData = infData;
	}

	public String getInfHora() {
		return infHora;
	}

	public void setInfHora(String infHora) {
		this.infHora = infHora;
	}

	public String getPacienteCodigo() {
		return pacienteCodigo;
	}

	public void setPacienteCodigo(String pacienteCodigo) {
		this.pacienteCodigo = pacienteCodigo;
	}

	public String getInfComentarios() {
		return infComentarios;
	}

	public void setInfComentarios(String infComentarios) {
		this.infComentarios = infComentarios;
	}

	public String getIntTipo() {
		return intTipo;
	}

	public void setIntTipo(String intTipo) {
		this.intTipo = intTipo;
	}

	public String getExameEsp() {
		return exameEsp;
	}

	public void setExameEsp(String exameEsp) {
		this.exameEsp = exameEsp;
	}

	public String getExameAn() {
		return exameAn;
	}

	public void setExameAn(String exameAn) {
		this.exameAn = exameAn;
	}

	public String getInfCientePreparo() {
		return infCientePreparo;
	}

	public void setInfCientePreparo(String infCientePreparo) {
		this.infCientePreparo = infCientePreparo;
	}

	public String getInfValor() {
		return infValor;
	}

	public void setInfValor(String infValor) {
		this.infValor = infValor;
	}

	public String getInfValorTaxa() {
		return infValorTaxa;
	}

	public void setInfValorTaxa(String infValorTaxa) {
		this.infValorTaxa = infValorTaxa;
	}
	
	public String getUnidadeCodigo() {
		return unidadeCodigo;
	}
	public void setUnidadeCodigo(String unidadeCodigo) {
		this.unidadeCodigo = unidadeCodigo;
	}

	public List<Exame> getExames() {
		return exames;
	}
	
	public String getDataLigacaoOrc() {
		return dataLigacaoOrc;
	}

	public void setDataLigacaoOrc(String dataLigacaoOrc) {
		this.dataLigacaoOrc = dataLigacaoOrc;
	}

	public void setExames(List<Exame> exames) {
		this.exames = exames;
	}

	public List<Orcamento> getOrcamentos() {
		return orcamentos;
	}

	public void setOrcamentos(List<Orcamento> orcamentos) {
		this.orcamentos = orcamentos;
	}

}