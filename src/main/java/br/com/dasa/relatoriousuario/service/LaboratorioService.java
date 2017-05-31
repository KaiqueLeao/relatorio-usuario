package br.com.dasa.relatoriousuario.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dasa.relatoriousuario.dao.LaboratorioDAO;
import br.com.dasa.relatoriousuario.model.Laboratorio;

@Component
public class LaboratorioService {

	@Autowired
	LaboratorioDAO daoLaboratorio;

	public List<Laboratorio> todosLaboratorios() {
		try {
			return daoLaboratorio.todosLaboratorios();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}