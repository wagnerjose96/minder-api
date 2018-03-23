package br.hela.usuario;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.hela.usuario.comandos.CriarUsuario;
import javassist.tools.web.BadHttpRequest;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	@Autowired
	private UsuarioService service;

	@GetMapping
	public ResponseEntity<List<Usuario>> getUsuarios() 
			throws TimeoutException, NullPointerException, BadHttpRequest {

		verificaListaUsuario();
		verificaTempoResposta();
		Optional<List<Usuario>> optionalUsuario = service.encontrar();
		if (optionalUsuario.isPresent()) {
			return ResponseEntity.ok(optionalUsuario.get());
		}
		throw new BadHttpRequest();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getUsuarioId(@PathVariable UsuarioId id)
			throws TimeoutException, NullPointerException, BadHttpRequest {

		verificaUsuarioExitente(id);
		verificaTempoResposta();
		Optional<Usuario> optionalUsuario = service.encontrar(id);
		if (optionalUsuario.isPresent()) {
			return ResponseEntity.ok(optionalUsuario.get());
		}
		throw new BadHttpRequest();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deleteUsuario(@PathVariable UsuarioId id)
			throws TimeoutException, NullPointerException, BadHttpRequest {

		verificaUsuarioExitente(id);
		verificaTempoResposta();
		Optional<String> resultado = service.deletar(id);
		if (!resultado.isPresent()) {
			return ResponseEntity.ok(resultado);
		}
		throw new BadHttpRequest();
	}

	@PostMapping
	public ResponseEntity<UsuarioId> postUsuario(@RequestBody CriarUsuario comando)
			throws TimeoutException, NullPointerException, BadHttpRequest {

		verificaTempoResposta();
		Optional<UsuarioId> optionalUsuarioId = service.salvar(comando);
		verificaUsuarioExitente(optionalUsuarioId.get());
		if (optionalUsuarioId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalUsuarioId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		throw new BadHttpRequest();
	}

	@PutMapping
	public ResponseEntity<UsuarioId> putUsuario(@RequestBody Usuario comando)
			throws TimeoutException, NullPointerException, BadHttpRequest {

		verificaUsuarioExitente(comando.getId());
		verificaTempoResposta();
		Optional<UsuarioId> optionalUsuarioId = service.alterar(comando);
		if (optionalUsuarioId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalUsuarioId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		throw new BadHttpRequest();
	}

	private void verificaTempoResposta() throws TimeoutException {
		if (System.currentTimeMillis() == 10) {
			throw new TimeoutException("Servidor sem resposta");
		}
	}

	private void verificaUsuarioExitente(UsuarioId id) throws NullPointerException {
		if (!service.encontrar(id).isPresent()) {
			throw new NullPointerException("Usuário não encontrado");
		}
	}

	private void verificaListaUsuario() throws NullPointerException {
		if (!service.encontrar().isPresent()) {
			throw new NullPointerException("Nenhum usuário cadastrado");
		}
	}

}
