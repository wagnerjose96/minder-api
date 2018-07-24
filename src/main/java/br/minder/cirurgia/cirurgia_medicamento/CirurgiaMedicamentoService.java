package br.minder.cirurgia.cirurgia_medicamento;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CirurgiaMedicamentoService {
	@Autowired
	private CirurgiaMedicamentoRepository repo;

	public void salvar(CirurgiaMedicamento novoCirurgiaMedicamento) {
		repo.save(novoCirurgiaMedicamento);
	}
	
	public Optional<CirurgiaMedicamento> encontrar(CirurgiaMedicamentoId id) {
		Optional<CirurgiaMedicamento> cirurgia = repo.findById(id);
		if (cirurgia.isPresent()) {
			CirurgiaMedicamento resultado = new CirurgiaMedicamento(cirurgia.get());
			return Optional.of(resultado);
		}
		return Optional.empty();
	}
}
