package br.minder.plano_de_saude;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.minder.convenio.ConvenioService;
import br.minder.convenio.comandos.BuscarConvenio;
import br.minder.plano_de_saude.comandos.BuscarPlanoDeSaude;
import br.minder.plano_de_saude.comandos.CriarPlanoDeSaude;
import br.minder.plano_de_saude.comandos.EditarPlanoDeSaude;
import br.minder.usuario.UsuarioId;

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
		return Optional.of("Plano de saúde ===> " + id + ": deletado com sucesso");
	}

	public Optional<PlanoDeSaudeId> salvar(CriarPlanoDeSaude comando, UsuarioId id) {
		PlanoDeSaude novo = repo.save(new PlanoDeSaude(comando, id));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarPlanoDeSaude> encontrar(PlanoDeSaudeId planoId) {
		Optional<PlanoDeSaude> plano = repo.findById(planoId);
		if (plano.isPresent()) {
			BuscarPlanoDeSaude resultado = new BuscarPlanoDeSaude(plano.get());
			Optional<BuscarConvenio> convenio = convService.encontrar(plano.get().getIdConvenio());
			if (convenio.isPresent()) {
				resultado.setConvenio(convenio.get());
				return Optional.of(resultado);
			}
		}
		return Optional.empty();
	}

	public Optional<List<BuscarPlanoDeSaude>> encontrar(UsuarioId id) {
		List<BuscarPlanoDeSaude> rsPlanos = new ArrayList<>();
		List<PlanoDeSaude> planos = repo.findAll();
		if (!planos.isEmpty()) {
			for (PlanoDeSaude plano : planos) {
				if (id.toString().equals(plano.getIdUsuario().toString())) {
					Optional<BuscarConvenio> convenio = convService.encontrar(plano.getIdConvenio());
					BuscarPlanoDeSaude nova = new BuscarPlanoDeSaude(plano);
					if (convenio.isPresent()) {
						nova.setConvenio(convenio.get());
						rsPlanos.add(nova);
					}
				}
			}
			return Optional.of(rsPlanos);
		}
		return Optional.empty();
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
