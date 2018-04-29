package br.hela.planoDeSaude.planoDeSaude_convenio;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PlanoDeSaude_Convenio_Service {
	@Autowired
	private PlanoDeSaude_Convenio_Repository repo;

	public void salvar(PlanoDeSaude_Convenio novo) {
		repo.save(novo);
	}
}
