package br.minder.alergia.alergia_medicamento;

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
	
	public Optional<AlergiaMedicamento> encontrar(AlergiaMedicamentoId id) {
		Optional<AlergiaMedicamento> alergia = repo.findById(id);
		if (alergia.isPresent()) {
			AlergiaMedicamento resultado = new AlergiaMedicamento(alergia.get());
			return Optional.of(resultado);
		}
		return Optional.empty();
	}
}
