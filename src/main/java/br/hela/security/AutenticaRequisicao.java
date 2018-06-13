package br.hela.security;

import java.nio.file.AccessDeniedException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import br.hela.login.comandos.LogarUsuario;

@Component
public class AutenticaRequisicao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sql = "select nome_usuario, email, ativo from usuario where nome_usuario = ? or email = ?";

	public boolean autenticaRequisicao(String token) throws AccessDeniedException {
		String identificador = JWTUtil.getUsername(token); // pegou o username ou email do token
		LogarUsuario usuario = new LogarUsuario();
		List<LogarUsuario> user = jdbcTemplate.query(sql, new Object[] { identificador, identificador },
				(rs, rowNum) -> {
					String email = rs.getString("email");
					String nomeUsuario = rs.getString("nome_usuario");
					if (email.equals(identificador) || nomeUsuario.equals(identificador) && rs.getInt("ativo") != 0
							&& JWTUtil.tokenValido(token)) {
						usuario.setIdentificador(rs.getString("nome_usuario"));
					}
					return usuario;
				});
		if (user.size() == 1)
			return true;
		return false;
	}

}
