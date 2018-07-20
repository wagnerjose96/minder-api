package br.minder.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.minder.endereco.EnderecoId;
import br.minder.endereco.EnderecoService;
import br.minder.endereco.comandos.BuscarEndereco;
import br.minder.sangue.SangueService;
import br.minder.sangue.comandos.BuscarSangue;
import br.minder.sexo.SexoService;
import br.minder.sexo.comandos.BuscarSexo;
import br.minder.telefone.TelefoneId;
import br.minder.telefone.TelefoneService;
import br.minder.telefone.comandos.BuscarTelefone;
import br.minder.usuario.comandos.BuscarUsuario;
import br.minder.usuario.comandos.CriarUsuario;
import br.minder.usuario.comandos.EditarUsuario;

@Service
@Transactional
public class UsuarioService {
	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private TelefoneService telefoneService;

	@Autowired
	private EnderecoService enderecoService;

	@Autowired
	private SangueService sangueService;

	@Autowired
	private SexoService sexoService;

	public Optional<UsuarioId> salvar(CriarUsuario comando) {
		if (comando.getEndereco() != null && comando.getTelefone() != null) {
			Optional<TelefoneId> idTelefone = telefoneService.salvar(comando.getTelefone());
			Optional<EnderecoId> idEndereco = enderecoService.salvar(comando.getEndereco());
			if (idEndereco.isPresent() && idTelefone.isPresent()) {
				Usuario novo = new Usuario(comando);
				novo.setIdEndereco(idEndereco.get());
				novo.setIdTelefone(idTelefone.get());
				repo.save(novo);
				return Optional.of(novo.getId());
			}
		}
		return Optional.empty();
	}

	public Optional<List<BuscarUsuario>> encontrar() {
		List<BuscarUsuario> resultados = new ArrayList<>();
		List<Usuario> usuarios = repo.findAll();
		if (!usuarios.isEmpty()) {
			for (Usuario usuario : usuarios) {
				if (usuario.getAtivo() == 1) {
					BuscarUsuario user = new BuscarUsuario(usuario);
					Optional<BuscarSangue> sangue = sangueService.encontrar(usuario.getIdSangue());
					Optional<BuscarEndereco> endereco = enderecoService.encontrar(usuario.getIdEndereco());
					Optional<BuscarSexo> sexo = sexoService.encontrar(usuario.getIdSexo());
					Optional<BuscarTelefone> telefone = telefoneService.encontrar(usuario.getIdTelefone());
					if (sangue.isPresent() && endereco.isPresent() && sexo.isPresent() && telefone.isPresent()) {
						user.setSexo(sexo.get());
						user.setSangue(sangue.get());
						user.setEndereco(endereco.get());
						user.setTelefone(telefone.get());
						resultados.add(user);
					}
				}
			}
			return Optional.of(resultados);
		}
		return Optional.empty();
	}

	public Optional<BuscarUsuario> encontrar(UsuarioId id) {
		List<Usuario> usuarios = repo.findAll();
		BuscarUsuario user = new BuscarUsuario();
		for (Usuario usuario : usuarios) {
			if (usuario.getId().toString().equals(id.toString()) && usuario.getAtivo() == 1) {
				user = new BuscarUsuario(usuario);
				Optional<BuscarSangue> sangue = sangueService.encontrar(usuario.getIdSangue());
				Optional<BuscarEndereco> endereco = enderecoService.encontrar(usuario.getIdEndereco());
				Optional<BuscarSexo> sexo = sexoService.encontrar(usuario.getIdSexo());
				Optional<BuscarTelefone> telefone = telefoneService.encontrar(usuario.getIdTelefone());
				if (sangue.isPresent() && endereco.isPresent() && sexo.isPresent() && telefone.isPresent()) {
					user.setSexo(sexo.get());
					user.setSangue(sangue.get());
					user.setEndereco(endereco.get());
					user.setTelefone(telefone.get());
				}
			}
		}
		return Optional.of(user);
	}

	public Optional<String> deletar(UsuarioId id) {
		Optional<Usuario> usuario = repo.findById(id);
		if (usuario.isPresent()) {
			usuario.get().setAtivo(0);
			repo.save(usuario.get());
			return Optional.of("Usuário ===> " + id + ": deletado com sucesso");
		}
		return Optional.empty();
	}

	public Optional<UsuarioId> alterar(EditarUsuario comando) {
		Optional<Usuario> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			if (comando.getEndereco() != null)
				enderecoService.alterar(comando.getEndereco());
			if (comando.getTelefone() != null)
				telefoneService.alterar(comando.getTelefone());
			Usuario user = optional.get();
			user.apply(comando);
			repo.save(user);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

}