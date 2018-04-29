package br.hela.planoDeSaude;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.hela.convenio.Convenio;
import br.hela.convenio.ConvenioId;
import br.hela.planoDeSaude.comandos.BuscarPlanoDeSaude;
import br.hela.planoDeSaude.comandos.CriarPlanoDeSaude;
import br.hela.planoDeSaude.comandos.EditarPlanoDeSaude;

@Service
@Transactional
public class PlanoDeSaudeService {
	@Autowired
	private PlanoDeSaudeRepository repo;

	public Optional<PlanoDeSaudeId> salvar(CriarPlanoDeSaude comando) {
		PlanoDeSaude novo = repo.save(new PlanoDeSaude(comando));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarPlanoDeSaude> encontrar(PlanoDeSaudeId planoId) throws Exception {
		ResultSet rs = executeQuery(planoId.toString());
		BuscarPlanoDeSaude plano = new BuscarPlanoDeSaude(repo.findById(planoId).get());
		String id = planoId.toString();
		plano.setConvenio(convenio(rs, id));
		return Optional.of(plano);
	}

	public Optional<List<BuscarPlanoDeSaude>> encontrar() throws Exception {
		List<PlanoDeSaude> planos = repo.findAll();
		List<BuscarPlanoDeSaude> rsPlanos = new ArrayList<>();
		for (PlanoDeSaude plano : planos) {
			ResultSet rs = executeQuery(plano.getId().toString());
			BuscarPlanoDeSaude nova = new BuscarPlanoDeSaude(plano);
			nova.setConvenio(convenio(rs, plano.getId().toString()));
			rsPlanos.add(nova);
		}
		return Optional.of(rsPlanos);
	}

	private Statement connect() throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/escoladeti2018", "postgres",
				"11223344");
		Statement stmt = con.createStatement();
		return stmt;
	}

	private ResultSet executeQuery(String id) throws Exception {
		Statement stmt = connect();
		String query = "select b.id, a.nome, " + "a.id, a.ativo from convenio a "
				+ "inner join plano_de_saude b on a.id = b.id_convenio "
				+ "group by b.id, b.id_convenio, a.id having b.id = '" + id + "' " + "order by b.id";
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}

	private Convenio convenio(ResultSet rs, String id) throws Exception {
		Convenio conv = null;
		while (rs.next()) {
			String idPlano = rs.getString("id");
			if (id.equals(idPlano)) {
				conv = new Convenio();
				conv.setId(new ConvenioId(rs.getString("id")));
				conv.setNome(rs.getString("nome"));
				conv.setAtivo(rs.getInt("ativo"));
			}
		}
		return conv;
	}

	public Optional<PlanoDeSaudeId> alterar(EditarPlanoDeSaude comando) {
		Optional<PlanoDeSaude> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			PlanoDeSaude plano = optional.get();
			plano.apply(comando);
			repo.save(plano);
		}
		return Optional.of(comando.getId());
	}
}
