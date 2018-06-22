package br.hela.contato;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import br.hela.contato.Contato;
import br.hela.contato.ContatoId;
import br.hela.contato.comandos.BuscarContato;
import br.hela.contato.comandos.CriarContato;
import br.hela.contato.comandos.EditarContato;
import br.hela.contato.contato_emergencia.Contato_Emergencia;
import br.hela.contato.contato_emergencia.Contato_Emergencia_Id;
import br.hela.contato.contato_emergencia.Contato_Emergencia_Repository;
import br.hela.emergencia.EmergenciaId;
import br.hela.emergencia.comandos.BuscarEmergencia;
import br.hela.telefone.TelefoneId;
import br.hela.telefone.TelefoneService;
import br.hela.telefone.comandos.BuscarTelefone;
import br.hela.usuario.UsuarioId;

@Service
@Transactional
public class ContatoService {
	@Autowired
	private ContatoRepository repo;

	@Autowired
	private TelefoneService telefoneService;

	@Autowired
	private Contato_Emergencia_Repository repoContatoEmergencia;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sql = "select e.id_usuario, e.id_emergencia, u.ativo, u.id "
			+ "from usuario u inner join emergencia e " + "on u.id = e.id_usuario "
			+ "group by u.id, e.id_usuario, e.id_emergencia " + "having u.id = ?";

	private String sqlContatoEmergencia = "select e.id_contato, e.id_emergencia, e.id " + "from contato_emergencia e "
			+ "where e.id_emergencia = ? and e.id_contato = ?";

	public Optional<ContatoId> salvar(CriarContato comando, UsuarioId id) {
		if (comando.getTelefone() != null) {
			Optional<TelefoneId> idTelefone = telefoneService.salvar(comando.getTelefone());
			if (idTelefone.isPresent()) {
				Contato novo = new Contato(comando);
				novo.setIdTelefone(idTelefone.get());
				repo.save(novo);
				Contato_Emergencia contatoEmergencia = new Contato_Emergencia();
				List<BuscarEmergencia> emergencia = executeQuery(id.toString(), sql);
				contatoEmergencia.setIdEmergencia(emergencia.get(0).getId());
				contatoEmergencia.setIdContato(novo.getId());
				repoContatoEmergencia.save(contatoEmergencia);
				return Optional.of(novo.getId());
			}
		}
		return Optional.empty();
	}

	public Optional<BuscarContato> encontrar(ContatoId contatoId) {
		Optional<Contato> contato = repo.findById(contatoId);
		if (contato.isPresent()) {
			BuscarContato resultado = new BuscarContato(contato.get());
			Optional<BuscarTelefone> telefone = telefoneService.encontrar(contato.get().getIdTelefone());
			if (telefone.isPresent()) {
				resultado.setTelefone(telefone.get());
				return Optional.of(resultado);
			}
		}
		return Optional.empty();
	}

	public Optional<List<BuscarContato>> encontrar() {
		List<Contato> contatos = repo.findAll();
		List<BuscarContato> resultados = new ArrayList<>();
		for (Contato contato : contatos) {
			BuscarContato nova = new BuscarContato(contato);
			Optional<BuscarTelefone> telefone = telefoneService.encontrar(contato.getIdTelefone());
			if (telefone.isPresent()) {
				nova.setTelefone(telefone.get());
				resultados.add(nova);
			}
		}
		return Optional.of(resultados);
	}

	public Optional<ContatoId> alterar(EditarContato comando) {
		Optional<Contato> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			if (comando.getTelefone() != null)
				telefoneService.alterar(comando.getTelefone());
			Contato contato = optional.get();
			contato.apply(comando);
			repo.save(contato);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

	public Optional<String> deletar(ContatoId id, UsuarioId idUsuario) {
		if (repo.findById(id).isPresent()) {
			EmergenciaId idEmergencia = executeQuery(idUsuario.toString(), sql).get(0).getId();
			Contato_Emergencia_Id idContatoEmergencia = buscaId(idEmergencia.toString(), id.toString(),
					sqlContatoEmergencia).get(0).getId();
			repoContatoEmergencia.deleteById(idContatoEmergencia);
			Optional<Contato> contato = repo.findById(id);
			if (contato.isPresent()) {
				telefoneService.deletar(contato.get().getIdTelefone());
				repo.deleteById(id);
				return Optional.of("Contato " + id + " deletado com sucesso");
			}
		}
		return Optional.empty();
	}

	private List<BuscarEmergencia> executeQuery(String id, String sql) {
		return jdbcTemplate.query(sql, new Object[] { id }, (rs, rowNum) -> {
			BuscarEmergencia emer = new BuscarEmergencia();
			String idUsuario = rs.getString("id_usuario");
			if (id.equals(idUsuario) && rs.getInt("ativo") != 0) {
				emer.setId(new EmergenciaId(rs.getString("id_emergencia")));
			}
			return emer;
		});
	}

	private List<Contato_Emergencia> buscaId(String idEmergencia, String idContato, String sql) {
		return jdbcTemplate.query(sql, new Object[] { idEmergencia, idContato }, (rs, rowNum) -> {
			Contato_Emergencia emer = new Contato_Emergencia();
			String emergencia = rs.getString("id_emergencia");
			String contato = rs.getString("id_contato");
			if (emergencia.equals(idEmergencia) && contato.equals(idContato)) {
				emer.setId(new Contato_Emergencia_Id(rs.getString("id")));
			}
			return emer;
		});
	}
}
