package br.minder.doenca;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.minder.doenca.Doenca;
import br.minder.doenca.DoencaId;
import br.minder.doenca.comandos.BuscarDoenca;
import br.minder.doenca.comandos.CriarDoenca;
import br.minder.doenca.comandos.EditarDoenca;
import br.minder.doenca.doenca_medicamento.DoencaMedicamento;
import br.minder.doenca.doenca_medicamento.DoencaMedicamentoService;
import br.minder.medicamento.MedicamentoId;
import br.minder.medicamento.MedicamentoService;
import br.minder.medicamento.comandos.BuscarMedicamento;
import br.minder.usuario.UsuarioId;

@Service
@Transactional
public class DoencaService {
	private String sql = "select c.id_doenca, a.nome_medicamento, "
			+ "a.composicao, a.id_medicamento, a.ativo from medicamento a "
			+ "inner join doenca_medicamento b on a.id_medicamento = b.id_medicamento "
			+ "inner join doenca c on b.id_doenca = c.id_doenca "
			+ "group by c.id_doenca, a.id_medicamento having c.id_doenca = ?";

	@Autowired
	private DoencaRepository doencaRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DoencaMedicamentoService service;

	@Autowired
	private MedicamentoService medService;

	public Optional<DoencaId> salvar(CriarDoenca comando, UsuarioId id) {
		if (comando.getDataDescoberta() != null && comando.getNomeDoenca() != null) {
			Doenca novo = doencaRepo.save(new Doenca(comando, id));
			for (MedicamentoId idMedicamento : comando.getIdMedicamentos()) {
				if (medService.encontrar(idMedicamento).isPresent()) {
					DoencaMedicamento doencaMedicamento = new DoencaMedicamento();
					doencaMedicamento.setIdDoenca(novo.getIdDoenca());
					doencaMedicamento.setIdMedicamento(idMedicamento);
					service.salvar(doencaMedicamento);
				}
			}
			return Optional.of(novo.getIdDoenca());
		}
		return Optional.empty();
	}

	public Optional<BuscarDoenca> encontrar(DoencaId doencaId, UsuarioId id) {
		List<BuscarMedicamento> medicamentos = executeQuery(doencaId.toString(), sql);
		Optional<Doenca> doenca = doencaRepo.findById(doencaId);
		if (doenca.isPresent() && id.toString().equals(doenca.get().getIdUsuario().toString())) {
			BuscarDoenca resultado = new BuscarDoenca(doenca.get());
			resultado.setMedicamentos(medicamentos);
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<DoencaId> alterar(EditarDoenca comando) {
		Optional<Doenca> optional = doencaRepo.findById(comando.getIdDoenca());
		if (optional.isPresent() && comando.getNomeDoenca() != null) {
			Doenca doenca = optional.get();
			doenca.apply(comando);
			doencaRepo.save(doenca);
			for (MedicamentoId idMedicamento : comando.getIdMedicamentos()) {
				if (medService.encontrar(idMedicamento).isPresent()) {
					DoencaMedicamento doencaMedicamento = new DoencaMedicamento();
					doencaMedicamento.setIdDoenca(comando.getIdDoenca());
					doencaMedicamento.setIdMedicamento(idMedicamento);
					service.salvar(doencaMedicamento);
				}
			}
			return Optional.of(comando.getIdDoenca());
		}
		return Optional.empty();
	}

	private List<BuscarMedicamento> executeQuery(String id, String sql) {
		return jdbcTemplate.query(sql, new Object[] { id }, (rs, rowNum) -> {
			BuscarMedicamento med = new BuscarMedicamento();
			String idDoenca = rs.getString("id_doenca");
			if (id.equals(idDoenca)) {
				med.setIdMedicamento(new MedicamentoId(rs.getString("id_medicamento")));
				med.setNomeMedicamento(rs.getString("nome_medicamento"));
				med.setComposicao(rs.getString("composicao"));
				med.setAtivo(rs.getInt("ativo"));
			}
			return med;
		});
	}

}
