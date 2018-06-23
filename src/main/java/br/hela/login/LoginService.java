package br.hela.login;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.login.comandos.LogarUsuario;
import br.hela.security.Criptografia;

@Service
@Transactional
public class LoginService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sqlNomeUsuario = "select nome_usuario, senha, ativo from usuario "
			+ "where nome_usuario = ? and senha = ?";

	private String sqlEmail = "select email, senha, ativo from usuario " + "where email = ? and senha = ?";
	
	private static final String COLUNASENHA = "senha";

	public boolean consultarUsuario(LogarUsuario comando) {
		String senha = Criptografia.criptografa(comando.getSenha());
		String username = comando.getIdentificador();
		LogarUsuario usuario = new LogarUsuario();
		List<LogarUsuario> user = jdbcTemplate.query(sqlNomeUsuario, new Object[] { username, senha },
				(rs, rowNum) -> {
					String senhaUsuario = rs.getString(COLUNASENHA);
					String nomeUsuario = rs.getString("nome_usuario");
					if (senhaUsuario.equals(senha) && nomeUsuario.equals(username) && rs.getInt("ativo") != 0) {
						usuario.setIdentificador(rs.getString("nome_usuario"));
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
			String emailUsuario = rs.getString("email");
			if (senhaUsuario.equals(senha) && emailUsuario.equals(email) && rs.getInt("ativo") != 0) {
				usuario.setIdentificador(rs.getString("email"));
				usuario.setSenha(rs.getString(COLUNASENHA));
			}
			return usuario;
		});
		return user.size() == 1;
	}

}
