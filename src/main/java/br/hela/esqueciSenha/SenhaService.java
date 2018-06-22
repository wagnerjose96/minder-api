package br.hela.esqueciSenha;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.esqueciSenha.comandos.GerarSenha;
import br.hela.usuario.Usuario;
import br.hela.usuario.UsuarioId;
import br.hela.usuario.UsuarioRepository;

@Service
@Transactional
public class SenhaService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UsuarioRepository repo;

	private String sqlSenha = "select email, id, ativo from usuario " + "where email = ?";

	private List<GerarSenha> consultarId(GerarSenha comando) {
		String email = comando.getEmail();
		return jdbcTemplate.query(sqlSenha, new Object[] { email }, (rs, rowNum) -> {
			GerarSenha usuario = new GerarSenha();
			String emailUsuario = rs.getString("email");
			if (emailUsuario.equals(email)) {
				usuario.setEmail(rs.getString("email"));
				usuario.setId(new UsuarioId(rs.getString("id")));
				usuario.setAtivo(rs.getInt("ativo"));
			}
			return usuario;
		});
	}

	public String gerarSenhaAleatoria(GerarSenha comando) {
		String[] carct = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h",
				"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C",
				"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
				"Y", "Z" };
		String senha = null;
		for (int x = 0; x < 6; x++) {
			int j = (int) (Math.random() * carct.length);
			senha += carct[j];
		}
		List<GerarSenha> usuario = consultarId(comando);
		if (usuario.isEmpty() && usuario.get(0).getAtivo() == 1) {
			Optional<Usuario> optional = repo.findById(usuario.get(0).getId());
			if (optional.isPresent()) {
				Usuario user = optional.get();
				comando.setSenha(senha);
				user.applySenha(comando);
				repo.save(user);
			}
			return senha;
		}
		return senha;
	}

}
