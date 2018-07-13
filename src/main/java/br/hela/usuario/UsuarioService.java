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
import br.hela.sexo.SexoService;
import br.hela.sexo.comandos.BuscarSexo;
import br.hela.telefone.TelefoneId;
import br.hela.telefone.TelefoneService;
import br.hela.telefone.comandos.BuscarTelefone;
import br.hela.usuario.comandos.BuscarUsuario;
import br.hela.usuario.comandos.CriarUsuario;
import br.hela.usuario.comandos.EditarUsuario;

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

	public Optional<BuscarUsuario> encontrar(UsuarioId id) {
		Optional<Usuario> usuario = repo.findById(id);
		if (usuario.isPresent() && usuario.get().getAtivo() == 1) {
			BuscarUsuario resultado = new BuscarUsuario(usuario.get());
			Optional<BuscarSangue> sangue = sangueService.encontrar(usuario.get().getIdSangue());
			Optional<BuscarEndereco> endereco = enderecoService.encontrar(usuario.get().getIdEndereco());
			Optional<BuscarSexo> sexo = sexoService.encontrar(usuario.get().getIdSexo());
			Optional<BuscarTelefone> telefone = telefoneService.encontrar(usuario.get().getIdTelefone());
			if (sangue.isPresent() && endereco.isPresent() && sexo.isPresent() && telefone.isPresent()) {
				resultado.setSexo(sexo.get());
				resultado.setSangue(sangue.get());
				resultado.setEndereco(endereco.get());
				resultado.setTelefone(telefone.get());
				return Optional.of(resultado);
			}
		}
		return Optional.empty();
	}

	public Optional<List<BuscarUsuario>> encontrar() {
		List<BuscarUsuario> resultados = new ArrayList<>();
		List<Usuario> usuarios = repo.findAll();
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