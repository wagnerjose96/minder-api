package br.hela.doenca;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.doenca.Doenca;
import br.hela.doenca.DoencaId;
import br.hela.doenca.doenca_medicamento.Doenca_Medicamento;
import br.hela.doenca.doenca_medicamento.Doenca_Medicamento_Service;
import br.hela.doenca.comandos.BuscarDoenca;
import br.hela.doenca.comandos.CriarDoenca;
import br.hela.doenca.comandos.EditarDoenca;
import br.hela.medicamento.Medicamento;
import br.hela.medicamento.MedicamentoId;
import br.hela.medicamento.MedicamentoService;

@Service
@Transactional
public class DoencaService {
	@Autowired
	private DoencaRepository doencaRepo;

	@Autowired
	private Doenca_Medicamento_Service service;

	@Autowired
	private MedicamentoService medicamentoService;

	public Optional<DoencaId> salvar(CriarDoenca comando) throws NullPointerException {
		Doenca novo = doencaRepo.save(new Doenca(comando));
		for (MedicamentoId id_medicamento : comando.getId_medicamentos()) {
			do {
				if (verificaMedicamentoExistente(id_medicamento)) {
					Doenca_Medicamento doencaMedicamento = new Doenca_Medicamento();
					doencaMedicamento.setIdDoenca(novo.getIdDoenca());
					doencaMedicamento.setIdMedicamento(id_medicamento);
					service.salvar(doencaMedicamento);
				}
			} while (verificarMedicamentoÚnico(id_medicamento, comando.getId_medicamentos()));
		}
		return Optional.of(novo.getIdDoenca());
	}

	public Optional<BuscarDoenca> encontrar(DoencaId doencaId) throws Exception {
		ResultSet rs = executeQuery(doencaId.toString());
		BuscarDoenca doenca = new BuscarDoenca(doencaRepo.findById(doencaId).get());
		String id = doencaId.toString();
		doenca.setMedicamentos(medicamentos(rs, id));
		return Optional.of(doenca);
	}

	public Optional<List<BuscarDoenca>> encontrar() throws Exception {
		List<Doenca> doencas = doencaRepo.findAll();
		List<BuscarDoenca> rsDoencas = new ArrayList<>();
		for (Doenca doenca : doencas) {
			ResultSet rs = executeQuery(doenca.getIdDoenca().toString());
			BuscarDoenca nova = new BuscarDoenca(doenca);
			nova.setMedicamentos(medicamentos(rs, doenca.getIdDoenca().toString()));
			rsDoencas.add(nova);
		}
		return Optional.of(rsDoencas);
	}

	public Optional<DoencaId> alterar(EditarDoenca comando) {
		Optional<Doenca> optional = doencaRepo.findById(comando.getIdDoenca());
		if (optional.isPresent()) {
			Doenca doenca = optional.get();
			doenca.apply(comando);
			doencaRepo.save(doenca);
			for (MedicamentoId id_medicamento : comando.getId_medicamentos()) {
				if (verificaMedicamentoExistente(id_medicamento)) {
					Doenca_Medicamento doencaMedicamento = new Doenca_Medicamento();
					doencaMedicamento.setIdDoenca(comando.getIdDoenca());
					doencaMedicamento.setIdMedicamento(id_medicamento);
					service.salvar(doencaMedicamento);
				}
			}
			return Optional.of(comando.getIdDoenca());
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

	private List<Medicamento> medicamentos(ResultSet rs, String id) throws Exception {
		List<Medicamento> meds = new ArrayList<>();
		while (rs.next()) {
			String idDoenca = rs.getString("id_doenca");
			if (id.equals(idDoenca)) {
				Medicamento med = new Medicamento();
				med.setIdMedicamento(new MedicamentoId(rs.getString("id_medicamento")));
				med.setNomeMedicamento(rs.getString("nome_medicamento"));
				med.setComposicao(rs.getString("composicao"));
				med.setAtivo(rs.getInt("ativo"));
				meds.add(med);
			}
		}
		return meds;
	}

	private Statement connect() throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/escoladeti2018", "postgres",
				"11223344");
		Statement stmt = con.createStatement();
		return stmt;
	}

	private boolean verificarMedicamentoÚnico(MedicamentoId id_medicamento, List<MedicamentoId> list) {
		for (MedicamentoId medicamentoId : list) {
			if (medicamentoId.equals(id_medicamento)) {
				return false;
			}
		}
		return true;
	}

	private ResultSet executeQuery(String id) throws Exception {
		Statement stmt = connect();
		String query = "select c.id_doenca, a.nome_medicamento, "
				+ "a.composicao, a.id_medicamento, a.ativo from medicamento a "
				+ "inner join doenca_medicamento b on a.id_medicamento = b.id_medicamento "
				+ "inner join doenca c on b.id_doenca = c.id_doenca "
				+ "group by c.id_doenca, a.id_medicamento having c.id_doenca = '" + id + "'";
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}

}
