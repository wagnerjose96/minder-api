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
import br.hela.contato.ContatoId;
import br.hela.contato.comandos.BuscarContato;

@Service
@Transactional
public class EmergenciaService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sql = "select e.id_emergencia, c.id, c.nome, " + "c.ddd, c.numero from contato c "
			+ "inner join contato_emergencia b on c.id = b.id_contato "
			+ "inner join emergencia e on b.id_emergencia = e.id_emergencia "
			+ "group by e.id_emergencia, c.id having e.id_emergencia = ? ";

	@Autowired
	private EmergenciaRepository repo;

	public Optional<EmergenciaId> salvar(CriarEmergencia comando) {
		Emergencia novo = repo.save(new Emergencia(comando));
		return Optional.of(novo.getIdEmergencia());
	}

	public Optional<BuscarEmergencia> encontrar(EmergenciaId id) {
		List<BuscarContato> contatos = executeQuery(id.toString(), sql);
		Emergencia emergencia = repo.findById(id).get();
		BuscarEmergencia resultado = new BuscarEmergencia(emergencia);
		resultado.setContatos(contatos);
		return Optional.of(resultado);
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
		List<BuscarContato> contatos = jdbcTemplate.query(sql, new Object[] { id }, (rs, rowNum) -> {
			BuscarContato contato = new BuscarContato();
			String idEmergencia = rs.getString("id_emergencia");
			if (id.equals(idEmergencia)) {
				contato.setId(new ContatoId(rs.getString("id")));
				contato.setNome(rs.getString("nome"));
				contato.setDdd(rs.getInt("ddd"));
				contato.setNumero(rs.getInt("numero"));
			}
			return contato;
		});
		return contatos;
	}
}