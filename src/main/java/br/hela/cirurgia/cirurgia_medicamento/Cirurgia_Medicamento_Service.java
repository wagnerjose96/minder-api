package br.hela.cirurgia.cirurgia_medicamento;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class Cirurgia_Medicamento_Service {
	@Autowired
	private Cirurgia_Medicamento_Repository repo;

	public void salvar(Cirurgia_Medicamento novoCirurgiaMedicamento) {
		repo.save(novoCirurgiaMedicamento);
	}
}
