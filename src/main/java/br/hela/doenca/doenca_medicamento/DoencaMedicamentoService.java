package br.hela.doenca.doenca_medicamento;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DoencaMedicamentoService {
	@Autowired
	private DoencaMedicamentoRepository repo;

	public void salvar(DoencaMedicamento novoDoencaMedicamento) {
		repo.save(novoDoencaMedicamento);
	}

	public List<DoencaMedicamento> encontrar() {
		return repo.findAll();
	}
}