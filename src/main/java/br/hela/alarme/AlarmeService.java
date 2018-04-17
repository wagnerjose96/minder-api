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
import br.hela.alarme.alarme_medicamento.Alarme_Medicamento;
import br.hela.alarme.alarme_medicamento.Alarme_Medicamento_Service;
import br.hela.alarme.comandos.BuscarAlarme;
import br.hela.alarme.comandos.CriarAlarme;
import br.hela.alarme.comandos.EditarAlarme;
import br.hela.medicamento.Medicamento;
import br.hela.medicamento.MedicamentoId;
import br.hela.medicamento.MedicamentoService;

@Service
@Transactional
public class AlarmeService {

	@Autowired
	private AlarmeRepository repo;

	@Autowired
	private Alarme_Medicamento_Service service;

	@Autowired
	private MedicamentoService medicamentoService;

	public Optional<AlarmeId> salvar(CriarAlarme comando) throws NullPointerException {
		Alarme novo = repo.save(new Alarme(comando));
		if (verificaMedicamentoExistente(comando.getIdMedicamento())) {
			Alarme_Medicamento alarmeMedicamento = new Alarme_Medicamento();
			alarmeMedicamento.setIdAlarme(novo.getId());
			alarmeMedicamento.setIdMedicamento(comando.getIdMedicamento());
			service.salvar(alarmeMedicamento);
		}
		return Optional.of(novo.getId());
	}

	public Optional<BuscarAlarme> encontrar(AlarmeId alarmeId) throws Exception {
		Statement stmt = connect();
		String query = "select c.id, a.nome_medicamento, "
				+ "a.composicao, a.id_medicamento, a.ativo from medicamento a "
				+ "inner join alarme_medicamento b on a.id_medicamento = b.id_medicamento "
				+ "inner join alarme c on b.id_alarme = c.id " + "group by c.id, a.id_medicamento having c.id = '"
				+ alarmeId.toString() + "'";
		ResultSet rs = stmt.executeQuery(query);
		BuscarAlarme alarme = new BuscarAlarme(repo.findById(alarmeId).get());
		String id = alarmeId.toString();
		alarme.setMedicamento(medicamento(rs, id));
		return Optional.of(alarme);
	}

	public Optional<List<BuscarAlarme>> encontrar() throws Exception {
		Statement stmt = connect();
		String query = "select c.id, a.nome_medicamento, "
				+ "a.composicao, a.id_medicamento, a.ativo from medicamento a "
				+ "inner join alarme_medicamento b on a.id_medicamento = b.id_medicamento "
				+ "inner join alarme c on b.id_alarme = c.id "
				+ "group by c.id, a.id_medicamento";
		List<Alarme> alarmes = repo.findAll();
		List<BuscarAlarme> rsAlarmes = new ArrayList<>();
		for (Alarme alarme : alarmes) {
			ResultSet rs = stmt.executeQuery(query);
			BuscarAlarme nova = new BuscarAlarme(alarme);
			String id = alarme.getId().toString();
			nova.setMedicamento(medicamento(rs, id));
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
				if (verificaMedicamentoExistente(comando.getIdMedicamento())) {
					Alarme_Medicamento alarmeMedicamento = new Alarme_Medicamento();
					alarmeMedicamento.setIdAlarme(comando.getId());
					alarmeMedicamento.setIdMedicamento(comando.getIdMedicamento());
					service.salvar(alarmeMedicamento);
				}
			
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

	private boolean verificaMedicamentoExistente(MedicamentoId id) {
		if (!medicamentoService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
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

}
