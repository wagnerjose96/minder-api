package br.hela.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.usuario.comandos.CriarUsuario;
import br.hela.usuario.comandos.EditarUsuario;
import br.hela.usuario.comandos.GerarSenha;
import br.hela.usuario.comandos.LogarUsuario;

@Service
@Transactional
public class UsuarioService {
	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sqlNomeUsuario = "select nome_usuario, senha, ativo from usuario " 
	+ "where nome_usuario = ? and senha = ?";
	
	private String sqlEmail = "select email, senha, ativo from usuario " 
			+ "where email = ? and senha = ?";

	public Optional<UsuarioId> salvar(CriarUsuario comando) {
		Usuario novo = repo.save(new Usuario(comando));
		return Optional.of(novo.getId());
	}

	public Optional<Usuario> encontrar(UsuarioId id) {
		Usuario usuario = repo.findById(id).get();
		if (usuario.getAtivo() == 1) {
			return Optional.of(usuario);
		}
		return Optional.empty();
	}

	public Optional<List<Usuario>> encontrar() {
		List<Usuario> resultados = new ArrayList<>();
		List<Usuario> usuarios = repo.findAll();
		for (Usuario usuario : usuarios) {
			if (usuario.getAtivo() == 1) {
				resultados.add(usuario);
			}
		}
		return Optional.of(resultados);
	}

	public Optional<String> deletar(UsuarioId id) {
		Usuario usuario = repo.findById(id).get();
		usuario.setAtivo(0);
		repo.save(usuario);
		return Optional.of("Usuário -> " + id + ": deletado com sucesso");
	}

	public Optional<UsuarioId> alterar(EditarUsuario comando) {
		Optional<Usuario> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			Usuario user = optional.get();
			user.apply(comando);
			repo.save(user);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

	public void deletarTodos() {
		repo.deleteAll();
	}
	
	public boolean logarPorNomeDeUsuario(LogarUsuario comando) {
		List<LogarUsuario> usuario = consultarUsuario(comando);
		if (usuario.size() > 0 && usuario.get(0).getAtivo() == 1) {
			return true;
		}
		else return false;
	}
	
	public boolean logarPorEmail(LogarUsuario comando) {
		List<LogarUsuario> usuario = consultarEmail(comando);
		if (usuario.size() > 0 && usuario.get(0).getAtivo() == 1) {
			return true;
		}
		else return false;
	}

	public List<LogarUsuario> consultarUsuario(LogarUsuario comando) {
		String senha = comando.criptografa(comando.getSenha());
		String nome_usuario = comando.getNome_usuario();
		List<LogarUsuario> user = jdbcTemplate.query(sqlNomeUsuario, new Object[] { nome_usuario, senha }, (rs, rowNum) -> {
			LogarUsuario usuario = new LogarUsuario();
			String senhaUsuario = rs.getString("senha");
			String nomeUsuario = rs.getString("nome_usuario");
			if (senhaUsuario.equals(senha) && nomeUsuario.equals(nome_usuario)) {
				usuario.setNome_usuario(rs.getString("nome_usuario"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setAtivo(rs.getInt("ativo"));
			}
			return usuario;
		} );
		return user;
	}
	
	public List<LogarUsuario> consultarEmail(LogarUsuario comando) {
		String senha = comando.criptografa(comando.getSenha());
		String email = comando.getEmail();
		List<LogarUsuario> user = jdbcTemplate.query(sqlEmail, new Object[] { email, senha }, (rs, rowNum) -> {
			LogarUsuario usuario = new LogarUsuario();
			String senhaUsuario = rs.getString("senha");
			String emailUsuario = rs.getString("email");
			if (senhaUsuario.equals(senha) && emailUsuario.equals(email)) {
				usuario.setEmail(rs.getString("email"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setAtivo(rs.getInt("ativo"));
			}
			return usuario;
		} );
		return user;
	}

	public boolean gerarSenhaAleatoria(GerarSenha comando) {
		String[] carct ={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String senha="";
		for (int x=0; x<6; x++) {
			int j = (int) (Math.random()*carct.length);
			senha += carct[j];
		}
		Optional<Usuario> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			Usuario user = optional.get();
			comando.setSenha(senha);
			user.applySenha(comando);
			repo.save(user);
			return true;
		}
		return false;
	}
}