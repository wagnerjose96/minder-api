package br.hela.usuario;

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
		return repo.findById(id);
	}

	public Optional<List<Usuario>> encontrar() {
		return Optional.of(repo.findAll());
	}

	public Optional<String> deletar(UsuarioId id) {
		repo.deleteById(id);
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

}
