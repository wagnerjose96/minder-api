package br.minder.doenca.doenca_medicamento;

import java.util.List;
import java.util.Optional;
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
	
	public Optional<DoencaMedicamento> encontrar(DoencaMedicamentoId id) {
		Optional<DoencaMedicamento> doenca = repo.findById(id);
		if (doenca.isPresent()) {
			DoencaMedicamento resultado = new DoencaMedicamento(doenca.get());
			return Optional.of(resultado);
		}
		return Optional.empty();
	}
}