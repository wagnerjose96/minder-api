package br.hela.alarme.alarme_medicamento;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class Alarme_Medicamento_Service {
	@Autowired
	private Alarme_Medicamento_Repository repo;

	public void salvar(Alarme_Medicamento novoAlarmeMedicamento) {
		repo.save(novoAlarmeMedicamento);
	}
}
