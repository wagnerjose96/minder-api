package br.hela.contatoEmergencia.contato_emergencia_telefone;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class Contato_emergencia_telefone_Service {
	@Autowired
	private Contato_emergencia_telefone_Repository repo;

	public void salvar(Contato_emergencia_telefone novoContatoEmergenciaTelefone) {
		repo.save(novoContatoEmergenciaTelefone);
	}
}
