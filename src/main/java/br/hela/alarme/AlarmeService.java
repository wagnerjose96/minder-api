package br.hela.alarme;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

	@Autowired
	JdbcTemplate jdbcTemplate;

	public void retorno(String id) {

		List<BuscarAlarme> buscarAlarmes = jdbcTemplate.query("select c.id, c.id_medicamento, a.nome_medicamento, "
				+ "a.composicao, a.id_medicamento, a.ativo from medicamento a "
				+ "inner join alarme c on c.id_medicamento = a.id_medicamento "
				+ "group by c.id, a.id_medicamento having c.id = ?", new Object[] { id }, (rs, rowNum) -> {
					BuscarAlarme alarme = new BuscarAlarme();
					rs.getString("nome");

					return alarme;
				});

		System.out.println(buscarAlarmes);
	}

	public Optional<AlarmeId> salvar(CriarAlarme comando) {
		Alarme novo = repo.save(new Alarme(comando));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarAlarme> encontrar(AlarmeId alarmeId) throws Exception {
		BuscarAlarme alarme = new BuscarAlarme(repo.findById(alarmeId).get());
		List<Medicamento> medicamentos = executeQuery(alarmeId.toString());
		if (medicamentos.get(0).getAtivo() != 0) {
			alarme.setMedicamento(medicamentos.get(0));
		}
		return Optional.of(alarme);
	}

	public Optional<List<BuscarAlarme>> encontrar() throws Exception {
		List<BuscarAlarme> rsAlarmes = new ArrayList<>();
		List<Alarme> alarmes = repo.findAll();
		for (Alarme alarme : alarmes) {
			BuscarAlarme nova = new BuscarAlarme(alarme);
			List<Medicamento> medicamentos = executeQuery(nova.getId().toString());
			if (medicamentos.get(0).getAtivo() != 0) {
				nova.setMedicamento(medicamentos.get(0));
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

	public Optional<String> deletar(AlarmeId id) {
		if (repo.findById(id).isPresent()) {
			repo.deleteById(id);
			return Optional.of("Alarme " + id + " deletado com sucesso");
		}
		return Optional.empty();
	}

	private List<Medicamento> executeQuery(String id) {
		List<Medicamento> medicamentos = jdbcTemplate.query("select c.id, c.id_medicamento, a.nome_medicamento, "
				+ "a.composicao, a.id_medicamento, a.ativo from medicamento a "
				+ "inner join alarme c on c.id_medicamento = a.id_medicamento "
				+ "group by c.id, a.id_medicamento having c.id = ?", new Object[] { id }, (rs, rowNum) -> {
					Medicamento med = new Medicamento();
					String idAlarme = rs.getString("id");
					if (id.equals(idAlarme)) {
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
