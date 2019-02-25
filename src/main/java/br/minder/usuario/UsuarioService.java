package br.minder.usuario;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.minder.endereco.EnderecoId;
import br.minder.endereco.EnderecoService;
import br.minder.endereco.comandos.BuscarEndereco;
import br.minder.genero.GeneroService;
import br.minder.genero.comandos.BuscarGenero;
import br.minder.sangue.SangueService;
import br.minder.sangue.comandos.BuscarSangue;
import br.minder.telefone.TelefoneId;
import br.minder.telefone.TelefoneService;
import br.minder.telefone.comandos.BuscarTelefone;
import br.minder.usuario.comandos.BuscarUsuario;
import br.minder.usuario.comandos.CriarUsuario;
import br.minder.usuario.comandos.EditarUsuario;

@Service
@Transactional
public class UsuarioService {
	private static final String COLUNANOMEUSUARIO = "nome_usuario";
	private static final String COLUNAEMAIL = "email";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sqlUsername = "select nome_usuario from usuario where nome_usuario = ?";

	private String sqlEmail = "select email from usuario  where email = ?";
	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private TelefoneService telefoneService;

	@Autowired
	private EnderecoService enderecoService;

	@Autowired
	private SangueService sangueService;

	@Autowired
	private GeneroService generoService;

	private static String formataEmail(String email) {
		email = email.replaceAll("%40", "@");
		email = email.replaceAll("=", "");
		email = email.replaceAll(COLUNAEMAIL, "");
		return email;
	}

	public Optional<UsuarioId> salvar(CriarUsuario comando) throws NoSuchAlgorithmException {
		if (comando.getNome() != null) {
			Usuario novo = new Usuario(comando);
			if (comando.getEndereco() != null) {
				Optional<EnderecoId> idEndereco = enderecoService.salvar(comando.getEndereco());
				if (idEndereco.isPresent())
					novo.setIdEndereco(idEndereco.get());
			}
			if (comando.getTelefone() != null) {
				Optional<TelefoneId> idTelefone = telefoneService.salvar(comando.getTelefone());
				if (idTelefone.isPresent())
					novo.setIdTelefone(idTelefone.get());
			}
			repo.save(novo);
			return Optional.of(novo.getId());
		}
		return Optional.empty();
	}

	public Optional<List<BuscarUsuario>> encontrar() {
		List<BuscarUsuario> resultados = new ArrayList<>();
		List<Usuario> usuarios = repo.findAll();
		if (!usuarios.isEmpty()) {
			for (Usuario usuario : usuarios) {
				if (usuario.getAtivo() == 1) {
					BuscarUsuario user = new BuscarUsuario(usuario);
					Optional<BuscarSangue> sangue = sangueService.encontrar(usuario.getIdSangue());
					Optional<BuscarEndereco> endereco = enderecoService.encontrar(usuario.getIdEndereco());
					Optional<BuscarGenero> genero = generoService.encontrar(usuario.getIdGenero());
					Optional<BuscarTelefone> telefone = telefoneService.encontrar(usuario.getIdTelefone());
					if (sangue.isPresent() && endereco.isPresent() && genero.isPresent() && telefone.isPresent()) {
						user.setGenero(genero.get());
						user.setSangue(sangue.get());
						user.setEndereco(endereco.get());
						user.setTelefone(telefone.get());
						resultados.add(user);
					}
				}
			}
			return Optional.of(resultados);
		}
		return Optional.empty();
	}

	public Optional<BuscarUsuario> encontrar(UsuarioId id) {
		Optional<Usuario> usuario = repo.findById(id);
		if (usuario.isPresent() && usuario.get().getAtivo() == 1) {
			BuscarUsuario user = new BuscarUsuario(usuario.get());
			Optional<BuscarSangue> sangue = sangueService.encontrar(usuario.get().getIdSangue());
			Optional<BuscarEndereco> endereco = enderecoService.encontrar(usuario.get().getIdEndereco());
			Optional<BuscarGenero> genero = generoService.encontrar(usuario.get().getIdGenero());
			Optional<BuscarTelefone> telefone = telefoneService.encontrar(usuario.get().getIdTelefone());
			if (sangue.isPresent() && endereco.isPresent() && genero.isPresent() && telefone.isPresent()) {
				user.setGenero(genero.get());
				user.setSangue(sangue.get());
				user.setEndereco(endereco.get());
				user.setTelefone(telefone.get());
			}
			return Optional.of(user);
		}
		return Optional.empty();
	}

	public Optional<String> deletar(UsuarioId id) {
		Optional<Usuario> usuario = repo.findById(id);
		if (usuario.isPresent() && usuario.get().getAtivo() == 1) {
			usuario.get().setAtivo(0);
			repo.save(usuario.get());
			return Optional.of("Usuário ===> " + id + ": deletado com sucesso");
		}
		return Optional.empty();
	}

	public Optional<UsuarioId> alterar(EditarUsuario comando) {
		Optional<Usuario> optional = repo.findById(comando.getId());
		if (comando.getNome() != null && optional.isPresent()) {
			if (comando.getEndereco() != null)
				enderecoService.alterar(comando.getEndereco());
			if (comando.getTelefone() != null)
				telefoneService.alterar(comando.getTelefone());
			Usuario user = optional.get();
			user.apply(comando);
			repo.save(user);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

	public boolean validarEmail(String email) {
		List<String> user = jdbcTemplate.query(sqlEmail, new Object[] { formataEmail(email) }, (rs, rowNum) -> {
			String usuario = null;
			String emailUsuario = rs.getString(COLUNAEMAIL);
			if (emailUsuario.equals(email)) {
				usuario = rs.getString(COLUNAEMAIL);
			}
			return usuario;
		});
		return user.size() == 1;
	}

	public boolean validarUsername(String username) {
		List<String> user = jdbcTemplate.query(sqlUsername, new Object[] { username }, (rs, rowNum) -> {
			String usuario = null;
			String nomeUsuario = rs.getString(COLUNANOMEUSUARIO);
			if (nomeUsuario.equals(username)) {
				usuario = rs.getString(COLUNANOMEUSUARIO);
			}
			return usuario;
		});
		return user.size() == 1;
	}
}