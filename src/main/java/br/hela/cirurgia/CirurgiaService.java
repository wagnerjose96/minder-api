package br.hela.cirurgia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.cirurgia.cirurgia_medicamento.Cirurgia_Medicamento;
import br.hela.cirurgia.cirurgia_medicamento.Cirurgia_Medicamento_Service;
import br.hela.cirurgia.comandos.BuscarCirurgia;
import br.hela.cirurgia.comandos.CriarCirurgia;
import br.hela.cirurgia.comandos.EditarCirurgia;
import br.hela.medicamento.MedicamentoId;
import br.hela.medicamento.MedicamentoService;
import br.hela.medicamento.comandos.BuscarMedicamento;

@Service
@Transactional
public class CirurgiaService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private String sql = "select c.id, a.nome_medicamento, " + "a.composicao, a.id_medicamento, a.ativo from medicamento a "
			+ "inner join cirurgia_medicamento b on a.id_medicamento = b.id_medicamento "
			+ "inner join cirurgia c on b.id_cirurgia = c.id group by c.id, a.id_medicamento having c.id = ?";
	
	@Autowired
	private CirurgiaRepository cirurgiaRepo;

	@Autowired
	private Cirurgia_Medicamento_Service service;

	@Autowired
	private MedicamentoService medicamentoService;

	public Optional<CirurgiaId> salvar(CriarCirurgia comando) {
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

	public Optional<BuscarCirurgia> encontrar(CirurgiaId cirurgiaId) {
		List<BuscarMedicamento> medicamentos = executeQuery(cirurgiaId.toString(), sql);
		BuscarCirurgia cirurgia = new BuscarCirurgia(cirurgiaRepo.findById(cirurgiaId).get());
		cirurgia.setMedicamentos(medicamentos);
		return Optional.of(cirurgia);
	}

	public Optional<List<BuscarCirurgia>> encontrar() {
		List<Cirurgia> cirurgias = cirurgiaRepo.findAll();
		List<BuscarCirurgia> rsCirurgias = new ArrayList<>();
		for (Cirurgia cirurgia : cirurgias) {
			List<BuscarMedicamento> medicamentos = executeQuery(cirurgia.getIdCirurgia().toString(), sql);
			BuscarCirurgia nova = new BuscarCirurgia(cirurgia);
			nova.setMedicamentos(medicamentos);
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
	
	private List<BuscarMedicamento> executeQuery(String id, String sql) {
		List<BuscarMedicamento> medicamentos = jdbcTemplate.query(sql, new Object[] { id }, (rs, rowNum) -> {
			BuscarMedicamento med = new BuscarMedicamento();
			String idCirurgia = rs.getString("id");
			if (id.equals(idCirurgia)) {
				med.setIdMedicamento(new MedicamentoId(rs.getString("id_medicamento")));
				med.setNomeMedicamento(rs.getString("nome_medicamento"));
				med.setComposicao(rs.getString("composicao"));
				med.setAtivo(rs.getInt("ativo"));
			}
			return med;
		});
		return medicamentos;
	}

}
