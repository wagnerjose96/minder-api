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
	
	public Optional<String> deletar(PlanoDeSaudeId id) {
		repo.deleteById(id);
		return Optional.of("Plano ==> " + id + " deletado com sucesso!");
	}

	public Optional<PlanoDeSaudeId> salvar(CriarPlanoDeSaude comando) {
		PlanoDeSaude novo = repo.save(new PlanoDeSaude(comando));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarPlanoDeSaude> encontrar(PlanoDeSaudeId planoId) throws Exception {
		BuscarPlanoDeSaude plano = new BuscarPlanoDeSaude(repo.findById(planoId).get());
		ResultSet rs = executeQuery(planoId.toString());
		Convenio conv = convenio(rs, planoId.toString());
		if (conv.getAtivo() != 0) {
			plano.setConvenio(conv);
		}
		return Optional.of(plano);
	}

	public Optional<List<BuscarPlanoDeSaude>> encontrar() throws Exception {
		List<BuscarPlanoDeSaude> rsPlanos = new ArrayList<>();
		List<PlanoDeSaude> planos = repo.findAll();
		for (PlanoDeSaude plano : planos) {
			ResultSet rs = executeQuery(plano.getId().toString());
			BuscarPlanoDeSaude nova = new BuscarPlanoDeSaude(plano);
			Convenio conv = convenio(rs, plano.getId().toString());
			if (conv.getAtivo() != 0) {
				nova.setConvenio(conv);
			}
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

	private Convenio convenio(ResultSet rs, String id) throws Exception {
		Convenio conv = new Convenio();
		while (rs.next()) {
			String idPlano = rs.getString("id");
			if (id.equals(idPlano)) {
				conv.setId(new ConvenioId(rs.getString("id_convenio")));
				conv.setNome(rs.getString("nome"));
				conv.setAtivo(rs.getInt("ativo"));
			}
		}
		return conv;
	}

	private Statement connect() throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager
				.getConnection("jdbc:postgresql://localhost:5432/escoladeti2018", "postgres", "11223344");
		Statement stmt = con.createStatement();
		return stmt;
	}

	private ResultSet executeQuery(String id) throws Exception {
		Statement stmt = connect();
		String query = "select c.id, c.id_convenio, a.nome, " + "a.id, a.ativo from convenio a "
				+ "inner join plano_de_saude c on a.id = c.id_convenio " + "group by c.id, a.id having c.id = '"
				+ id + "'";
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}

}
