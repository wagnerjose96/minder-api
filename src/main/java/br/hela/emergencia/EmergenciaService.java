package br.hela.emergencia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.emergencia.comandos.BuscarEmergencia;
import br.hela.emergencia.comandos.CriarEmergencia;
import br.hela.emergencia.comandos.EditarEmergencia;
import br.hela.telefone.TelefoneId;
import br.hela.telefone.comandos.BuscarTelefone;
import br.hela.contato.ContatoId;
import br.hela.contato.comandos.BuscarContato;

@Service
@Transactional
public class EmergenciaService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sql = "select e.id_emergencia, b.id_contato, c.id, c.nome, t.ddd, t.numero, t.id as id_telefone "
			+ "from emergencia e " + "inner join contato_emergencia b on e.id_emergencia = b.id_emergencia "
			+ "inner join contato c on c.id = b.id_contato " + "inner join telefone t on c.id_telefone = t.id "
			+ "group by e.id_emergencia, c.id, b.id_contato, t.id having e.id_emergencia = ? ";

	@Autowired
	private EmergenciaRepository repo;

	public Optional<EmergenciaId> salvar(CriarEmergencia comando) {
		Emergencia novo = repo.save(new Emergencia(comando));
		return Optional.of(novo.getIdEmergencia());
	}

	public Optional<BuscarEmergencia> encontrar(EmergenciaId id) {
		List<BuscarContato> contatos = executeQuery(id.toString(), sql);
		Optional<Emergencia> emergencia = repo.findById(id);
		if (emergencia.isPresent()) {
			BuscarEmergencia resultado = new BuscarEmergencia(emergencia.get());
			resultado.setContatos(contatos);
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<List<BuscarEmergencia>> encontrar() {
		List<BuscarEmergencia> resultados = new ArrayList<>();
		List<Emergencia> emergencias = repo.findAll();
		for (Emergencia emergencia : emergencias) {
			List<BuscarContato> contatos = executeQuery(emergencia.getIdEmergencia().toString(), sql);
			BuscarEmergencia emer = new BuscarEmergencia(emergencia);
			emer.setContatos(contatos);
			resultados.add(emer);
		}
		return Optional.of(resultados);
	}

	public Optional<EmergenciaId> alterar(EditarEmergencia comando) {
		Optional<Emergencia> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			Emergencia emer = optional.get();
			emer.apply(comando);
			repo.save(emer);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

	private List<BuscarContato> executeQuery(String id, String sql) {
		return jdbcTemplate.query(sql, new Object[] { id }, (rs, rowNum) -> {
			BuscarContato contato = new BuscarContato();
			BuscarTelefone telefone = new BuscarTelefone();
			String idEmergencia = rs.getString("id_emergencia");
			if (id.equals(idEmergencia)) {
				contato.setId(new ContatoId(rs.getString("id")));
				contato.setNome(rs.getString("nome"));
				telefone.setId(new TelefoneId(rs.getString("id_telefone")));
				telefone.setDdd(rs.getInt("ddd"));
				telefone.setNumero(rs.getInt("numero"));
				contato.setTelefone(telefone);
			}
			return contato;
		});
	}
}