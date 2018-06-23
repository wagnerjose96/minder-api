package br.hela.contato.contato_emergencia;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class Contato_Emergencia_Service {
	@Autowired
	private Contato_Emergencia_Repository repo;

	public void salvar(Contato_Emergencia novoContatoEmergencia) {
		repo.save(novoContatoEmergencia);
	}

	public List<Contato_Emergencia> encontrar() {
		return repo.findAll();
	}
}