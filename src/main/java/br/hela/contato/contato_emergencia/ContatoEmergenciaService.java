package br.hela.contato.contato_emergencia;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ContatoEmergenciaService {
	@Autowired
	private ContatoEmergenciaRepository repo;

	public void salvar(ContatoEmergencia novoContatoEmergencia) {
		repo.save(novoContatoEmergencia);
	}

	public List<ContatoEmergencia> encontrar() {
		return repo.findAll();
	}
}