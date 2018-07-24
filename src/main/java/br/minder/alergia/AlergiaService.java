package br.minder.alergia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import br.minder.alergia.Alergia;
import br.minder.alergia.AlergiaId;
import br.minder.alergia.alergia_medicamento.AlergiaMedicamento;
import br.minder.alergia.alergia_medicamento.AlergiaMedicamentoService;
import br.minder.alergia.comandos.BuscarAlergia;
import br.minder.alergia.comandos.CriarAlergia;
import br.minder.alergia.comandos.EditarAlergia;
import br.minder.medicamento.MedicamentoId;
import br.minder.medicamento.MedicamentoService;
import br.minder.medicamento.comandos.BuscarMedicamento;
import br.minder.usuario.UsuarioId;

@Service
@Transactional
public class AlergiaService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sql = "select c.id, a.nome_medicamento, "
			+ "a.composicao, a.id_medicamento, a.ativo from medicamento a "
			+ "inner join alergia_medicamento b on a.id_medicamento = b.id_medicamento "
			+ "inner join alergia c on b.id_alergia = c.id " + "group by c.id, a.id_medicamento having c.id = ? "
			+ "order by c.tipo_alergia";

	@Autowired
	private AlergiaRepository repo;

	@Autowired
	private AlergiaMedicamentoService service;

	@Autowired
	private MedicamentoService medService;

	public Optional<AlergiaId> salvar(CriarAlergia comando, UsuarioId id) {
		if (comando.getDataDescoberta() != null && comando.getTipoAlergia() != null
				&& comando.getIdMedicamentos() != null && comando.getEfeitos() != null
				&& comando.getLocalAfetado() != null) {
			Alergia novo = repo.save(new Alergia(comando, id));
			for (MedicamentoId idMedicamento : comando.getIdMedicamentos()) {
				if (medService.encontrar(idMedicamento).isPresent()) {
					AlergiaMedicamento alergiaMedicamento = new AlergiaMedicamento();
					alergiaMedicamento.setIdAlergia(novo.getIdAlergia());
					alergiaMedicamento.setIdMedicamento(idMedicamento);
					service.salvar(alergiaMedicamento);
				}
			}
			return Optional.of(novo.getIdAlergia());
		}
		return Optional.empty();
	}

	public Optional<BuscarAlergia> encontrar(AlergiaId alergiaId, UsuarioId id) {
		List<BuscarMedicamento> medicamentos = executeQuery(alergiaId.toString(), sql);
		Optional<Alergia> result = repo.findById(alergiaId);
		if (result.isPresent() && id.toString().equals(result.get().getIdUsuario().toString())) {
			BuscarAlergia resultado = new BuscarAlergia(result.get());
			resultado.setMedicamentos(medicamentos);
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<List<BuscarAlergia>> encontrar(UsuarioId id) {
		List<Alergia> alergias = repo.findAll();
		List<BuscarAlergia> rsAlergias = new ArrayList<>();
		if (!alergias.isEmpty()) {
			for (Alergia alergia : alergias) {
				if (id.toString().equals(alergia.getIdUsuario().toString())) {
					List<BuscarMedicamento> medicamentos = executeQuery(alergia.getIdAlergia().toString(), sql);
					BuscarAlergia nova = new BuscarAlergia(alergia);
					nova.setMedicamentos(medicamentos);
					rsAlergias.add(nova);
				}
			}
			return Optional.of(rsAlergias);
		}
		return Optional.empty();
	}

	public Optional<AlergiaId> alterar(EditarAlergia comando) {
		if (comando.getDataDescoberta() != null && comando.getTipoAlergia() != null
				&& comando.getIdMedicamentos() != null && comando.getEfeitos() != null
				&& comando.getLocalAfetado() != null) {
			Optional<Alergia> optional = repo.findById(comando.getIdAlergia());
			if (optional.isPresent()) {
				Alergia alergia = optional.get();
				alergia.apply(comando);
				repo.save(alergia);
				for (MedicamentoId idMedicamento : comando.getIdMedicamentos()) {
					if (medService.encontrar(idMedicamento).isPresent()) {
						AlergiaMedicamento alergiaMedicamento = new AlergiaMedicamento();
						alergiaMedicamento.setIdAlergia(comando.getIdAlergia());
						alergiaMedicamento.setIdMedicamento(idMedicamento);
						service.salvar(alergiaMedicamento);
					}
				}
				return Optional.of(comando.getIdAlergia());
			}
		}
		return Optional.empty();
	}

	private List<BuscarMedicamento> executeQuery(String id, String sql) {
		return jdbcTemplate.query(sql, new Object[] { id }, (rs, rowNum) -> {
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
	}

}
