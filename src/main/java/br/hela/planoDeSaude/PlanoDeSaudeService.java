package br.hela.planoDeSaude;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import br.hela.convenio.ConvenioId;
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
	
	public Optional<String> deletar(PlanoDeSaudeId id) {
		repo.deleteById(id);
		return Optional.of("Plano de saúde ==> " + id + " deletado com sucesso!");
	}

	public Optional<PlanoDeSaudeId> salvar(CriarPlanoDeSaude comando) {
		PlanoDeSaude novo = repo.save(new PlanoDeSaude(comando));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarPlanoDeSaude> encontrar(PlanoDeSaudeId planoId) throws Exception {
		BuscarPlanoDeSaude plano = new BuscarPlanoDeSaude(repo.findById(planoId).get());
		List<BuscarConvenio> convenio = executeQuery(planoId.toString());
		plano.setConvenio(convenio.get(0));
		return Optional.of(plano);
	}

	public Optional<List<BuscarPlanoDeSaude>> encontrar() throws Exception {
		List<BuscarPlanoDeSaude> rsPlanos = new ArrayList<>();
		List<PlanoDeSaude> planos = repo.findAll();
		for (PlanoDeSaude plano : planos) {
			List<BuscarConvenio> convenio = executeQuery(plano.getId().toString());
			BuscarPlanoDeSaude nova = new BuscarPlanoDeSaude(plano);
			nova.setConvenio(convenio.get(0));
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
	
	private List<BuscarConvenio> executeQuery(String id) throws Exception {
		List<BuscarConvenio> convenio = jdbcTemplate.query("select c.id, c.id_convenio, a.nome, " 
				+ "a.id, a.ativo from convenio a "
				+ "inner join plano_de_saude c on a.id = c.id_convenio "
				+ "group by c.id, a.id having c.id = ?",
				new Object[] { id }, (rs, rowNum) -> {
					BuscarConvenio conv = new BuscarConvenio();
						String idPlano = rs.getString("id");
						if (id.equals(idPlano)) {
							conv.setId(new ConvenioId(rs.getString("id_convenio")));
							conv.setNome(rs.getString("nome"));
							conv.setAtivo(rs.getInt("ativo"));
						}
					return conv;
				});
		return convenio;
	}

}
