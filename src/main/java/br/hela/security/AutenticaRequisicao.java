package br.hela.security;

import java.nio.file.AccessDeniedException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import br.hela.login.comandos.IdentificarUsuario;
import br.hela.login.comandos.LogarUsuario;
import br.hela.usuario.UsuarioId;

@Component
public class AutenticaRequisicao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private String sql = "select nome_usuario, email, ativo from usuario where nome_usuario = ? or email = ?";
	private String idUser = "select id, nome_usuario, email from usuario where nome_usuario = ? or email = ?";

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

	public UsuarioId idUser(String token) {
		String identificador = JWTUtil.getUsername(token);
		IdentificarUsuario usuario = new IdentificarUsuario();
		List<IdentificarUsuario> user = jdbcTemplate.query(idUser, new Object[] { identificador, identificador },
				(rs, rowNum) -> {
					String email = rs.getString("email");
					String nomeUsuario = rs.getString("nome_usuario");
					if (email.equals(identificador) || nomeUsuario.equals(identificador)) {
						usuario.setId(new UsuarioId(rs.getString("id")));
					}
					return usuario;
				});
		if (user.size() == 1)
			return user.get(0).getId();
		return null;
	}

}