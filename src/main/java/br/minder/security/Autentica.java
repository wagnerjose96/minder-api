package br.minder.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import br.minder.login.comandos.IdentificarUsuario;
import br.minder.login.comandos.LogarAdm;
import br.minder.login.comandos.LogarUsuario;
import br.minder.usuario.UsuarioId;
import lombok.Getter;

@Component
@Getter
public class Autentica {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String selectUser = "select nome_usuario, email, ativo from usuario where nome_usuario = ? or email = ?";
	private String idUser = "select id, nome_usuario, email from usuario where nome_usuario = ? or email = ?";
	private String selectAdm = "select nome_usuario from usuario_adm where nome_usuario = ?";

	private static final String COLUNAUSERNAME = "nome_usuario";
	private static final String COLUNAATIVO = "ativo";
	private static final String COLUNAEMAIL = "email";

	public boolean autenticaRequisicao(String token) {
		String identificador = JWTUtil.getUsername(token);
		LogarUsuario usuario = new LogarUsuario();
		List<LogarUsuario> user = jdbcTemplate.query(selectUser, new Object[] { identificador, identificador },
				(rs, rowNum) -> {
					String email = rs.getString(COLUNAEMAIL);
					String nomeUsuario = rs.getString(COLUNAUSERNAME);
					if (email.equals(identificador) || nomeUsuario.equals(identificador) && rs.getInt(COLUNAATIVO) != 0
							&& JWTUtil.tokenValido(token)) {
						usuario.setIdentificador(rs.getString(COLUNAUSERNAME));
					}
					return usuario;
				});
		return user.size() == 1;
	}

	public UsuarioId idUser(String token) {
		String identificador = JWTUtil.getUsername(token);
		IdentificarUsuario usuario = new IdentificarUsuario();
		List<IdentificarUsuario> user = jdbcTemplate.query(idUser, new Object[] { identificador, identificador },
				(rs, rowNum) -> {
					String email = rs.getString(COLUNAEMAIL);
					String nomeUsuario = rs.getString(COLUNAUSERNAME);
					if (email.equals(identificador) || nomeUsuario.equals(identificador)) {
						usuario.setId(new UsuarioId(rs.getString("id")));
					}
					return usuario;
				});
		if (user.size() == 1)
			return user.get(0).getId();
		return null;
	}
	
	public boolean autenticaRequisicaoAdm(String token) {
		String identificador = JWTUtil.getUsername(token);
		LogarAdm usuario = new LogarAdm();
		List<LogarAdm> user = jdbcTemplate.query(selectAdm, new Object[] { identificador }, (rs, rowNum) -> {
			String nomeUsuario = rs.getString(COLUNAUSERNAME);
			if (nomeUsuario.equals(identificador) && JWTUtil.tokenValido(token)) {
				usuario.setIdentificador(rs.getString(COLUNAUSERNAME));
			}
			return usuario;
		});
		return user.size() == 1;
	}

}