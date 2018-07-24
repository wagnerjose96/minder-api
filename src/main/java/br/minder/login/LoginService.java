package br.minder.login;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.minder.login.comandos.LogarAdm;
import br.minder.login.comandos.LogarUsuario;
import br.minder.security.Criptografia;

@Service
@Transactional
public class LoginService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private String sqlAdm = "select nome_usuario, senha from usuario_adm " + "where nome_usuario = ? and senha = ?";

	private String sqlNomeUsuario = "select nome_usuario, senha, ativo from usuario "
			+ "where nome_usuario = ? and senha = ?";

	private String sqlEmail = "select email, senha, ativo from usuario " + "where email = ? and senha = ?";
	
	private static final String COLUNASENHA = "senha";
	private static final String COLUNANOMEUSUARIO = "nome_usuario";
	private static final String COLUNAATIVO = "ativo";
	private static final String COLUNAEMAIL = "email";

	public boolean consultarUsuario(LogarUsuario comando) {
		String senha = Criptografia.criptografa(comando.getSenha());
		String username = comando.getIdentificador();
		LogarUsuario usuario = new LogarUsuario();
		List<LogarUsuario> user = jdbcTemplate.query(sqlNomeUsuario, new Object[] { username, senha },
				(rs, rowNum) -> {
					String senhaUsuario = rs.getString(COLUNASENHA);
					String nomeUsuario = rs.getString(COLUNANOMEUSUARIO);
					if (senhaUsuario.equals(senha) && nomeUsuario.equals(username) && rs.getInt(COLUNAATIVO) != 0) {
						usuario.setIdentificador(rs.getString(COLUNANOMEUSUARIO));
						usuario.setSenha(rs.getString(COLUNASENHA));
					}
					return usuario;
				});
		return user.size() == 1;
	}

	public boolean consultarEmail(LogarUsuario comando) {
		String senha = Criptografia.criptografa(comando.getSenha());
		String email = comando.getIdentificador();
		LogarUsuario usuario = new LogarUsuario();
		List<LogarUsuario> user = jdbcTemplate.query(sqlEmail, new Object[] { email, senha }, (rs, rowNum) -> {
			String senhaUsuario = rs.getString(COLUNASENHA);
			String emailUsuario = rs.getString(COLUNAEMAIL);
			if (senhaUsuario.equals(senha) && emailUsuario.equals(email) && rs.getInt(COLUNAATIVO) != 0) {
				usuario.setIdentificador(rs.getString(COLUNAEMAIL));
				usuario.setSenha(rs.getString(COLUNASENHA));
			}
			return usuario;
		});
		return user.size() == 1;
	}
	
	public boolean consultarUsuario(LogarAdm comando) {
		String senha = Criptografia.criptografa(comando.getSenha());
		String username = comando.getIdentificador();
		LogarAdm usuario = new LogarAdm();
		List<LogarAdm> user = jdbcTemplate.query(sqlAdm, new Object[] { username, senha }, (rs, rowNum) -> {
			String senhaUsuario = rs.getString(COLUNASENHA);
			String nomeUsuario = rs.getString(COLUNANOMEUSUARIO);
			if (senhaUsuario.equals(senha) && nomeUsuario.equals(username)) {
				usuario.setIdentificador(rs.getString(COLUNANOMEUSUARIO));
				usuario.setSenha(rs.getString(COLUNASENHA));
			}
			return usuario;
		});
		return user.size() == 1;
	}

}
