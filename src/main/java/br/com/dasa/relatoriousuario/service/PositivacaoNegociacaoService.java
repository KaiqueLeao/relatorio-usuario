package br.com.dasa.relatoriousuario.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import br.com.dasa.relatoriousuario.dao.ProgressJDBCConnectDB;
import br.com.dasa.relatoriousuario.model.AgendaOrcamento;
import br.com.dasa.relatoriousuario.model.DataSource;
import br.com.dasa.relatoriousuario.model.Exame;
import br.com.dasa.relatoriousuario.model.InformacaoLigacao;
import br.com.dasa.relatoriousuario.model.ItemPorEspecialidade;
import br.com.dasa.relatoriousuario.model.ItemPorOperador;
import br.com.dasa.relatoriousuario.model.ItemPorUnidade;
import br.com.dasa.relatoriousuario.model.ItemPorUnidadeItem;
import br.com.dasa.relatoriousuario.model.Orcamento;
import br.com.dasa.relatoriousuario.model.RelatorioPositivacao;
import br.com.dasa.relatoriousuario.model.UsuarioGrupoOrcamento;
import br.com.dasa.relatoriousuario.utils.Constantes;
import br.com.dasa.relatoriousuario.utils.Util;
 
/**
 * @author t75381419449
 *
 */
@Component
public class PositivacaoNegociacaoService {
	
	public final Logger logger = Logger.getLogger(PositivacaoNegociacaoService.class); 

	/**
	 * relatorioPositivacao
	 * @param dataInicial
	 * @param dataFinal
	 * @param marca
	 * @return
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public RelatorioPositivacao relatorioPositivacao(
			String dataInicial, String dataFinal, String marca,HttpServletRequest request) throws Exception {
		DataSource dsParam;
		DataSource dsCentral;
		DataSource dsAgenda;
		try{
			dsParam = getParamDataSource(marca);
			dsCentral = getCentralDataSource(marca);
			dsAgenda = getAgendaDataSource(marca);

			logger.info("Selecionando usuários grupo orçamentos.");
			List<UsuarioGrupoOrcamento> listUsuariosOrcamento = listarUsuariosOrcamento(dsParam);
			
			logger.info("Selecionando orçamentos não efetivados.");
			List<Orcamento> listOrcamentosNaoEfetivados = listarOrcamentosNaoEfetivados(
					dsParam, dataInicial, dataFinal, marca);
			
			logger.info("Consultando agenda de orçamentos não efetivados.");
			List<AgendaOrcamento> listAgendaOrcamentoEfetivados = consultarAgendaEfetivado(
					listOrcamentosNaoEfetivados, listUsuariosOrcamento, dsAgenda);
		
			logger.info("Selecionando informações das ligações.");
			List<InformacaoLigacao> listInformacaoLigacaoEfetivados = listarInformacaoLigacaoAgendados(
					dsCentral, listAgendaOrcamentoEfetivados); 
		
			logger.info("Verificando agendamento das ligações de orçamento.");
			RelatorioPositivacao relatorioPositivacao = verificarOrcamentosRevertidosAgenda(
					listInformacaoLigacaoEfetivados,listAgendaOrcamentoEfetivados, dsAgenda);
			
			return relatorioPositivacao;
		}catch(Exception e){
			logger.error("Erro relatorioPositivacao: " + e.getMessage());
			throw e;
		}
		
	}

	/**
	 * @param marca
	 * @return
	 * @throws IOException
	 */
	private DataSource getAgendaDataSource(String marca) throws IOException {
		DataSource dsAgenda;
		/**
		 * Monta DataSource de acordo com a marca que foi enviada
		 * dsAgenda conexão com banco de dados Agenda
		 */
		dsAgenda = new DataSource();
		dsAgenda = Util.retornaDataSourcesMarca(marca, Constantes.AGENDA);
		return dsAgenda;
	}

	/**
	 * @param marca
	 * @return
	 * @throws IOException
	 */
	private DataSource getCentralDataSource(String marca) throws IOException {
		DataSource dsCentral;
		/**
		 * Monta DataSource de acordo com a marca que foi enviada
		 * dsCentral conexão com banco de dados Central
		 */
		dsCentral = new DataSource();
		dsCentral = Util.retornaDataSourcesMarca(marca, Constantes.CENTRAL);
		return dsCentral;
	}

