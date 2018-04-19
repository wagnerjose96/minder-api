package br.hela.doenca.doenca_medicamento;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class Doenca_Medicamento_Service {
	@Autowired
	private Doenca_Medicamento_Repository repo;

	public void salvar(Doenca_Medicamento novoDoencaMedicamento) {
		repo.save(novoDoencaMedicamento);
	}

	public List<Doenca_Medicamento> encontrar() {
		return repo.findAll();
	}
}

