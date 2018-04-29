package br.hela.alarme;

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
import br.hela.alarme.Alarme;
import br.hela.alarme.AlarmeId;
import br.hela.alarme.comandos.BuscarAlarme;
import br.hela.alarme.comandos.CriarAlarme;
import br.hela.alarme.comandos.EditarAlarme;
import br.hela.medicamento.Medicamento;
import br.hela.medicamento.MedicamentoId;

@Service
@Transactional
public class AlarmeService {

	@Autowired
	private AlarmeRepository repo;
	
	public Optional<String> deletar(AlarmeId id) {
		repo.deleteById(id);
		return Optional.of("Alarme ==> " + id + " deletado com sucesso!");
	}

	public Optional<AlarmeId> salvar(CriarAlarme comando) {
		Alarme novo = repo.save(new Alarme(comando));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarAlarme> encontrar(AlarmeId alarmeId) throws Exception {
		BuscarAlarme alarme = new BuscarAlarme(repo.findById(alarmeId).get());
		ResultSet rs = executeQuery(alarmeId.toString());
		Medicamento med = medicamento(rs, alarmeId.toString());
		if (med.getAtivo() != 0) {
			alarme.setMedicamento(med);
		}
		return Optional.of(alarme);
	}

	public Optional<List<BuscarAlarme>> encontrar() throws Exception {
		List<BuscarAlarme> rsAlarmes = new ArrayList<>();
		List<Alarme> alarmes = repo.findAll();
		for (Alarme alarme : alarmes) {
			ResultSet rs = executeQuery(alarme.getId().toString());
			BuscarAlarme nova = new BuscarAlarme(alarme);
			Medicamento med = medicamento(rs, alarme.getId().toString());
			if (med.getAtivo() != 0) {
				nova.setMedicamento(med);
			}
			rsAlarmes.add(nova);
		}
		return Optional.of(rsAlarmes);
	}

	public Optional<AlarmeId> alterar(EditarAlarme comando) {
		Optional<Alarme> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			Alarme alarme = optional.get();
			alarme.apply(comando);
			repo.save(alarme);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

	private Medicamento medicamento(ResultSet rs, String id) throws Exception {
		Medicamento med = new Medicamento();
		while (rs.next()) {
			String idAlarme = rs.getString("id");
			if (id.equals(idAlarme)) {
				med.setIdMedicamento(new MedicamentoId(rs.getString("id_medicamento")));
				med.setNomeMedicamento(rs.getString("nome_medicamento"));
				med.setComposicao(rs.getString("composicao"));
				med.setAtivo(rs.getInt("ativo"));
			}
		}
		return med;
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
		String query = "select c.id, a.nome_medicamento, "
				+ "a.composicao, a.id_medicamento, a.ativo from medicamento a "
				+ "inner join alarme c on a.id_medicamento = c.id_medicamento " + "group by c.id, a.id_medicamento having c.id = '" + id
				+ "'";
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}

}
