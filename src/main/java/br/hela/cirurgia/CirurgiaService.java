package br.hela.cirurgia;

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

import br.hela.cirurgia.cirurgia_medicamento.Cirurgia_Medicamento;
import br.hela.cirurgia.cirurgia_medicamento.Cirurgia_Medicamento_Service;
import br.hela.cirurgia.comandos.BuscarCirurgia;
import br.hela.cirurgia.comandos.CriarCirurgia;
import br.hela.cirurgia.comandos.EditarCirurgia;
import br.hela.medicamento.Medicamento;
import br.hela.medicamento.MedicamentoId;
import br.hela.medicamento.MedicamentoService;

@Service
@Transactional
public class CirurgiaService {
	@Autowired
	private CirurgiaRepository cirurgiaRepo;

	@Autowired
	private Cirurgia_Medicamento_Service service;

	@Autowired
	private MedicamentoService medicamentoService;

	public Optional<CirurgiaId> salvar(CriarCirurgia comando) throws NullPointerException {
		Cirurgia novo = cirurgiaRepo.save(new Cirurgia(comando));
		for (MedicamentoId id_medicamento : comando.getId_medicamentos()) {
			do {
				if (verificaMedicamentoExistente(id_medicamento)) {
					Cirurgia_Medicamento cirurgiaMedicamento = new Cirurgia_Medicamento();
					cirurgiaMedicamento.setIdCirurgia(novo.getIdCirurgia());
					cirurgiaMedicamento.setIdMedicamento(id_medicamento);
					service.salvar(cirurgiaMedicamento);
				}
			} while (verificarMedicamentoÚnico(id_medicamento, comando.getId_medicamentos()));
		}
		return Optional.of(novo.getIdCirurgia());
	}

	public Optional<BuscarCirurgia> encontrar(CirurgiaId cirurgiaId) throws Exception {
		ResultSet rs = executeQuery(cirurgiaId.toString());
		BuscarCirurgia cirurgia = new BuscarCirurgia(cirurgiaRepo.findById(cirurgiaId).get());
		String id = cirurgiaId.toString();
		cirurgia.setMedicamentos(medicamentos(rs, id));
		return Optional.of(cirurgia);
	}

	public Optional<List<BuscarCirurgia>> encontrar() throws Exception {
		List<Cirurgia> cirurgias = cirurgiaRepo.findAll();
		List<BuscarCirurgia> rsCirurgias = new ArrayList<>();
		for (Cirurgia cirurgia : cirurgias) {
			ResultSet rs = executeQuery(cirurgia.getIdCirurgia().toString());
			BuscarCirurgia nova = new BuscarCirurgia(cirurgia);
			nova.setMedicamentos(medicamentos(rs, cirurgia.getIdCirurgia().toString()));
			rsCirurgias.add(nova);
		}
		return Optional.of(rsCirurgias);
	}

	public Optional<CirurgiaId> alterar(EditarCirurgia comando) {
		Optional<Cirurgia> optional = cirurgiaRepo.findById(comando.getIdCirurgia());
		if (optional.isPresent()) {
			Cirurgia cirurgia = optional.get();
			cirurgia.apply(comando);
			cirurgiaRepo.save(cirurgia);
			for (MedicamentoId id_medicamento : comando.getId_medicamentos()) {
				if (verificaMedicamentoExistente(id_medicamento)) {
					Cirurgia_Medicamento cirurgiaMedicamento = new Cirurgia_Medicamento();
					cirurgiaMedicamento.setIdCirurgia(comando.getIdCirurgia());
					cirurgiaMedicamento.setIdMedicamento(id_medicamento);
					service.salvar(cirurgiaMedicamento);
				}
			}
			return Optional.of(comando.getIdCirurgia());
		}
		return Optional.empty();
	}

	private List<Medicamento> medicamentos(ResultSet rs, String id) throws Exception {
		List<Medicamento> meds = new ArrayList<>();
		while (rs.next()) {
			String idCirurgia = rs.getString("id");
			if (id.equals(idCirurgia)) {
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

	private boolean verificaMedicamentoExistente(MedicamentoId id) {
		if (!medicamentoService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean verificarMedicamentoÚnico(MedicamentoId id_medicamento, List<MedicamentoId> list) {
		for (MedicamentoId medicamentoId : list) {
			if (medicamentoId.equals(id_medicamento)) {
				return false;
			}
		}
		return true;
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
		String query = "select c.id, a.nome_medicamento, "
				+ "a.composicao, a.id_medicamento, a.ativo from medicamento a "
				+ "inner join cirurgia_medicamento b on a.id_medicamento = b.id_medicamento "
				+ "inner join cirurgia c on b.id = c.id " 
				+ "group by c.id, a.id_medicamento having c.id = '" 
				+ id + "' " + "order by c.tipo_cirurgia";
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}
}
