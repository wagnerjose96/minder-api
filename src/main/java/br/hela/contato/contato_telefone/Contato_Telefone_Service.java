package br.hela.contato.contato_telefone;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class Contato_Telefone_Service {
	@Autowired
	private Contato_Telefone_Repository repo;

	public void salvar(Contato_Telefone comando) {
		repo.save(comando);
	}
	
}
