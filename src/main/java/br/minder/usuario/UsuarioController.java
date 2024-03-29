package br.minder.usuario;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
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
import br.minder.security.Autentica;
import br.minder.usuario.comandos.BuscarUsuario;
import br.minder.usuario.comandos.CriarUsuario;
import br.minder.usuario.comandos.EditarUsuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Usuário Controller")
@RestController
@RequestMapping("/api/usuario")
@CrossOrigin
public class UsuarioController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private UsuarioService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque um usuário")
	@GetMapping
	public ResponseEntity<BuscarUsuario> getUsuario(@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarUsuario> optionalUsuarios = service.encontrar(autentica.idUser(token));
			if (optionalUsuarios.isPresent()) {
				return ResponseEntity.ok(optionalUsuarios.get());
			}
			throw new NullPointerException("O usuário procurado não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Verifica email existente")
	@GetMapping("/email/{email}")
	public boolean getEmailUsuario(@PathVariable String email) {
		return service.validarEmail(email);
	}

	@ApiOperation("Verifica nome de usuário existente")
	@GetMapping("/username/{username}")
	public boolean getNomeUsuario(@PathVariable String username) {
		return service.validarUsername(username);
	}

	@ApiOperation("Delete um usuário pelo ID")
	@DeleteMapping
	public ResponseEntity<String> deletarUsuario(@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<String> optionalUsuario = service.deletar(autentica.idUser(token));
			if (optionalUsuario.isPresent())
				return ResponseEntity.ok(optionalUsuario.get());
			throw new NullPointerException("O usuário a deletar não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Cadastre um novo usuário")
	@PostMapping
	public ResponseEntity<String> postUsuario(@RequestBody CriarUsuario comando)
			throws SQLException, NoSuchAlgorithmException {
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
	public ResponseEntity<String> putUsuario(@RequestBody EditarUsuario comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicao(token)) {
			if (service.encontrar(comando.getId()).isPresent()
					&& comando.getId().toString().equals(autentica.idUser(token).toString())) {
				Optional<UsuarioId> optionalUsuarioId = service.alterar(comando);
				if (optionalUsuarioId.isPresent()) {
					return ResponseEntity.ok().body("O usuário foi alterado com sucesso");
				} else {
					throw new SQLException("Ocorreu um erro interno durante a alteração do usuário");
				}
			}
			throw new NullPointerException("O usuário a ser alterado não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}
}