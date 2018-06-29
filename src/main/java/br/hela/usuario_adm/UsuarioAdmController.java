package br.hela.usuario_adm;

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

import br.hela.security.Autentica;
import br.hela.usuario_adm.UsuarioAdmId;
import br.hela.usuario_adm.UsuarioAdmService;
import br.hela.usuario_adm.comandos.BuscarUsuarioAdm;
import br.hela.usuario_adm.comandos.CriarUsuarioAdm;
import br.hela.usuario_adm.comandos.EditarUsuarioAdm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Usuário Admin Controller")
@RestController
@RequestMapping("/adm")
@CrossOrigin
public class UsuarioAdmController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private UsuarioAdmService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque todos os usuários admin")
	@GetMapping
	public ResponseEntity<List<BuscarUsuarioAdm>> getUsuarioAdmins(@RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			Optional<List<BuscarUsuarioAdm>> optionalUsuarioAdmin = service.encontrar();
			if (optionalUsuarioAdmin.isPresent()) {
				return ResponseEntity.ok(optionalUsuarioAdmin.get());
			}
			return ResponseEntity.notFound().build();
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Busque um usuário admin pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarUsuarioAdm> getUsuarioAdminId(@PathVariable UsuarioAdmId id,
			@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			Optional<BuscarUsuarioAdm> optionalUsuarioAdmin = service.encontrar(id);
			if (optionalUsuarioAdmin.isPresent()) {
				return ResponseEntity.ok(optionalUsuarioAdmin.get());
			}
			throw new NullPointerException("O administrador procurado não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Delete um usuário admin pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deleteUsuarioAdmin(@PathVariable UsuarioAdmId id,
			@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			if (service.encontrar(id).isPresent()) {
				Optional<String> resultado = service.deletar(id);
				return ResponseEntity.ok(resultado);
			}
			throw new NullPointerException("O administrador a deletar não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Cadastre um novo usuário admin")
	@PostMapping
	public ResponseEntity<String> postUsuarioAdmin(@RequestBody CriarUsuarioAdm comando) throws SQLException {
		Optional<UsuarioAdmId> optionalUsuarioAdminId = service.salvar(comando);
		if (optionalUsuarioAdminId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalUsuarioAdminId.get()).toUri();
			return ResponseEntity.created(location).body("O administrador foi cadastrado com sucesso");
		}
		throw new SQLException("O administrador não foi salvo devido a um erro interno");
	}

	@ApiOperation("Altere um usuário admin")
	@PutMapping
	public ResponseEntity<String> putUsuarioAdmin(@RequestHeader String token, @RequestBody EditarUsuarioAdm comando)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			if (!service.encontrar(comando.getId()).isPresent()) {
				throw new NullPointerException("O administrador a ser alterado não existe no banco de dados");
			}
			Optional<UsuarioAdmId> optionalUsuarioAdminId = service.alterar(comando);
			if (optionalUsuarioAdminId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalUsuarioAdminId.get()).toUri();
				return ResponseEntity.created(location).body("O administrador foi alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do administrador");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}
}
