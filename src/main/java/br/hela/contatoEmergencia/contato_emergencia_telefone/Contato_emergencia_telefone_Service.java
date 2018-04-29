package br.hela.contatoEmergencia.contato_emergencia_telefone;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.hela.contatoEmergencia.ContatoEmergenciaId;
import br.hela.telefone.TelefoneId;

@Service
@Transactional
public class Contato_emergencia_telefone_Service {
	@Autowired
	private Contato_emergencia_telefone_Repository repo;

	public void salvar(TelefoneId idTelefone, ContatoEmergenciaId idContato) {
		Contato_emergencia_telefone contatoEmergenciaTelefone = new Contato_emergencia_telefone();
		contatoEmergenciaTelefone.setIdContatoEmergencia(idContato);
		contatoEmergenciaTelefone.setIdTelefone(idTelefone);
		repo.save(contatoEmergenciaTelefone);
	}
}
