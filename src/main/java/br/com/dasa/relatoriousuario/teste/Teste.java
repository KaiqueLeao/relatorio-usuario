package br.com.dasa.relatoriousuario.teste;

import java.io.IOException;

import br.com.dasa.relatoriousuario.model.DataSource;
import br.com.dasa.relatoriousuario.utils.Constantes;
import br.com.dasa.relatoriousuario.utils.Util;

public class Teste {
	
	public static void main(String[] args) throws IOException {

		/**
		 * ("001") representa o código do laboratório que será obtido pelo banco
		 */
		DataSource ds = Util.retornaDataSourcesMarca("001", Constantes.AGENDA);
		
		System.out.println("DS Username: " 		  + ds.getName());
		System.out.println("DS Password: " 		  + ds.getPassword());
		System.out.println("DS URL: " 			  + ds.getUrl());
		System.out.println("DS DriverClassName: " + ds.getDriverClassName());
	}
}
