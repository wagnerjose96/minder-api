package br.hela.alergia;

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

import br.hela.alergia.Alergia;
import br.hela.alergia.AlergiaId;
import br.hela.alergia.alergia_medicamento.Alergia_Medicamento;
import br.hela.alergia.alergia_medicamento.Alergia_Medicamento_Service;
import br.hela.alergia.comandos.BuscarAlergia;
import br.hela.alergia.comandos.CriarAlergia;
import br.hela.alergia.comandos.EditarAlergia;
import br.hela.medicamento.Medicamento;
import br.hela.medicamento.MedicamentoId;
import br.hela.medicamento.MedicamentoService;

@Service
@Transactional
public class AlergiaService {
	private static String driver = "org.postgresql.Driver";
	private static String user = "postgres";
	private static String senha = "11223344";
	private static String url = "jdbc:postgresql://localhost:5432/escoladeti2018";

	@Autowired
	private AlergiaRepository repo;

	@Autowired
	private Alergia_Medicamento_Service service;

	@Autowired
	private MedicamentoService medicamentoService;

	public Optional<AlergiaId> salvar(CriarAlergia comando) throws NullPointerException {
		Alergia novo = repo.save(new Alergia(comando));
		for (MedicamentoId id_medicamento : comando.getId_medicamentos()) {
			if (verificaMedicamentoExistente(id_medicamento)) {
				Alergia_Medicamento alergiaMedicamento = new Alergia_Medicamento();
				alergiaMedicamento.setIdAlergia(novo.getIdAlergia());
				alergiaMedicamento.setIdMedicamento(id_medicamento);
				service.salvar(alergiaMedicamento);
			}
		}
		return Optional.of(novo.getIdAlergia());
	}

	public Optional<Alergia> encontrar(AlergiaId id) {
		return repo.findById(id);
	}

	public Optional<List<BuscarAlergia>> encontrar() throws Exception {
		Class.forName(driver);
		Connection con = DriverManager.getConnection(url, user, senha);
		Statement stmt = con.createStatement();
		String query = "select c.id, c.tipo_alergia, c.local_afetado, c.data_descoberta, "
				+ "c.efeitos, a.nome_medicamento, a.composicao, a.id_medicamento from medicamento a "
				+ "inner join alergia_medicamento b on a.id_medicamento = b.id_medicamento "
				+ "inner join alergia c on b.id_alergia = c.id "
				+ "group by c.id, a.id_medicamento order by c.tipo_alergia";

		List<Alergia> alergias = repo.findAll();
		List<BuscarAlergia> rsAlergias = new ArrayList<>();
		for (Alergia alergia : alergias) {
			ResultSet rs = stmt.executeQuery(query);
			BuscarAlergia nova = new BuscarAlergia(alergia);
			List<Medicamento> meds = new ArrayList<>();
			String idAlergia = alergia.getIdAlergia().toString();
			while (rs.next()) {
				String id = rs.getString("id");
				if(id.equals(idAlergia)) {
					Medicamento med = new Medicamento();
					med.setIdMedicamento(new MedicamentoId(rs.getString("id_medicamento")));
					med.setNomeMedicamento(rs.getString("nome_medicamento"));
					med.setComposicao(rs.getString("composicao"));
					meds.add(med);
				}	
			}
			nova.setMedicamentos(meds);
			rsAlergias.add(nova);
		}
		return Optional.of(rsAlergias);
	}

	public Optional<String> deletar(AlergiaId id) {
		repo.deleteById(id);
		return Optional.of("Alergia -> " + id + ": deletada com sucesso");
	}

	public Optional<AlergiaId> alterar(EditarAlergia comando) {
		Optional<Alergia> optional = repo.findById(comando.getIdAlergia());
		if (optional.isPresent()) {
			Alergia alergia = optional.get();
			alergia.apply(comando);
			repo.save(alergia);
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
}
