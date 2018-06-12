package br.hela.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.usuario.comandos.CriarUsuario;
import br.hela.usuario.comandos.EditarUsuario;

@Service
@Transactional
public class UsuarioService {
	@Autowired
	private UsuarioRepository repo;

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
}