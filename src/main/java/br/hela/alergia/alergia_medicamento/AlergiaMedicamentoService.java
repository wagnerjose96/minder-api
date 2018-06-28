package br.hela.alergia.alergia_medicamento;

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
}
