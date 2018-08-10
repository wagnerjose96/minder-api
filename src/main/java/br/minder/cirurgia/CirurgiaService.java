package br.minder.cirurgia;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.minder.cirurgia.cirurgia_medicamento.CirurgiaMedicamento;
import br.minder.cirurgia.cirurgia_medicamento.CirurgiaMedicamentoService;
import br.minder.cirurgia.comandos.BuscarCirurgia;
import br.minder.cirurgia.comandos.CriarCirurgia;
import br.minder.cirurgia.comandos.EditarCirurgia;
import br.minder.medicamento.MedicamentoId;
import br.minder.medicamento.MedicamentoService;
import br.minder.medicamento.comandos.BuscarMedicamento;
import br.minder.usuario.UsuarioId;

@Service
@Transactional
public class CirurgiaService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sql = "select c.id, a.nome_medicamento, "
			+ "a.composicao, a.id_medicamento, a.ativo from medicamento a "
			+ "inner join cirurgia_medicamento b on a.id_medicamento = b.id_medicamento "
			+ "inner join cirurgia c on b.id_cirurgia = c.id group by c.id, a.id_medicamento having c.id = ?";

	@Autowired
	private CirurgiaRepository cirurgiaRepo;

	@Autowired
	private CirurgiaMedicamentoService service;

	@Autowired
	private MedicamentoService medService;

	public Optional<CirurgiaId> salvar(CriarCirurgia comando, UsuarioId id) {
		if (comando.getTipoCirurgia() != null && comando.getClinicaResponsavel() != null
				&& comando.getDataCirurgia() != null && comando.getIdMedicamentos() != null
				&& comando.getMedicoResponsavel() != null) {
			Cirurgia novo = cirurgiaRepo.save(new Cirurgia(comando, id));
			for (MedicamentoId idMedicamento : comando.getIdMedicamentos()) {
				if (medService.encontrar(idMedicamento).isPresent()) {
					CirurgiaMedicamento cirurgiaMedicamento = new CirurgiaMedicamento();
					cirurgiaMedicamento.setIdCirurgia(novo.getIdCirurgia());
					cirurgiaMedicamento.setIdMedicamento(idMedicamento);
					service.salvar(cirurgiaMedicamento);
				}
			}
			return Optional.of(novo.getIdCirurgia());
		}
		return Optional.empty();
	}

	public Optional<BuscarCirurgia> encontrar(CirurgiaId cirurgiaId, UsuarioId id) {
		List<BuscarMedicamento> medicamentos = executeQuery(cirurgiaId.toString(), sql);
		Optional<Cirurgia> result = cirurgiaRepo.findById(cirurgiaId);
		if (result.isPresent() && id.toString().equals(result.get().getIdUsuario().toString())) {
			BuscarCirurgia resultado = new BuscarCirurgia(result.get());
			resultado.setMedicamentos(medicamentos);
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<CirurgiaId> alterar(EditarCirurgia comando) {
		Optional<Cirurgia> optional = cirurgiaRepo.findById(comando.getIdCirurgia());
		if (optional.isPresent() && comando.getTipoCirurgia() != null && comando.getClinicaResponsavel() != null
				&& comando.getDataCirurgia() != null && comando.getIdMedicamentos() != null
				&& comando.getMedicoResponsavel() != null) {
			Cirurgia cirurgia = optional.get();
			cirurgia.apply(comando);
			cirurgiaRepo.save(cirurgia);
			for (MedicamentoId idMedicamento : comando.getIdMedicamentos()) {
				if (medService.encontrar(idMedicamento).isPresent()) {
					CirurgiaMedicamento cirurgiaMedicamento = new CirurgiaMedicamento();
					cirurgiaMedicamento.setIdCirurgia(comando.getIdCirurgia());
					cirurgiaMedicamento.setIdMedicamento(idMedicamento);
					service.salvar(cirurgiaMedicamento);
				}
			}
			return Optional.of(comando.getIdCirurgia());
		}
		return Optional.empty();
	}

	private List<BuscarMedicamento> executeQuery(String id, String sql) {
		return jdbcTemplate.query(sql, new Object[] { id }, (rs, rowNum) -> {
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
	}

}
