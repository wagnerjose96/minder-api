package br.hela.alergia.alergia_medicamento;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class Alergia_Medicamento_Service {
	@Autowired
	private Alergia_Medicamento_Repository repo;

	public void salvar(Alergia_Medicamento novoAlergiaMedicamento) {
		repo.save(novoAlergiaMedicamento);
	}
	
	public List<Alergia_Medicamento> encontrar() {
		return repo.findAll();
	}
}