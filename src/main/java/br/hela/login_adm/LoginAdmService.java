package br.hela.login_adm;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.login_adm.comandos.LogarAdm;
import br.hela.security.Criptografia;

@Service
@Transactional
public class LoginAdmService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sql = "select nome_usuario, senha from usuario_adm " + "where nome_usuario = ? and senha = ?";

	public boolean consultarUsuario(LogarAdm comando) {
		String senha = Criptografia.criptografa(comando.getSenha());
		String username = comando.getUsername();
		LogarAdm usuario = new LogarAdm();
		List<LogarAdm> user = jdbcTemplate.query(sql, new Object[] { username, senha }, (rs, rowNum) -> {
			String senhaUsuario = rs.getString("senha");
			String nomeUsuario = rs.getString("nome_usuario");
			if (senhaUsuario.equals(senha) && nomeUsuario.equals(username)) {
				usuario.setUsername(rs.getString("nome_usuario"));
				usuario.setSenha(rs.getString("senha"));
			}
			return usuario;
		});
		return user.size() == 1;
	}

}
