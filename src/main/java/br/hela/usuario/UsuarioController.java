package br.hela.usuario;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.hela.security.AutenticaRequisicao;
import br.hela.usuario.comandos.CriarUsuario;
import br.hela.usuario.comandos.EditarUsuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Usuário Controller")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	@Autowired
	private UsuarioService service;
	
	@Autowired
	private AutenticaRequisicao autentica;

	@ApiOperation(value = "Busque todos os usuários")
	@GetMapping
	public ResponseEntity<List<Usuario>> getUsuarios(@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<List<Usuario>> optionalUsuario = service.encontrar();
			return ResponseEntity.ok(optionalUsuario.get());
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Busque um usuário pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getUsuarioId(@PathVariable UsuarioId id, @RequestHeader String token)
			throws NullPointerException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (verificaUsuarioExistente(id)) {
				Optional<Usuario> optionalUsuario = service.encontrar(id);
				return ResponseEntity.ok(optionalUsuario.get());
			}
			throw new NullPointerException("O usuário procurado não existe no banco de dados");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Delete um usuário pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deleteUsuario(@PathVariable UsuarioId id, @RequestHeader String token)
			throws NullPointerException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (verificaUsuarioExistente(id)) {
				Optional<String> resultado = service.deletar(id);
				return ResponseEntity.ok(resultado);
			}
			throw new NullPointerException("O usuário a deletar não existe no banco de dados");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Cadastre um novo usuário")
	@PostMapping
	public ResponseEntity<String> postUsuario(@RequestBody CriarUsuario comando) throws Exception {
		Optional<UsuarioId> optionalUsuarioId = service.salvar(comando);
		if (optionalUsuarioId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalUsuarioId.get()).toUri();
			return ResponseEntity.created(location).body("Usuário cadastrado com sucesso");
		}
		throw new Exception("O usuário não foi salvo devido a um erro interno");
	}

	@ApiOperation(value = "Altere um usuário")
	@PutMapping
	public ResponseEntity<String> putUsuario(@RequestBody EditarUsuario comando, @RequestHeader String token)
			throws NullPointerException, InternalError, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaUsuarioExistente(comando.getId())) {
				throw new NullPointerException("O usuário a ser alterado não existe no banco de dados");
			}
			Optional<UsuarioId> optionalUsuarioId = service.alterar(comando);
			if (optionalUsuarioId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalUsuarioId.get()).toUri();
				return ResponseEntity.created(location).body("Usuário alterado com sucesso");
			} else {
				throw new InternalError("Erro interno durante a alteração do usuário");
			}
		}
		throw new AccessDeniedException("Acesso negado");
	}

	private boolean verificaUsuarioExistente(UsuarioId id) {
		if (!service.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}
}
