package br.com.dasa.relatoriousuario.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.com.dasa.relatoriousuario.model.DataSource;

public final class Util {
	
	static Logger logger = Logger.getLogger(Util.class);

	/**
	 * 
	 * @param marca
	 * @param bdSelecionado
	 * @return
	 * @throws IOException
	 */
	public static DataSource retornaDataSourcesMarca(String marca, int bdSelecionado) throws IOException{
		
		Properties prop = new Properties();
		InputStream input = null;

		try {
			/**
			 * Busca o arquivo Application.properties na raiz do projeto
			 */
			input = Util.carregaApplicationProperties();

			// load a properties file
			prop.load(input);

			Enumeration<Object> elements = prop.elements();
			
			DataSource ds = new DataSource();
			
			/**
			 * Conexão com banco PARAM
			 */
			if(bdSelecionado == Constantes.PARAM){
				ds.setUrl(prop.getProperty("bd." + marca + ".1.url"));
				ds.setName(prop.getProperty("bd." + marca + ".1.username"));
				ds.setPassword(prop.getProperty("bd." + marca + ".1.password"));
				ds.setDriverClassName(prop.getProperty("bd." + marca + ".1.driverClassName"));
			}else if(bdSelecionado == Constantes.AGENDA){
				/**
				 * Conexão com banco AGENDA
				 */
				ds.setUrl(prop.getProperty("bd." + marca + ".2.url"));
				ds.setName(prop.getProperty("bd." + marca + ".2.username"));
				ds.setPassword(prop.getProperty("bd." + marca + ".2.password"));
				ds.setDriverClassName(prop.getProperty("bd." + marca + ".2.driverClassName"));
			}else{
				/**
				 * Conexão com banco CENTRAL
				 */
				ds.setUrl(prop.getProperty("bd." + marca + ".3.url"));
				ds.setName(prop.getProperty("bd." + marca + ".3.username"));
				ds.setPassword(prop.getProperty("bd." + marca + ".3.password"));
				ds.setDriverClassName(prop.getProperty("bd." + marca + ".3.driverClassName"));
			}
			
			return ds;
			
		} catch (Exception e) {
			logger.error("Erro retornaDataSourcesMarca : " + e.getMessage());
		} finally {
			input.close();
		}
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	private static InputStream carregaApplicationProperties() throws Exception {
		try{
			InputStream input;
			File file = new File("application.properties");
			String absolutePath = file.getAbsolutePath();
			input = new FileInputStream(absolutePath);
			return input;
		}catch(Exception e){
			logger.error("Erro CarregaApplicationProperties : " + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Obtém caminho para pasta temporária
	 * @return
	 */
	public static String getFilePath(HttpServletRequest request){
		HttpSession session = request.getSession(true);
		String caminhoTemp = System.getProperty("java.io.tmpdir");
		String reportDate = formataData();
		
		try{
			if(caminhoTemp.endsWith("/") || caminhoTemp.endsWith("\\"))
				caminhoTemp = caminhoTemp.substring(0, caminhoTemp.length() - 1);
				
			String nomeArquivo = caminhoTemp + "/relatorioPositivacao" + reportDate + ".xls";
			session.setAttribute("caminhoArquivo", nomeArquivo);
			
			return caminhoTemp + "/relatorioPositivacao" + reportDate + ".xls";
		}catch(Exception e){
			logger.error("Erro getFilePath: " + e.getMessage());
		}
		return null;
	}

	private static String formataData() {
		DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
		Date today = Calendar.getInstance().getTime();        
		String reportDate = df.format(today);
		return reportDate;
	}
	
	/**
	 * Excluir arquivo
	 * @param file
	 * @return
	 */
	public static boolean excluirArquivo(File file){
		try{
			file.delete(); 
			logger.info("Arquivo " + file.getAbsolutePath() + " excluido com sucesso!");
			return true;
		}catch(Exception e){
			logger.error("Erro excluirArquivo: " + e.getMessage());
		}
		return false;
	}
	
}
