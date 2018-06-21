package br.hela.planoDeSaude;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import br.hela.convenio.ConvenioService;
import br.hela.convenio.comandos.BuscarConvenio;
import br.hela.planoDeSaude.comandos.BuscarPlanoDeSaude;
import br.hela.planoDeSaude.comandos.CriarPlanoDeSaude;
import br.hela.planoDeSaude.comandos.EditarPlanoDeSaude;

@Service
@Transactional
public class PlanoDeSaudeService {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	private PlanoDeSaudeRepository repo;

	@Autowired
	private ConvenioService convService;

	public Optional<String> deletar(PlanoDeSaudeId id) {
		repo.deleteById(id);
		return Optional.of("Plano de saúde ==> " + id + " deletado com sucesso!");
	}

	public Optional<PlanoDeSaudeId> salvar(CriarPlanoDeSaude comando) {
		PlanoDeSaude novo = repo.save(new PlanoDeSaude(comando));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarPlanoDeSaude> encontrar(PlanoDeSaudeId planoId) {
		PlanoDeSaude plano = repo.findById(planoId).get();
		BuscarPlanoDeSaude resultado = new BuscarPlanoDeSaude(plano);
		Optional<BuscarConvenio> convenio = convService.encontrar(plano.getIdConvenio());
		resultado.setConvenio(convenio.get());
		return Optional.of(resultado);
	}

	public Optional<List<BuscarPlanoDeSaude>> encontrar() {
		List<BuscarPlanoDeSaude> rsPlanos = new ArrayList<>();
		List<PlanoDeSaude> planos = repo.findAll();
		for (PlanoDeSaude plano : planos) {
			Optional<BuscarConvenio> convenio = convService.encontrar(plano.getIdConvenio());
			BuscarPlanoDeSaude nova = new BuscarPlanoDeSaude(plano);
			nova.setConvenio(convenio.get());
			rsPlanos.add(nova);
		}
		return Optional.of(rsPlanos);
	}

	public Optional<PlanoDeSaudeId> alterar(EditarPlanoDeSaude comando) {
		Optional<PlanoDeSaude> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			PlanoDeSaude plano = optional.get();
			plano.apply(comando);
			repo.save(plano);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

}
