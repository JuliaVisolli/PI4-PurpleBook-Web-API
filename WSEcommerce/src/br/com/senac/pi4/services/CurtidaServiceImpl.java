package br.com.senac.pi4.services;

import br.com.senac.pi4.dao.CurtidaDAO;
import br.com.senac.pi4.model.Curtida;

public class CurtidaServiceImpl {

	CurtidaDAO curtidaDAO = new CurtidaDAO();

	public void curtida(Curtida curtida) {
		try {

			curtidaDAO.curtida(curtida);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}