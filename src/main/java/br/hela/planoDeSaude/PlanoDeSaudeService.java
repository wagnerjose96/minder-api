package br.hela.planoDeSaude;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.hela.planoDeSaude.comandos.CriarPlanoDeSaude;

@Service
@Transactional
public class PlanoDeSaudeService {
	@Autowired
	private PlanoDeSaudeRepository repo;

	public Optional<PlanoDeSaudeId> salvar(CriarPlanoDeSaude comando) {
		PlanoDeSaude novo = repo.save(new PlanoDeSaude(comando));
		return Optional.of(novo.getId());
	}

	public Optional<PlanoDeSaude> encontrar(PlanoDeSaudeId planoId) throws Exception {
		return repo.findById(planoId);
	}

	public Optional<List<PlanoDeSaude>> encontrar() throws Exception {
		return Optional.of(repo.findAll());
	}

}
