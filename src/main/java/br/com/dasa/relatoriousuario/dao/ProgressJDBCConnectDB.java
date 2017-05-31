package br.com.dasa.relatoriousuario.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.dasa.relatoriousuario.model.AgendaOrcamento;
import br.com.dasa.relatoriousuario.model.DataSource;
import br.com.dasa.relatoriousuario.model.InformacaoLigacao;
import br.com.dasa.relatoriousuario.model.Orcamento;
import br.com.dasa.relatoriousuario.model.UsuarioGrupoOrcamento;
import br.com.dasa.relatoriousuario.utils.ConnectionManager;

/**
 * @author t75381419449
 *
 */
public class ProgressJDBCConnectDB {

	public final static Logger logger = Logger.getLogger(ProgressJDBCConnectDB.class);
	
	
	/**
	 * @param url
	 * @param user
	 * @param passw
	 * @param sql
	 * @return
	 */
	public static List<UsuarioGrupoOrcamento> listarUsuarios(DataSource dataSource, String sql){
		
		
		/**
		 * Efetua conexão com o banco de dados 
		 * recebendo como parametro o DataSource
		 */
		Connection conn = ConnectionManager.getConnection(dataSource);
		Statement stmt = null;
		ResultSet resultSet = null;

		List<UsuarioGrupoOrcamento> listUsuarioOrcamento = new ArrayList<UsuarioGrupoOrcamento>();
		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql);
			while (resultSet.next()) {
				UsuarioGrupoOrcamento usuarioGrupoOrcamento = new UsuarioGrupoOrcamento();
				usuarioGrupoOrcamento.setUsuarioCodigo(resultSet.getString("Usuario-Codigo"));
				listUsuarioOrcamento.add(usuarioGrupoOrcamento);
			}
		}  catch(Exception e) {
			System.out.println("Erro: " + e.getMessage() + "\n SQL: " + sql);
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (Exception e) {
				System.out.println("Erro: " + e.getMessage());
			}
		}
		return listUsuarioOrcamento;
	}
	
	/**
	 * @param url
	 * @param user
	 * @param passw
	 * @param sql
	 * @return
	 */
	public static List<Orcamento> listarOrcamentosNaoEfetivados(DataSource dataSource, String sqlOrcamentosNaoEfetivados){
		Connection conn = ConnectionManager.getConnection(dataSource);
		Statement stmt = null;;
		ResultSet resultSet = null;
		List<Orcamento> listOrcamentoNaoEfetivado = new ArrayList<Orcamento>(); 
		
		try {
			Class.forName ("com.ddtek.jdbc.openedge.OpenEdgeDriver");
		} catch(Exception ex) {
			System.out.println("Erro: " + ex.getMessage());
		}

		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sqlOrcamentosNaoEfetivados);
		
			if(resultSet != null){
				while (resultSet.next()) {
					Orcamento orcamentoNaoEfetivado = new Orcamento();
					orcamentoNaoEfetivado.setPacienteCodigo(resultSet.getString("pacienteCodigo"));
					orcamentoNaoEfetivado.setUsuarioCodigo(resultSet.getString("usuarioCodigo"));
					orcamentoNaoEfetivado.setDataLigacao(resultSet.getString("DataLigacao"));
					orcamentoNaoEfetivado.setConvenioCodigo(resultSet.getString("convenioCodigo"));					
					orcamentoNaoEfetivado.setValorRDI(resultSet.getDouble("ValorRDI"));
					orcamentoNaoEfetivado.setValorAC(resultSet.getDouble("valorAC"));
					listOrcamentoNaoEfetivado.add(orcamentoNaoEfetivado);
				}
			}
		} catch(Exception e) {
			System.out.println("Erro: " + e.getMessage() + "\n SQL: " + sqlOrcamentosNaoEfetivados);
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (Exception e) {

			}
		}
		return listOrcamentoNaoEfetivado;
	}
	
	/**
	 * @param url
	 * @param user
	 * @param passw
	 * @param sql
	 * @return
	 */
	public static List<AgendaOrcamento> consultarAgendaOrcamento(DataSource dataSource, String sqlAgendaOrcamento){
		Connection conn = ConnectionManager.getConnection(dataSource); 
		Statement stmt = null;;
		ResultSet resultSet = null;
		List<AgendaOrcamento> listAgendaOrcamento = new ArrayList<AgendaOrcamento>(); 

		try {
			Class.forName ("com.ddtek.jdbc.openedge.OpenEdgeDriver");
		} catch(Exception ex) {
			System.out.println("Erro: " + ex.getMessage());
		}

		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sqlAgendaOrcamento);
		
			if(resultSet != null){
				while (resultSet.next()) {
					AgendaOrcamento agendaOrcamento = new AgendaOrcamento();
					agendaOrcamento.setPacienteCodigo(resultSet.getString("pacienteCodigo"));
					agendaOrcamento.setExameCodigo(resultSet.getString("exameCodigo"));
					agendaOrcamento.setDataLigacao(resultSet.getString("DataLigacao"));
					agendaOrcamento.setUsuarioCodigo(resultSet.getString("usuarioCodigo"));
					agendaOrcamento.setAgData(resultSet.getString("agData"));
					listAgendaOrcamento.add(agendaOrcamento);
				}
			}
		} catch(Exception e) {
			System.out.println("Erro: " + e.getMessage() + "\n SQL: " + sqlAgendaOrcamento);
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (Exception e) {

			}
		}
		return listAgendaOrcamento;
	}
	
	/**
	 * @param url
	 * @param user
	 * @param passw
	 * @param sqlInformacaoLigacao
	 * @return
	 */
	public static List<InformacaoLigacao> listarInformacaoLigacao(DataSource dataSource, String sqlInformacaoLigacao, String dataLigacao){
		Connection conn = ConnectionManager.getConnection(dataSource);
		Statement stmt = null;;
		ResultSet resultSet = null;
		List<InformacaoLigacao> listInformacaoLigacao = new ArrayList<InformacaoLigacao>(); 

		try {
			Class.forName ("com.ddtek.jdbc.openedge.OpenEdgeDriver");
		} catch(Exception ex) {
			System.out.println("Erro: " + ex.getMessage());
		}

		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sqlInformacaoLigacao);
		
			if(resultSet != null){
				while (resultSet.next()) {
					InformacaoLigacao informacaoLigacao = new InformacaoLigacao();
					informacaoLigacao.setPacienteCodigo(resultSet.getString("pacienteCodigo"));
					informacaoLigacao.setInfData(resultSet.getString("infData"));
					informacaoLigacao.setInfHora(resultSet.getString("infHora"));
					informacaoLigacao.setInfValorTaxa(resultSet.getString("infValor"));
					informacaoLigacao.setUsuarioCodigo(resultSet.getString("usuarioCodigo"));
					informacaoLigacao.setExameAn(resultSet.getString("exameAn"));
					informacaoLigacao.setExameEsp(resultSet.getString("exameEsp"));
					informacaoLigacao.setInfComentarios(resultSet.getString("infComentarios"));
					
					if(!informacaoLigacao.getPacienteCodigo().equals("null")){
						listInformacaoLigacao.add(informacaoLigacao);
					}
					
				}
			}
		} catch(Exception e) {
			System.out.println("Erro: " + e.getMessage() + "\n SQL: " + sqlInformacaoLigacao);
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (Exception e) {

			}
		}
		return listInformacaoLigacao;
	}
	
	public static List<InformacaoLigacao> buscarInformacaoLigacao(DataSource dataSource, String sqlInformacaoLigacao){
		Connection conn = ConnectionManager.getConnection(dataSource);
		Statement stmt = null;;
		ResultSet resultSet = null;
		List<InformacaoLigacao> listInformacaoLigacao = new ArrayList<InformacaoLigacao>(); 

		try {
			Class.forName ("com.ddtek.jdbc.openedge.OpenEdgeDriver");
		} catch(Exception ex) {
			System.out.println("Erro: " + ex.getMessage());
		}

		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sqlInformacaoLigacao);
		
			if(resultSet != null){
				while (resultSet.next()) {
					InformacaoLigacao informacaoLigacao = new InformacaoLigacao();
					informacaoLigacao.setInfData(resultSet.getString("dataLigacao"));
					informacaoLigacao.setUsuarioCodigo(resultSet.getString("usuarioCodigo"));
					informacaoLigacao.setPacienteCodigo(resultSet.getString("pacienteCodigo"));
					informacaoLigacao.setInfComentarios(resultSet.getString("infComentarios"));
					
					if(!informacaoLigacao.getPacienteCodigo().equals("null")){
						listInformacaoLigacao.add(informacaoLigacao);
					}
					
				}
			}
		} catch(Exception e) {
			System.out.println("Erro: " + e.getMessage() + "\n SQL: " + sqlInformacaoLigacao);
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (Exception e) {

			}
		}
		return listInformacaoLigacao;
	}
	
	// SQL's
	/**
	 * @return
	 */
	public static String usuariosOrcamentoSQL() {
		String sqlUsuariosOrcamento = "select * from pub.usuario_grupo where \"Grupo-codigo\" = 'orcamento'";
		return sqlUsuariosOrcamento;
	}
	
	/**
	 * @param dataInicial
	 * @param dataFinal
	 * @param marca
	 * @return
	 */
	public static String orcamentoNaoEfetivadoSQL(String dataInicial, String dataFinal, String marca) {
		String sqlOrcamentosNaoEfetivados =  "SELECT ao.\"paciente-codigo\" as pacienteCodigo, ao.DataLigacao, ao.HoraLigacao, \"rc-agendaOrcamento\", "
						+ "o.dataLigacao,o.\"laboratorio-codigo\",o.\"unidade-codigo\" as unidadeCodigo, o.margemAcerto, o.dataAtend, o.horaAtend, \"Convenio-Codigo\" as convenioCodigo, " 
						+ "o.\"usuario-codigo\" as usuarioCodigo, substr(o.comentOrcamento,1,4000), o.codClassifOrc, substr(o.resultadoContato,1,4000), "
						+ "o.valorAC, o.ValorRDI FROM \"PUB\".orcNaoEfetivo o "
						+ "join pub.AgendaOrcamento ao on ao.rowid = o.\"rc-agendaOrcamento\" "
						+ "where o.\"Laboratorio-codigo\" = " + "'" + marca + "' " + "and o.\"usuario-Codigo\" != '' "
						+ "and o.dataLigacao > " + "'" + dataInicial + "'" + " and o.dataLigacao < " + "'" + dataFinal + "'";  
		return sqlOrcamentosNaoEfetivados;
	}
	
	/**
	 * @return
	 */
	public static String informacoesLigacoesSQL(String pacienteCodigo, List<UsuarioGrupoOrcamento> listUsuariosOrcamento) {
		String listusuarioOrcamentoIDs = getUsuariosOrcamentoIDs(listUsuariosOrcamento);
		String sqlInformacoesLigacoes = "SELECT  \"Usuario-Codigo\" as usuarioCodigo, \"inf-data\" as dataLigacao, \"inf-hora\",\"Paciente-Codigo\" as pacienteCodigo,substr(\"inf-comentarios\",1,4000) as infComentarios,\"Int-tipo\", " +
				"substr(\"exame-esp\",1,400), substr(\"exame-An\",1,4000),\"inf-ciente-preparo\",\"inf-valor\",\"inf-valor-taxa\" " +
				"FROM \"PUB\".\"Informacao_ligacoes\" " +
				"where \"Paciente-Codigo\" = " + pacienteCodigo  + " " +
				"AND substr(\"inf-comentarios\",1,4000) LIKE " + "'%ORÇAMENTO%'" + " " +
				"and \"Usuario-Codigo\" NOT IN (" + listusuarioOrcamentoIDs + ") " +
				"order by \"inf-data\" desc, \"inf-hora\" desc " +
				"with (NOLOCK)";
		return sqlInformacoesLigacoes;
	}
	
	/**
	 * @return
	 */
	public static String informacoesLigacoesSQLOrcAgendados(String pacienteCodigo, String usuarioCodigo, String dataLigacao) {
		String sqlInformacoesLigacoes = "SELECT  \"Usuario-Codigo\" as usuarioCodigo, \"inf-data\" as infData, \"inf-hora\" as infHora ,\"inf-hora\",\"Paciente-Codigo\" as pacienteCodigo,substr(\"inf-comentarios\",1,4000) as infComentarios,\"Int-tipo\", " +
				"substr(\"exame-esp\",1,400) as exameEsp, substr(\"exame-An\",1,4000) as exameAn,\"inf-ciente-preparo\",\"inf-valor\" as infValor, \"inf-valor-taxa\" " +
				"FROM \"PUB\".\"Informacao_ligacoes\" " +
				"where \"Paciente-Codigo\" = " + pacienteCodigo  + " " +
				"AND usuarioCodigo = " + usuarioCodigo + " " +
				"AND infData = " + "'" + dataLigacao + "'" + " " +
				"AND substr(\"inf-comentarios\",1,4000) LIKE " + "'%ORÇAMENTO%'" + " " +
				"order by \"inf-data\" desc, \"inf-hora\" desc " +
				"with (NOLOCK)";
		return sqlInformacoesLigacoes;
	}
	
	/**
	 * @param listUsuariosOrcamento
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String getUsuariosOrcamentoIDs(List<UsuarioGrupoOrcamento> listUsuariosOrcamento){
		StringBuilder sbUsuariosIDs = new StringBuilder();
		Double valor;
		for (Iterator<UsuarioGrupoOrcamento> iterator = listUsuariosOrcamento.iterator(); iterator.hasNext();) {
			UsuarioGrupoOrcamento usuarioGrupoOrcamento = (UsuarioGrupoOrcamento) iterator.next();
			   try {
				    valor = (Double.parseDouble(usuarioGrupoOrcamento.getUsuarioCodigo()));
				    sbUsuariosIDs.append(",").append(usuarioGrupoOrcamento.getUsuarioCodigo());
				} catch (NumberFormatException e) {  
			       
				}
			
		}
		String retorno = sbUsuariosIDs.toString().replaceFirst(",","");
		return retorno;
	}
	
	/**
	 * @return
	 */
	public static String agendaOrcamentoSQL(String pacienteCodigo, String dataLigacao, String usuarioCodigo, List<UsuarioGrupoOrcamento> listUsuarioOrcamentoID) {
		String listusuarioOrcamentoIDs = getUsuariosOrcamentoIDs(listUsuarioOrcamentoID);
		String sqlAgendaOrcamento = "select \"cod-agenda\", \"Ag-Data\" as agData, \"Ag-Hora\", \"Paciente-Codigo\" as pacienteCodigo , " + 
									"DataLigacao, HoraLigacao, \"usuario-codigo\" as usuarioCodigo, \"Exame-Codigo\" as exameCodigo " +    
									"from pub.Agenda " +
									"where \"Paciente-Codigo\" " + "=" + pacienteCodigo + " " + 
									"and agData >= " + "'" + dataLigacao + "'" + " " +
									"and usuarioCodigo IN (" + listusuarioOrcamentoIDs + ")";
		return sqlAgendaOrcamento;
	}

}
