package br.minder.alergia.alergia_medicamento;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AlergiaMedicamentoService {
	@Autowired
	private AlergiaMedicamentoRepository repo;

	public void salvar(AlergiaMedicamento novoAlergiaMedicamento) {
		repo.save(novoAlergiaMedicamento);
	}

	public List<AlergiaMedicamento> encontrar() {
		return repo.findAll();
	}

	public Optional<AlergiaMedicamento> encontrar(AlergiaMedicamentoId id) {
		return repo.findById(id);
	}

}
