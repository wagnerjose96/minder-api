package br.hela.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.usuario.comandos.CriarUsuario;

@Service
@Transactional
public class UsuarioService {
	@Autowired
	private UsuarioRepository repo;

	public Optional<UsuarioId> salvar(CriarUsuario comando) {
		UsuarioId resultado = null;
		if (comando != null) {
			Usuario novo = repo.save(new Usuario(comando));
			resultado = novo.getId();
		}
		return Optional.of(resultado);
	}

	public Optional<Usuario> encontrar(UsuarioId id) {
		Optional<Usuario> resultado = null;
		if (repo.existsById(id)) {
			resultado = repo.findById(id);
		}
		return resultado;
	}

	public Optional<List<Usuario>> encontrar() {
		List<Usuario> resultado = null;
		if (repo.count() != 0) {
			resultado = repo.findAll();
		}
		return Optional.of(resultado);
	}

	public Optional<String> deletar(UsuarioId id) {
		String resultado = null;
		if (repo.existsById(id)) {
			repo.deleteById(id);
			resultado = "Usuário -> " + id + ": deletado com sucesso";
		}
		return Optional.of(resultado);
	}

	public Optional<UsuarioId> alterar(Usuario comando) {
		UsuarioId resultado = null;
		if (repo.existsById(comando.getId())) {
			repo.save(comando);
			resultado = comando.getId();
		}
		return Optional.of(resultado);
	}

}
