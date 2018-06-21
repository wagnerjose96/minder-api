package br.hela.alergia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import br.hela.alergia.Alergia;
import br.hela.alergia.AlergiaId;
import br.hela.alergia.alergia_medicamento.Alergia_Medicamento;
import br.hela.alergia.alergia_medicamento.Alergia_Medicamento_Service;
import br.hela.alergia.comandos.BuscarAlergia;
import br.hela.alergia.comandos.CriarAlergia;
import br.hela.alergia.comandos.EditarAlergia;
import br.hela.medicamento.MedicamentoId;
import br.hela.medicamento.MedicamentoService;
import br.hela.medicamento.comandos.BuscarMedicamento;

@Service
@Transactional
public class AlergiaService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private String sql = "select c.id, a.nome_medicamento, " + "a.composicao, a.id_medicamento, a.ativo from medicamento a "
			+ "inner join alergia_medicamento b on a.id_medicamento = b.id_medicamento "
			+ "inner join alergia c on b.id_alergia = c.id "
			+ "group by c.id, a.id_medicamento having c.id = ? " + "order by c.tipo_alergia";
	
	@Autowired
	private AlergiaRepository repo;

	@Autowired
	private Alergia_Medicamento_Service service;

	@Autowired
	private MedicamentoService medicamentoService;

	public Optional<AlergiaId> salvar(CriarAlergia comando) {
		Alergia novo = repo.save(new Alergia(comando));
		for (MedicamentoId id_medicamento : comando.getId_medicamentos()) {
			do {
				if (verificaMedicamentoExistente(id_medicamento)) {
					Alergia_Medicamento alergiaMedicamento = new Alergia_Medicamento();
					alergiaMedicamento.setIdAlergia(novo.getIdAlergia());
					alergiaMedicamento.setIdMedicamento(id_medicamento);
					service.salvar(alergiaMedicamento);
				}
			} while (verificarMedicamentoÚnico(id_medicamento, comando.getId_medicamentos()));
		}
		return Optional.of(novo.getIdAlergia());
	}

	public Optional<BuscarAlergia> encontrar(AlergiaId alergiaId) {
		List<BuscarMedicamento> medicamentos = executeQuery(alergiaId.toString(), sql);
		BuscarAlergia alergia = new BuscarAlergia(repo.findById(alergiaId).get());
		alergia.setMedicamentos(medicamentos);
		return Optional.of(alergia);
	}

	public Optional<List<BuscarAlergia>> encontrar() {
		List<Alergia> alergias = repo.findAll();
		List<BuscarAlergia> rsAlergias = new ArrayList<>();
		for (Alergia alergia : alergias) {
			List<BuscarMedicamento> medicamentos = executeQuery(alergia.getIdAlergia().toString(), sql);
			BuscarAlergia nova = new BuscarAlergia(alergia);
			nova.setMedicamentos(medicamentos);
			rsAlergias.add(nova);
		}
		return Optional.of(rsAlergias);
	}

	public Optional<AlergiaId> alterar(EditarAlergia comando) {
		Optional<Alergia> optional = repo.findById(comando.getIdAlergia());
		if (optional.isPresent()) {
			Alergia alergia = optional.get();
			alergia.apply(comando);
			repo.save(alergia);
			for (MedicamentoId id_medicamento : comando.getId_medicamentos()) {
				if (verificaMedicamentoExistente(id_medicamento)) {
					Alergia_Medicamento alergiaMedicamento = new Alergia_Medicamento();
					alergiaMedicamento.setIdAlergia(comando.getIdAlergia());
					alergiaMedicamento.setIdMedicamento(id_medicamento);
					service.salvar(alergiaMedicamento);
				}
			}
			return Optional.of(comando.getIdAlergia());
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
			String idAlergia = rs.getString("id");
			if (id.equals(idAlergia)) {
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
