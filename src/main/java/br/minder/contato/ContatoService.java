package br.minder.contato;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import br.minder.contato.Contato;
import br.minder.contato.ContatoId;
import br.minder.contato.comandos.BuscarContato;
import br.minder.contato.comandos.CriarContato;
import br.minder.contato.comandos.EditarContato;
import br.minder.contato.contato_emergencia.ContatoEmergencia;
import br.minder.contato.contato_emergencia.ContatoEmergenciaId;
import br.minder.contato.contato_emergencia.ContatoEmergenciaRepository;
import br.minder.conversor.TermoDeBusca;
import br.minder.emergencia.EmergenciaId;
import br.minder.emergencia.comandos.BuscarEmergencia;
import br.minder.telefone.TelefoneId;
import br.minder.telefone.TelefoneRepository;
import br.minder.telefone.TelefoneService;
import br.minder.telefone.comandos.BuscarTelefone;
import br.minder.usuario.UsuarioId;

@Service
@Transactional
public class ContatoService {
	@Autowired
	private ContatoRepository repo;

	@Autowired
	private TelefoneService telefoneService;

	@Autowired
	private TelefoneRepository telefoneRepo;

	@Autowired
	private ContatoEmergenciaRepository repoContatoEmergencia;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sql = "select e.id_usuario, e.id_emergencia, u.ativo, u.id "
			+ "from usuario u inner join emergencia e " + "on u.id = e.id_usuario "
			+ "group by u.id, e.id_usuario, e.id_emergencia " + "having u.id = ?";

	private String sqlContatoEmergencia = "select e.id_contato, e.id_emergencia, e.id " + "from contato_emergencia e "
			+ "where e.id_emergencia = ? and e.id_contato = ?";

	public Optional<ContatoId> salvar(CriarContato comando, UsuarioId id) {
		if (comando.getTelefone() != null && comando.getNome() != null) {
			Optional<TelefoneId> idTelefone = telefoneService.salvar(comando.getTelefone());
			if (idTelefone.isPresent()) {
				Contato novo = new Contato(comando);
				novo.setIdTelefone(idTelefone.get());
				repo.save(novo);
				ContatoEmergencia contatoEmergencia = new ContatoEmergencia();
				List<BuscarEmergencia> emergencia = executeQuery(id.toString(), sql);
				contatoEmergencia.setIdEmergencia(emergencia.get(0).getId());
				contatoEmergencia.setIdContato(novo.getId());
				repoContatoEmergencia.save(contatoEmergencia);
				return Optional.of(novo.getId());
			}
		}
		return Optional.empty();
	}

	public Optional<BuscarContato> encontrar(ContatoId contatoId, String usuarioId) {
		Contato contato = repo.findById(contatoId.toString(), usuarioId);
		if (contato != null) {
			BuscarContato resultado = new BuscarContato(contato);
			Optional<BuscarTelefone> telefone = telefoneService.encontrar(contato.getIdTelefone());
			if (telefone.isPresent()) {
				resultado.setTelefone(telefone.get());
				return Optional.of(resultado);
			}
		}
		return Optional.empty();
	}

	public Optional<Page<BuscarContato>> encontrar(Pageable pageable, String usuarioId, String searchTerm) {
		Page<Contato> contatos = repo.findAll(usuarioId, pageable);
		if (!contatos.hasContent()) {
			return Optional.empty();
		}
		List<BuscarContato> resultados = new ArrayList<>();
		for (Contato contato : contatos) {
			if (TermoDeBusca.searchTerm(contato.getNome(), searchTerm)) {
				BuscarContato nova = new BuscarContato(contato);
				Optional<BuscarTelefone> telefone = telefoneService.encontrar(contato.getIdTelefone());
				if (telefone.isPresent()) {
					nova.setTelefone(telefone.get());
					resultados.add(nova);
				}
			}
		}
		Page<BuscarContato> page = new PageImpl<>(resultados,
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
				resultados.size());
		return Optional.of(page);
	}

	public Optional<ContatoId> alterar(EditarContato comando, UsuarioId usuarioId) {
		Contato optional = repo.findById(comando.getId().toString(), usuarioId.toString());
		if (optional != null && comando.getTelefone() != null && comando.getNome() != null) {
			if (comando.getTelefone() != null)
				telefoneService.alterar(comando.getTelefone());
			Contato contato = optional;
			contato.apply(comando);
			repo.save(contato);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

	public Optional<String> deletar(ContatoId id, UsuarioId idUsuario) {
		Contato contato = repo.findById(id.toString(), idUsuario.toString());
		if (contato != null) {
			EmergenciaId idEmergencia = executeQuery(idUsuario.toString(), sql).get(0).getId();
			ContatoEmergenciaId idContatoEmergencia = buscaId(idEmergencia.toString(), id.toString(),
					sqlContatoEmergencia).get(0).getId();
			repoContatoEmergencia.deleteById(idContatoEmergencia);
			repo.deleteById(id);
			telefoneRepo.deleteById(contato.getIdTelefone());
			return Optional.of("Contato ===> " + id + ": deletado com sucesso");
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

	private List<ContatoEmergencia> buscaId(String idEmergencia, String idContato, String sql) {
		return jdbcTemplate.query(sql, new Object[] { idEmergencia, idContato }, (rs, rowNum) -> {
			ContatoEmergencia emer = new ContatoEmergencia();
			String emergencia = rs.getString("id_emergencia");
			String contato = rs.getString("id_contato");
			if (emergencia.equals(idEmergencia) && contato.equals(idContato)) {
				emer.setId(new ContatoEmergenciaId(rs.getString("id")));
			}
			return emer;
		});
	}
}