	/**
	 * @param marca
	 * @return
	 * @throws IOException
	 */
	private DataSource getParamDataSource(String marca) throws IOException {
		DataSource dsParam;
		/**
		 * Monta DataSource de acordo com a marca que foi enviada
		 * dsParam conexão com banco de dados Param
		 */
		dsParam = new DataSource();
		dsParam = Util.retornaDataSourcesMarca(marca, Constantes.PARAM);
		return dsParam;
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
	 * @param url
	 * @param user
	 * @param passw
	 * @param sqlOrcamentosNaoEfetivados
	 * @return
	 * @throws SQLException
	 */
	private List<Orcamento> listarOrcamentosNaoEfetivados(
			DataSource dataSource, String dataInicial, String dataFinal, String marca) throws SQLException {
		String sqlOrcamentosNaoEfetivados = ProgressJDBCConnectDB.orcamentoNaoEfetivadoSQL(dataInicial, dataFinal, marca);
		List<Orcamento> listOrcamentosNaoEfetivados = ProgressJDBCConnectDB.listarOrcamentosNaoEfetivados(
				dataSource, sqlOrcamentosNaoEfetivados);
		return listOrcamentosNaoEfetivados;
	}

	/**
	 * @param url
	 * @param user
	 * @param passw
	 * @param sqlInformacoesLigacoes
	 * @return
	 * @throws SQLException
	 */
//	private List<InformacaoLigacao> listarInformacaoLigacao(
//			DataSource dataSource, List<Orcamento> listOrcamentoNaoEfetivado, 
//			List<UsuarioGrupoOrcamento> listUsuariosOrcamento) throws SQLException {
//		// lista de informação da ligacao
//		List<InformacaoLigacao> listInformacaoLigacao = new ArrayList<InformacaoLigacao>();
//		List<InformacaoLigacao> listInformacaoLigacaoExames = new ArrayList<InformacaoLigacao>(); 
//		for (Iterator<Orcamento> iterator = listOrcamentoNaoEfetivado.iterator(); iterator.hasNext();) {
//			Orcamento orcamentoNaoEfetivado = (Orcamento) iterator.next();
//			String pacienteCodigo = orcamentoNaoEfetivado.getPacienteCodigo();
//			if(!pacienteCodigo.equals("null")){
//				String sqlInformacaoLigacao = ProgressJDBCConnectDB.informacoesLigacoesSQL(pacienteCodigo, listUsuariosOrcamento); 
//				listInformacaoLigacao = ProgressJDBCConnectDB.listarInformacaoLigacao(dataSource, sqlInformacaoLigacao, orcamentoNaoEfetivado.getDataLigacao());
//				if(listInformacaoLigacao != null && listInformacaoLigacao.size() > 0){
//					for (Iterator<InformacaoLigacao> iteratorExames = listInformacaoLigacao.iterator(); iteratorExames.hasNext();) {
//						if(iteratorExames.hasNext()){
//							InformacaoLigacao informacaoLigacao = (InformacaoLigacao) iteratorExames.next();
//							String infComentarios = informacaoLigacao.getInfComentarios();
//							List<Exame> listaExamesPaciente = lerInfComentarios(infComentarios, orcamentoNaoEfetivado);
//							if(listaExamesPaciente != null && listaExamesPaciente.size() > 0){
//								informacaoLigacao.setExames(listaExamesPaciente);
//								listInformacaoLigacaoExames.add(informacaoLigacao);
//							}
//						}
//					}
//				}
//			}
//		}
		// ler o campo inf-Comentarios
//		if(listInformacaoLigacao != null && listInformacaoLigacao.size() > 0){
//			for (Iterator<InformacaoLigacao> iteratorExames = listInformacaoLigacao.iterator(); iteratorExames.hasNext();) {
//				if(iteratorExames.hasNext()){
//					InformacaoLigacao informacaoLigacao = (InformacaoLigacao) iteratorExames.next();
//					String infComentarios = informacaoLigacao.getInfComentarios();
//					List<Exame> listaExamesPaciente = lerInfComentarios(infComentarios);
//					if(listaExamesPaciente != null && listaExamesPaciente.size() > 0){
//						informacaoLigacao.setExames(listaExamesPaciente);
//						listInformacaoLigacaoExames.add(informacaoLigacao);
//					}
//					logger.info("Coletando exames do paciente: " + informacaoLigacao.getPacienteCodigo());
//				}
//			}
//		}
//		return listInformacaoLigacaoExames;
//	}
	
	
	/**
	 * Lista informações das ligações de orçamentos agendados.
	 * @param dataSource
	 * @param listOrcamentoNaoEfetivado
	 * @param listUsuariosOrcamento
	 * @return
	 * @throws SQLException
	 */
	private List<InformacaoLigacao> listarInformacaoLigacaoAgendados(
			DataSource dataSource, List<AgendaOrcamento> listOrcamentoEfetivados) throws SQLException {
		List<InformacaoLigacao> listInformacaoLigacao = new ArrayList<InformacaoLigacao>();
		List<InformacaoLigacao> listInformacaoLigacaoExames = new ArrayList<InformacaoLigacao>(); 
		for (Iterator<AgendaOrcamento> iterator = listOrcamentoEfetivados.iterator(); iterator.hasNext();) {
			AgendaOrcamento agendaOrcamento = (AgendaOrcamento) iterator.next();
			if(!agendaOrcamento.getPacienteCodigo().equals("null")){
				String sqlInformacaoLigacao = ProgressJDBCConnectDB.informacoesLigacoesSQLOrcAgendados(
						agendaOrcamento.getPacienteCodigo(), agendaOrcamento.getUsuarioCodigo(), agendaOrcamento.getDataLigacao()); 
				listInformacaoLigacao = ProgressJDBCConnectDB.listarInformacaoLigacao(dataSource, sqlInformacaoLigacao, agendaOrcamento.getDataLigacao());
				if(listInformacaoLigacao != null && listInformacaoLigacao.size() > 0){
					for (Iterator<InformacaoLigacao> iteratorInformacaoLigacao = listInformacaoLigacao.iterator(); iteratorInformacaoLigacao.hasNext();) {
						if(iteratorInformacaoLigacao.hasNext()){
							InformacaoLigacao informacaoLigacao = (InformacaoLigacao) iteratorInformacaoLigacao.next();
							String infComentarios = informacaoLigacao.getInfComentarios();
							List<Exame> listaExamesPaciente = lerInfComentarios(infComentarios);
							if(listaExamesPaciente != null && listaExamesPaciente.size() > 0){
								informacaoLigacao.setExames(listaExamesPaciente);
								listInformacaoLigacaoExames.add(informacaoLigacao);
							}
						}
					}
				}
			}
		}
		return listInformacaoLigacaoExames;
	}
	
	/**
	 * @param url
	 * @param user
	 * @param passw
	 * @param sqlUsuariosOrcamento
	 * @return
	 * @throws SQLException
	 */
	private List<UsuarioGrupoOrcamento> listarUsuariosOrcamento(DataSource dataSource) throws SQLException {
		String sqlUsuariosOrcamento = ProgressJDBCConnectDB.usuariosOrcamentoSQL();
		List<UsuarioGrupoOrcamento> listUsuariosOrcamento = 
				ProgressJDBCConnectDB.listarUsuarios(dataSource, sqlUsuariosOrcamento);
		return listUsuariosOrcamento;
	}
	
	/**
	 * @param url
	 * @param user
	 * @param passw
	 * @param dataInicial
	 * @param dataFinal
	 * @param marca
	 * @return
	 * @throws SQLException
	 */
//	private List<AgendaOrcamento> consultarAgendaOrcamentos(
//			DataSource dataSource, String pacienteCodigo, String dataLigacao,List<UsuarioGrupoOrcamento> listUsuariosOrcamento) throws SQLException {
//		String sqlAgendaOrcamento = ProgressJDBCConnectDB.agendaOrcamentoSQL(pacienteCodigo, dataLigacao, listUsuariosOrcamento);
//		List<AgendaOrcamento> listOrcamentosNaoEfetivados = 
//				ProgressJDBCConnectDB.consultarAgendaOrcamento(dataSource, sqlAgendaOrcamento);
//		return listOrcamentosNaoEfetivados;
//	}
	
	/**
	 * @return
	 * @throws SQLException 
	 */
//	private RelatorioPositivacao verificarOrcamentosRevertidosAgenda(
//			List<InformacaoLigacao> listInformacaoLigacao, List<UsuarioGrupoOrcamento> listUsuariosOrcamento, DataSource dataSource) throws SQLException{
//		RelatorioPositivacao relatorioPositivacao = new RelatorioPositivacao();
//		List<AgendaOrcamento> listAgendaOrcamento = new ArrayList<AgendaOrcamento>();
//		List<ItemPorOperador> listItemPorOperador = new ArrayList<ItemPorOperador>();
//		List<ItemPorUnidade> listItemPorUnidade = new ArrayList<ItemPorUnidade>();
//		List<ItemPorEspecialidade> listItemPorEspecialidader = new ArrayList<ItemPorEspecialidade>();
//		int totalProcessados = 0;
//		int totalConvertido = 0;
//		int totalNaoConvertidos = 0;
//		
//		// percorrendo lista de informação da ligação.
//		for (Iterator<InformacaoLigacao> iterator = listInformacaoLigacao.iterator(); iterator.hasNext();) {
//			InformacaoLigacao informacaoLigacao = (InformacaoLigacao) iterator.next();
//			// consultando cada exame da informaçao da ligação na agenda. // TODO passar data inicial
//			listAgendaOrcamento = consultarAgendaOrcamentos(dataSource, informacaoLigacao.getPacienteCodigo(), 
//					informacaoLigacao.getDataLigacaoOrc(), listUsuariosOrcamento);
//			// lista de codigo de exames agendados
//			List<String> listExamesAgendados = listarExamesAgendados(listAgendaOrcamento);
//			if(listAgendaOrcamento.size() > 0){
//				// exames da informação da ligação
//				List<Exame> listExames = informacaoLigacao.getExames();
//				for (Iterator<Exame> iteratorExames = listExames.iterator(); iteratorExames.hasNext();) {
//					Exame exame = (Exame) iteratorExames.next();
//					// verifica quais exames da ligação foram agendados.
//					if(verificarExameAgendado(exame.getExameCodigo(), listExamesAgendados)){
//						logger.info("\n Convertidos " + totalConvertido);
//						setItemOperadorStatusSim(listItemPorOperador, informacaoLigacao, exame);
//						setItemUnidadeStatus(relatorioPositivacao, listItemPorUnidade, informacaoLigacao, exame, "S");
//						setItemEspecialidadeStatusSim(listItemPorEspecialidader, informacaoLigacao, exame);
//					}else{
//						logger.info("\n Nao Convertidos " + totalNaoConvertidos);
//						setItensOperadorStatuNao(listItemPorOperador, informacaoLigacao, exame);
//						setItemUnidadeStatus(relatorioPositivacao, listItemPorUnidade, informacaoLigacao, exame, "N");
//						setItensEspecialidadeStatuNao(listItemPorEspecialidader, informacaoLigacao, exame);
//					}
//				}
//			}else{
//				logger.info("\n Nao Convertidos " + totalNaoConvertidos);
//				List<Exame> listExames = informacaoLigacao.getExames();
//				for (Iterator<Exame> iterator2 = listExames.iterator(); iterator2.hasNext();) {
//					Exame exame = (Exame) iterator2.next();
//					setItensOperadorStatuNao(listItemPorOperador, informacaoLigacao, exame);
//					setItemUnidadeStatus(relatorioPositivacao, listItemPorUnidade, informacaoLigacao, exame, "N");
//					setItensEspecialidadeStatuNao(listItemPorEspecialidader, informacaoLigacao, exame);
//				}
//			}
//			System.out.println("Total processados: " +totalProcessados);
//			totalConvertido++;
//			totalNaoConvertidos++;
//			totalProcessados++;
//		}
//		relatorioPositivacao.setItensPorOperador(listItemPorOperador);
//		relatorioPositivacao.setItemPorEspecialidade(listItemPorEspecialidader);
//		
//		// calcular reumos dos orçamentos.
//		calcularResumosOrcamentos(relatorioPositivacao);
//		return relatorioPositivacao;
//	}
	
	/**
	 * @param relatorioPositivacao
	 */
	@SuppressWarnings("unused")
	private void calcularResumosOrcamentos(RelatorioPositivacao relatorioPositivacao){
		Double possibilidadeDeGanho = 100.0;	
		Double descontos = 0.0;	
		Double qtdeConvertido = 0.0;
		Double qtdeNaoConvertido = 0.0;
		Double ganho = 0.0;	
		Double valorNaoConvertido = 0.0;	
		Integer qtdeOrcamentos = 0;
		Integer qtdeOrcamentosConvertidos = 0;
		
		List<ItemPorOperador> listItemPorOperador = relatorioPositivacao.getItensPorOperador();
		for (Iterator<ItemPorOperador> iterator = listItemPorOperador.iterator(); iterator.hasNext();) {
			ItemPorOperador itemPorOperador = (ItemPorOperador) iterator.next();
			possibilidadeDeGanho += itemPorOperador.getValorNaoConvertido();
			if(itemPorOperador.getConvertido().equals("S")){
				qtdeConvertido++;
				ganho += itemPorOperador.getValor();
				descontos = itemPorOperador.getDesconto();
				qtdeOrcamentos++;
				qtdeOrcamentosConvertidos++;
			}else{
				qtdeNaoConvertido++;
				valorNaoConvertido = itemPorOperador.getValor();
				qtdeOrcamentos++;
			}
		}

		relatorioPositivacao.setPossibilidadeGanho(possibilidadeDeGanho.toString());
		if(descontos > 0 && qtdeOrcamentosConvertidos > 0){
			relatorioPositivacao.setMediaDesconto(String.valueOf(descontos/qtdeOrcamentosConvertidos));
		}else{
			relatorioPositivacao.setMediaDesconto("0");
		}
		relatorioPositivacao.setGanho(ganho.toString());
		relatorioPositivacao.setValorNaoConvertido(valorNaoConvertido.toString());
		relatorioPositivacao.setMediaNaoConversao(String.valueOf((qtdeNaoConvertido/qtdeOrcamentos) * 100));
		
	}
	
	/**
	 * @param exameCodigo
	 * @param listExamesAgendados
	 * @return
	 */
	private static boolean verificarExameAgendado(String exameCodigo, List<String> listExamesAgendados){
		boolean isExameAgendado = false;
		if(listExamesAgendados != null){
			if (listExamesAgendados.contains(exameCodigo)){
				isExameAgendado = true;
			}
		}
		return isExameAgendado;
	}

	/**
	 * @param listItemPorOperador
	 * @param informacaoLigacao
	 * @param exame
	 */
	private static void setItensOperadorStatuNao(List<ItemPorOperador> listItemPorOperador,
			InformacaoLigacao informacaoLigacao, Exame exame) {
		// exames não convertidos Convertidos (N)
		ItemPorOperador itemPorOperador = new ItemPorOperador();
		itemPorOperador.setData(informacaoLigacao.getInfData());
		itemPorOperador.setUsuarioCodigo(informacaoLigacao.getUsuarioCodigo());
		if(null != exame.getUnidade() || !exame.getUnidade().equals("")){
			itemPorOperador.setUnidade(exame.getUnidade());
		}else{
			itemPorOperador.setUnidade("Unidade não Informada");
		}
		itemPorOperador.setExame(exame.getExameCodigo());
		itemPorOperador.setEspecialidade(exame.getExameCodigo());
		itemPorOperador.setValor(Double.valueOf(exame.getValor()));
		itemPorOperador.setDesconto(exame.getPercentualDesconto());
		itemPorOperador.setValorDesconto(exame.getPercentualDesconto());
		if(exame != null || exame.getPercentualDesconto() != null || !exame.getPercentualDesconto().equals("")){
			itemPorOperador.setValorDesconto((exame.getValor() * exame.getPercentualDesconto()) / 100);
		}
		itemPorOperador.setConvertido("N");
		itemPorOperador.setValorNaoConvertido(exame.getValor());
		listItemPorOperador.add(itemPorOperador);
	}
	
	/**
	 * @param listItemPorOperador
	 * @param informacaoLigacao
	 * @param exame
	 */
	private static void setItensEspecialidadeStatuNao(List<ItemPorEspecialidade> listItemPorEspecialidade,
			InformacaoLigacao informacaoLigacao, Exame exame) {
		// exames não convertidos Convertidos (N)
		ItemPorEspecialidade itemPorEspecialidade = new ItemPorEspecialidade();
		itemPorEspecialidade.setData(informacaoLigacao.getInfData());
		itemPorEspecialidade.setEspecialidade(exame.getExameCodigo());
		itemPorEspecialidade.setValor(exame.getValor());
		itemPorEspecialidade.setDesconto(exame.getPercentualDesconto());
		itemPorEspecialidade.setValorDesconto(exame.getPercentualDesconto());
		if(exame != null || exame.getPercentualDesconto() != null || !exame.getPercentualDesconto().equals("")){
			itemPorEspecialidade.setValorDesconto((exame.getValor() * exame.getPercentualDesconto()) / 100);
		}
		itemPorEspecialidade.setConvertido("N");
		itemPorEspecialidade.setValorNaoConvertido(exame.getValor());
		listItemPorEspecialidade.add(itemPorEspecialidade);
	}



	/**
	 * @param listItemPorOperador
	 * @param informacaoLigacao
	 * @param exame
	 */
	private static void setItemOperadorStatusSim(List<ItemPorOperador> listItemPorOperador,
			InformacaoLigacao informacaoLigacao, Exame exame) {
		// exame deve ter status "S"
		ItemPorOperador itemPorOperador = new ItemPorOperador();
		itemPorOperador.setData(informacaoLigacao.getInfData());
		itemPorOperador.setUsuarioCodigo(informacaoLigacao.getUsuarioCodigo());
		if(null != exame.getUnidade() || !exame.getUnidade().equals("")){
			itemPorOperador.setUnidade(exame.getUnidade());
		}else{
			itemPorOperador.setUnidade("Unidade não Informada");
		}
		itemPorOperador.setExame(exame.getExameCodigo());
		itemPorOperador.setEspecialidade(exame.getExameCodigo());
		itemPorOperador.setValor(exame.getValor());
		itemPorOperador.setDesconto(exame.getPercentualDesconto());
		itemPorOperador.setValorDesconto(exame.getPercentualDesconto());
		if(exame != null || exame.getPercentualDesconto() != null || !exame.getPercentualDesconto().equals("")){
			itemPorOperador.setValorDesconto((exame.getValor() * exame.getPercentualDesconto()) / 100);
		}
		itemPorOperador.setConvertido("S");
		itemPorOperador.setValorNaoConvertido(exame.getValor());
		listItemPorOperador.add(itemPorOperador);
	}
	
	/**
	 * @param listItemPorOperador
	 * @param informacaoLigacao
	 * @param exame
	 */
	private static void setItemUnidadeStatus(RelatorioPositivacao relatorioPositivacao, List<ItemPorUnidade> listItemPorUnidade,
			InformacaoLigacao informacaoLigacao, Exame exame, String status) {

		ItemPorUnidade itemPorUnidade = new ItemPorUnidade();
		ItemPorUnidadeItem itemPorUnidadeItem = new ItemPorUnidadeItem();
		
		// set nos dados da unidade.
		itemPorUnidade.setUnidadeCodigo(informacaoLigacao.getUnidadeCodigo());
		itemPorUnidade.setUnidadeNome(exame.getUnidade());
		if(null != exame.getUnidade() || !exame.getUnidade().equals("")){
			itemPorUnidade.setUnidadeNome(exame.getUnidade());
		}else{
			itemPorUnidade.setUnidadeNome("Unidade não Informada");
		}

		// set nos exames da unidade
		itemPorUnidadeItem.setConvertido(status);
		itemPorUnidadeItem.setData(informacaoLigacao.getInfData());
		itemPorUnidadeItem.setDesconto(exame.getPercentualDesconto());
		itemPorUnidadeItem.setEspecialidade(exame.getExameCodigo());
		itemPorUnidadeItem.setUnidadeCodigo(informacaoLigacao.getUnidadeCodigo());
		itemPorUnidadeItem.setUnidadeNome(exame.getUnidade());
		itemPorUnidadeItem.setValor(exame.getValor());
		if(exame != null || exame.getPercentualDesconto() != null || !exame.getPercentualDesconto().equals("")){
			itemPorUnidadeItem.setValorDesconto((exame.getValor() * exame.getPercentualDesconto()) / 100) ;
		}
		itemPorUnidadeItem.setValorNaoConvertido(exame.getValor());
		
		// verificar se a unidade já existe no objeto "relatorioPositivacao"
		// caso exista adicionar o exame atual a esta lista, caso contrário criar uma
		// nova lista e adicionar o exame atual nela.
		verificarExameUnidade(relatorioPositivacao, itemPorUnidade, itemPorUnidadeItem, exame);
	}

	/**
	 * @param relatorioPositivacao
	 * @param exame
	 */
	private static void verificarExameUnidade(RelatorioPositivacao relatorioPositivacao, 
			ItemPorUnidade itemPorUnidadeExame, ItemPorUnidadeItem itemPorUnidadeItem, Exame exame){
		List<ItemPorUnidade> listItemPorUnidade = relatorioPositivacao.getItensPorUnidade();
		boolean isUnidadeNaLista = false;
		if(listItemPorUnidade != null){
			for (Iterator<ItemPorUnidade> iterator = listItemPorUnidade.iterator(); iterator.hasNext();) {
				ItemPorUnidade itemPorUnidade = (ItemPorUnidade) iterator.next();
				if(itemPorUnidade != null && itemPorUnidade.getUnidadeCodigo() != null){
					if(itemPorUnidade.getUnidadeCodigo().equals(itemPorUnidadeExame.getUnidadeCodigo())){
						isUnidadeNaLista = true;
						itemPorUnidade.getItens().add(itemPorUnidadeExame.getItens().get(0));
					}
				}
			}
		}else{
			List<ItemPorUnidade> listItemPorUnidadeEmpty = new ArrayList<>();
			relatorioPositivacao.setItensPorUnidade(listItemPorUnidadeEmpty);
		}

		if(!isUnidadeNaLista){
			List<ItemPorUnidade> listItemPorUnidadeNova = new ArrayList<>();
			List<ItemPorUnidadeItem> listItemPorUnidadeItem = new ArrayList<ItemPorUnidadeItem>();
			listItemPorUnidadeItem.add(itemPorUnidadeItem);
			itemPorUnidadeExame.setItens(listItemPorUnidadeItem);
			listItemPorUnidadeNova.add(itemPorUnidadeExame);
			relatorioPositivacao.setItensPorUnidade(listItemPorUnidadeNova);
		}
	}
	
	/**
	 * @param listItemPorOperador
	 * @param informacaoLigacao
	 * @param exame
	 */
	private static void setItemEspecialidadeStatusSim(List<ItemPorEspecialidade> listItemPorEspecialidade,
			InformacaoLigacao informacaoLigacao, Exame exame) {
		// exame deve ter status "S"
		ItemPorEspecialidade itemPorEspecialidade = new ItemPorEspecialidade();
		itemPorEspecialidade.setData(informacaoLigacao.getInfData());
		itemPorEspecialidade.setEspecialidade(exame.getExameCodigo());
		itemPorEspecialidade.setValor(exame.getValor());
		itemPorEspecialidade.setDesconto(exame.getPercentualDesconto());
		itemPorEspecialidade.setValorDesconto(exame.getPercentualDesconto());
		if(exame != null || exame.getPercentualDesconto() != null || exame.getPercentualDesconto() > 0){
			itemPorEspecialidade.setValorDesconto((exame.getValor() * exame.getPercentualDesconto()) / 100);
		}
		itemPorEspecialidade.setConvertido("S");
		itemPorEspecialidade.setValorNaoConvertido(exame.getValor());
		listItemPorEspecialidade.add(itemPorEspecialidade);
	}

	
	/**
	 * @param infComentarios
	 * @return
	 */
	public List<Exame> lerInfComentarios(String infComentarios){
		
		List<Exame> listExames = new ArrayList<Exame>();
		Scanner comentarios = new Scanner(infComentarios);
		String unidade = "";
		boolean isLabelOrcamentoLocated = false;
		boolean isLinhaInicioCabecalhoLocated = false;
		boolean isLinhaFimCabecalhoLocated = false;
		boolean isCabecalho = false;
		logger.info(infComentarios);
		while (comentarios.hasNext()) {
		    String linha = comentarios.nextLine();
//		    if(linha.startsWith("Unidade")){
//		    	unidade = linha.substring(9,linha.length()-1).trim();
//		    	logger.info("unidade: " + unidade);
//		    }
		    if(linha.toUpperCase().startsWith("ORÇAMENTO".toUpperCase())){
		    	isLabelOrcamentoLocated = true;
		    }
		    if(isLabelOrcamentoLocated && linha.equals("------------------------------------------------")){
		    	isLinhaInicioCabecalhoLocated = true;
		    }
		    if(isLabelOrcamentoLocated && 
		    		isLinhaInicioCabecalhoLocated && 
		    		(linha.startsWith("Exame") && linha.endsWith("Valor"))){
		    	isCabecalho = true;
		    }
		    if(isLabelOrcamentoLocated && 
		    		isLinhaInicioCabecalhoLocated &&
		    		isCabecalho && (linha.equals("------------------------------------------------"))
		    		){
		    	isLinhaFimCabecalhoLocated = true;
		    }
		    
		    // preenche a lista dos exames orçados no campo comentários.
			listarExamesOrcados(listExames, isLabelOrcamentoLocated, isLinhaInicioCabecalhoLocated,
					isLinhaFimCabecalhoLocated, isCabecalho, linha, unidade);//, orcamentoNaoEfetivado);
		}
		comentarios.close();
		return listExames;
	}



	/**
	 * @param listExames
	 * @param isLabelOrcamentoLocated
	 * @param isLinhaInicioCabecalhoLocated
	 * @param isLinhaFimCabecalhoLocated
	 * @param isCabecalho
	 * @param linha
	 */
	private void listarExamesOrcados(List<Exame> listExames, boolean isLabelOrcamentoLocated,
			boolean isLinhaInicioCabecalhoLocated, boolean isLinhaFimCabecalhoLocated, boolean isCabecalho,
			String linha, String unidade){//, OrcamentoNaoEfetivado orcamentoNaoEfetivado) {
		if(isLabelOrcamentoLocated && 
				isLinhaInicioCabecalhoLocated && 
				isCabecalho && 
				isLinhaFimCabecalhoLocated){

			if(linha.contains("PART ") && linha.length() > 46){
				Exame exame = new Exame(); 
				exame.setExameCodigo(linha.substring(0,5).trim());
				exame.setConvenioCodigo(linha.substring(6,17).trim());
				if(unidade != null && !unidade.equals("")){
					exame.setUnidade(unidade);
				}else{
					exame.setUnidade("Unidade não informada.");
				}
				exame.setQuantidade(linha.substring(18,20).trim());
				exame.setValorUnitario(Double.valueOf(linha.substring(21,31).replace(".", "").replace(",", ".").trim()));
				exame.setPercentualDesconto(Double.valueOf(linha.substring(32,36).replace(".", "").replace(",", ".").replace("%", "").trim()));
				exame.setValor(Double.valueOf(linha.substring(37,47).replace(".", "").replace(",", ".").trim()));
				listExames.add(exame);
			}
			
		}else{
//			logger.info("Campo inf-comentarios vazio, coletandao dados da tabela Agenda Orçamentos.");
//			
//			if(orcamentoNaoEfetivado.getExameCodigo() != null){
//				Exame exame = new Exame(); 
//				exame.setExameCodigo(orcamentoNaoEfetivado.getExameCodigo());
//				exame.setConvenioCodigo(orcamentoNaoEfetivado.getConvenioCodigo());
//				if(unidade != null && !unidade.equals("")){
//					exame.setUnidade(unidade);
//				}else{
//					exame.setUnidade("Unidade não informada.");
//				}
//				exame.setQuantidade("1");
//				exame.setValorUnitario(Double.valueOf(orcamentoNaoEfetivado.getValorAC()) + orcamentoNaoEfetivado.getValorRDI());
//				exame.setPercentualDesconto(0.0);
//				exame.setValor(Double.valueOf(linha.substring(37,47).replace(".", "").replace(",", ".").trim()));
//				listExames.add(exame);			
//			}
		}
	}
	
	/**
	 * @param listAgendaOrcamento
	 * @return
	 */
	private static List<String> listarExamesAgendados(List<AgendaOrcamento> listAgendaOrcamento){
		List<String> listExamesAgendadosCodigos = new ArrayList<String>();
		for (Iterator<AgendaOrcamento> iterator = listAgendaOrcamento.iterator(); iterator.hasNext();) {
			AgendaOrcamento agendaOrcamento = (AgendaOrcamento) iterator.next();
			listExamesAgendadosCodigos.add(agendaOrcamento.getExameCodigo());
		}
		return listExamesAgendadosCodigos;
	}
	
	/**
	 * @param listOrcamentoNaoEfetivado
	 * @param listUsuariosOrcamento
	 * @param dataSource
	 * @return
	 */
	private List<AgendaOrcamento> consultarAgendaEfetivado(
			List<Orcamento> listOrcamentoNaoEfetivado, List<UsuarioGrupoOrcamento> listUsuariosOrcamento, DataSource dataSource){
		
		List<AgendaOrcamento> listOrcamentosEfetivadosResultset = new ArrayList<>();
		List<AgendaOrcamento> listOrcamentosEfetivados = new ArrayList<>();
		
		for (Iterator<Orcamento> iteratorOrc = listOrcamentoNaoEfetivado.iterator(); iteratorOrc.hasNext();) {
			Orcamento orcamentoNaoEfetivado = (Orcamento) iteratorOrc.next();
			String sqlAgendaOrcamento = ProgressJDBCConnectDB.agendaOrcamentoSQL(
					orcamentoNaoEfetivado.getPacienteCodigo(), orcamentoNaoEfetivado.getDataLigacao(), orcamentoNaoEfetivado.getUsuarioCodigo(), listUsuariosOrcamento);
			listOrcamentosEfetivadosResultset = ProgressJDBCConnectDB.consultarAgendaOrcamento(dataSource, sqlAgendaOrcamento);
			for (Iterator<AgendaOrcamento> iteratorAgen = listOrcamentosEfetivadosResultset.iterator(); iteratorAgen.hasNext();) {
				AgendaOrcamento agendaOrcamento = (AgendaOrcamento) iteratorAgen.next();
				listOrcamentosEfetivados.add(agendaOrcamento);
				System.out.println("agData: " + agendaOrcamento.getAgData() + "\n" + 
									"DataLigacao: " + agendaOrcamento.getPacienteCodigo() + "\n" +
									"DataLigacao: " + agendaOrcamento.getDataLigacao() + "\n" +
									"ExameCodigo: " + agendaOrcamento.getExameCodigo() + "\n" +
									"DataLigacao: " + agendaOrcamento.getUnidadeCodigo() + "\n" 
						);
			}
		}
		return	listOrcamentosEfetivados;
	}
	
	/**
	 * @return
	 * @throws SQLException 
	 */
	private RelatorioPositivacao verificarOrcamentosRevertidosAgenda(
			List<InformacaoLigacao> listInformacaoLigacao, List<AgendaOrcamento> listAgendaOrcamentoEfetivados,
			DataSource dataSource) throws SQLException{
		RelatorioPositivacao relatorioPositivacao = new RelatorioPositivacao();
		List<AgendaOrcamento> listAgendaOrcamento = new ArrayList<AgendaOrcamento>();
		List<ItemPorOperador> listItemPorOperador = new ArrayList<ItemPorOperador>();
		List<ItemPorUnidade> listItemPorUnidade = new ArrayList<ItemPorUnidade>();
		List<ItemPorEspecialidade> listItemPorEspecialidader = new ArrayList<ItemPorEspecialidade>();
		int totalProcessados = 0;
		int totalConvertido = 0;
		int totalNaoConvertidos = 0;
		
		// percorrendo lista de informação da ligação.
		for (Iterator<InformacaoLigacao> iterator = listInformacaoLigacao.iterator(); iterator.hasNext();) {
			InformacaoLigacao informacaoLigacao = (InformacaoLigacao) iterator.next();
			// consultando cada exame da informaçao da ligação na agenda. // TODO passar data inicial
			listAgendaOrcamento = consultarAgendaOrcamentos(informacaoLigacao, listAgendaOrcamentoEfetivados);
			// lista de codigo de exames agendados
			List<String> listExamesAgendados = listarExamesAgendados(listAgendaOrcamento);
			if(listAgendaOrcamento.size() > 0){
				// exames da informação da ligação
				List<Exame> listExames = informacaoLigacao.getExames();
				for (Iterator<Exame> iteratorExames = listExames.iterator(); iteratorExames.hasNext();) {
					Exame exame = (Exame) iteratorExames.next();
					// verifica quais exames da ligação foram agendados.
					if(verificarExameAgendado(exame.getExameCodigo(), listExamesAgendados)){
						logger.info("\n Convertidos " + totalConvertido);
						setItemOperadorStatusSim(listItemPorOperador, informacaoLigacao, exame);
						setItemUnidadeStatus(relatorioPositivacao, listItemPorUnidade, informacaoLigacao, exame, "S");
						setItemEspecialidadeStatusSim(listItemPorEspecialidader, informacaoLigacao, exame);
					}else{
						logger.info("\n Nao Convertidos " + totalNaoConvertidos);
						setItensOperadorStatuNao(listItemPorOperador, informacaoLigacao, exame);
						setItemUnidadeStatus(relatorioPositivacao, listItemPorUnidade, informacaoLigacao, exame, "N");
						setItensEspecialidadeStatuNao(listItemPorEspecialidader, informacaoLigacao, exame);
					}
				}
			}else{
				logger.info("\n Nao Convertidos " + totalNaoConvertidos);
				List<Exame> listExames = informacaoLigacao.getExames();
				for (Iterator<Exame> iterator2 = listExames.iterator(); iterator2.hasNext();) {
					Exame exame = (Exame) iterator2.next();
					setItensOperadorStatuNao(listItemPorOperador, informacaoLigacao, exame);
					setItemUnidadeStatus(relatorioPositivacao, listItemPorUnidade, informacaoLigacao, exame, "N");
					setItensEspecialidadeStatuNao(listItemPorEspecialidader, informacaoLigacao, exame);
				}
			}
			System.out.println("Total processados: " +totalProcessados);
			totalConvertido++;
			totalNaoConvertidos++;
			totalProcessados++;
		}
		relatorioPositivacao.setItensPorOperador(listItemPorOperador);
		relatorioPositivacao.setItemPorEspecialidade(listItemPorEspecialidader);
		
		// calcular reumos dos orçamentos.
		calcularResumosOrcamentos(relatorioPositivacao);
		return relatorioPositivacao;	
	}

	/**
	 * @param informacaoLigacao
	 * @return
	 */
	private List<AgendaOrcamento> consultarAgendaOrcamentos(
			InformacaoLigacao informacaoLigacao, List<AgendaOrcamento> listAgendaOrcamentoEfetivados){
		List<AgendaOrcamento> listAgendaOrcamento = new ArrayList<AgendaOrcamento>();
		for (Iterator<AgendaOrcamento> iterator = listAgendaOrcamento.iterator(); iterator.hasNext();) {
			AgendaOrcamento agendaOrcamento = (AgendaOrcamento) iterator.next();
			if(agendaOrcamento.getUsuarioCodigo().equals(informacaoLigacao.getUsuarioCodigo()) &&
					agendaOrcamento.getPacienteCodigo().equals(informacaoLigacao.getPacienteCodigo()) &&
							agendaOrcamento.getDataLigacao().equals(informacaoLigacao.getInfData())){
				listAgendaOrcamento.add(agendaOrcamento);
			}
		}
		return listAgendaOrcamento;
	}

}