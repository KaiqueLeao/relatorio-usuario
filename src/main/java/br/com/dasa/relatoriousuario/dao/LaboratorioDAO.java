package br.com.dasa.relatoriousuario.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import br.com.dasa.relatoriousuario.model.DataSource;
import br.com.dasa.relatoriousuario.model.Laboratorio;
import br.com.dasa.relatoriousuario.service.PositivacaoNegociacaoService;
import br.com.dasa.relatoriousuario.utils.ConnectionManager;
import br.com.dasa.relatoriousuario.utils.Constantes;
import br.com.dasa.relatoriousuario.utils.Util;


/**
 * @author t35615539828
 *
 */
@Component
public class LaboratorioDAO {
	
	public final Logger logger = Logger.getLogger(LaboratorioDAO.class);
	/**
	 * 
	 * @return
	 */
	public List<Laboratorio> listar(){ 
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<Laboratorio> todosLaboratorios() throws IOException {
		Statement stmt = null;
		List<Laboratorio> laboratorios = new ArrayList<>();
		try{
			DataSource ds = Util.retornaDataSourcesMarca("001", Constantes.PARAM);
			/**
			 * Efetua conexão com o banco de dados 
			 * recebendo como parametro o DataSource
			 */
			Connection con = ConnectionManager.getConnection(ds);
		    String query = "Select \"laboratorio-codigo\" Codigo, \"lab-nome\" Nome " +
		    			   " From PUB.Laboratorio " +
		    			   " Order By Nome";
		    try {
		        stmt = con.createStatement();
		        ResultSet rs = stmt.executeQuery(query);
		        
		        while (rs.next()) {
		        	Laboratorio lab = new Laboratorio();
		        	lab.setCodigoLaboratorio(rs.getString("Codigo"));
		            lab.setNomeLaboratorio(rs.getString("Nome"));
		            laboratorios.add(lab);
		        }
		        
		        return laboratorios;
		        
		    } catch (SQLException e ) {
		    	logger.error("Erro buscar laboratorios: " + e.getMessage());
		    }finally {
				con.close();
			}
			
		}catch(Exception e){
			e.getMessage();
		}
		return null;
	}
	
}
