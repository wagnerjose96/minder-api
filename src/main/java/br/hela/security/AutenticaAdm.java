package br.hela.security;

import java.nio.file.AccessDeniedException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import br.hela.loginAdm.comandos.LogarAdm;

@Component
public class AutenticaAdm {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private String sql = "select nome_usuario from usuario_adm where nome_usuario = ?";
	
	public boolean autenticaRequisicao(String token) throws AccessDeniedException {
		String identificador = JWTUtil.getUsername(token); //pegou o username do token
		LogarAdm usuario = new LogarAdm();
		List<LogarAdm> user = jdbcTemplate.query(sql, new Object[] { identificador },
				(rs, rowNum) -> {
					String nomeUsuario = rs.getString("nome_usuario");
					if (nomeUsuario.equals(identificador) 
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

