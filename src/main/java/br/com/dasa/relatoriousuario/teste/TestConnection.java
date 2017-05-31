package br.com.dasa.relatoriousuario.teste;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.dasa.relatoriousuario.model.DataSource;
import br.com.dasa.relatoriousuario.model.Laboratorio;
import br.com.dasa.relatoriousuario.utils.ConnectionManager;
import br.com.dasa.relatoriousuario.utils.Constantes;
import br.com.dasa.relatoriousuario.utils.Util;

public class TestConnection {
	
	public static void main(String[] args) throws IOException, SQLException {
		
		DataSource ds = new DataSource();
		/**
		 * ("001") representa o código do laboratório que será obtido pelo banco
		 */
		ds = Util.retornaDataSourcesMarca("001", Constantes.PARAM);
		
		/**
		 * Efetua conexão com o banco de dados 
		 * recebendo como parametro o DataSource
		 */
		Connection con = ConnectionManager.getConnection(ds);
		List<Laboratorio> laboratorios = new ArrayList<>();
		
		Statement stmt = null;
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
	        
	        for(Laboratorio lab : laboratorios){
	        	System.out.println(lab.getCodigoLaboratorio() + "\t" + lab.getNomeLaboratorio() );
	        }
	        
	    } catch (SQLException e ) {
	    	System.out.println("Erro buscar laboratorios: " + e.getMessage());
	    }finally {
			con.close();
		}
		
	}
	
}
