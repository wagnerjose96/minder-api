package br.hela.usuario;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import br.hela.usuario.comandos.BuscarUsuario;
import br.hela.usuario.comandos.EditarUsuario;
import br.hela.usuario.comandos.CriarUsuario;
import br.hela.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Usuário Controller")
@RestController
@RequestMapping("/usuarios")
@CrossOrigin
public class UsuarioController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private UsuarioService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque todos os usuários")
	@GetMapping
	public ResponseEntity<List<BuscarUsuario>> getUsuarios(@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			Optional<List<BuscarUsuario>> optionalUsuarios = service.encontrar();
			if (optionalUsuarios.isPresent()) {
				return ResponseEntity.ok(optionalUsuarios.get());
			}
			return ResponseEntity.notFound().build();
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Busque um usuário pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarUsuario> getUsuarioPorId(@PathVariable UsuarioId id) {
		Optional<BuscarUsuario> optionalUsuario = service.encontrar(id);
		if (optionalUsuario.isPresent()) {
			return ResponseEntity.ok(optionalUsuario.get());
		}
		throw new NullPointerException("O usuário procurado não existe no banco de dados");
	}

	@ApiOperation("Delete um usuário pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarUsuario(@PathVariable UsuarioId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (service.encontrar(id).isPresent()) {
				Optional<String> optionalUsuario = service.deletar(id);
				return ResponseEntity.ok(optionalUsuario);
			}
			throw new NullPointerException("O usuário a deletar não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Cadastre um novo usuário")
	@PostMapping
	public ResponseEntity<String> postMedicamento(@RequestBody CriarUsuario comando) throws SQLException {
		Optional<UsuarioId> optionalUsuarioId = service.salvar(comando);
		if (optionalUsuarioId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalUsuarioId.get()).toUri();
			return ResponseEntity.created(location).body("O usuário foi cadastrado com sucesso");
		}
		throw new SQLException("O usuário não foi salvo devido a um erro interno");
	}

	@ApiOperation("Altere um usuário")
	@PutMapping
	public ResponseEntity<String> putMedicamento(@RequestBody EditarUsuario comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicao(token)) {
			if (!service.encontrar(comando.getId()).isPresent()) {
				throw new NullPointerException("O usuário a ser alterado não existe no banco de dados");
			}
			Optional<UsuarioId> optionalUsuarioId = service.alterar(comando);
			if (optionalUsuarioId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalUsuarioId.get()).toUri();
				return ResponseEntity.created(location).body("O usuário foi alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do usuário");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}
}