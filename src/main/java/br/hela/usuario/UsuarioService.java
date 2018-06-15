package br.hela.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.endereco.EnderecoId;
import br.hela.endereco.EnderecoService;
import br.hela.endereco.comandos.BuscarEndereco;
import br.hela.sangue.SangueService;
import br.hela.sangue.comandos.BuscarSangue;
import br.hela.usuario.comandos.BuscarUsuario;
import br.hela.usuario.comandos.CriarUsuario;
import br.hela.usuario.comandos.EditarUsuario;

@Service
@Transactional
public class UsuarioService {
	@Autowired
	private UsuarioRepository repo;
	@Autowired
	private EnderecoService servEndereco;
	@Autowired
	private SangueService sangueService;

	public Optional<UsuarioId> salvar(CriarUsuario comando) {
		if (comando.getEndereco() != null) {
			EnderecoId idEndereco = servEndereco.salvar(comando.getEndereco()).get();
			Usuario novo = new Usuario(comando);
			novo.setIdEndereco(idEndereco);
			repo.save(novo);
			return Optional.of(novo.getId());
		}
		return Optional.empty();
	}

	public Optional<BuscarUsuario> encontrar(UsuarioId id) {
		Usuario usuario = repo.findById(id).get();
		BuscarUsuario user = new BuscarUsuario();
		if (usuario.getAtivo() == 1) {
			user = new BuscarUsuario(usuario);
			Optional<BuscarSangue> sangue = sangueService.encontrar(usuario.getIdSangue());
			Optional<BuscarEndereco> endereco = servEndereco.encontrar(usuario.getIdEndereco());
			user.setTipoSanguineo(sangue.get());
			user.setEndereco(endereco.get());
		}
		return Optional.of(user);
	}

	public Optional<List<BuscarUsuario>> encontrar() {
		List<BuscarUsuario> resultados = new ArrayList<>();
		List<Usuario> usuarios = repo.findAll();
		for (Usuario usuario : usuarios) {
			if (usuario.getAtivo() == 1) {
				BuscarUsuario user = new BuscarUsuario(usuario);
				Optional<BuscarSangue> sangue = sangueService.encontrar(usuario.getIdSangue());
				Optional<BuscarEndereco> endereco = servEndereco.encontrar(usuario.getIdEndereco());
				user.setTipoSanguineo(sangue.get());
				user.setEndereco(endereco.get());
				resultados.add(user);
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
			if (comando.getEndereco() != null)
				servEndereco.alterar(comando.getEndereco()).get();
			Usuario user = optional.get();
			user.apply(comando);
			repo.save(user);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}
}
