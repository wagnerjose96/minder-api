package br.hela.usuarioAdm;

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
import br.hela.security.AutenticaAdm;
import br.hela.usuarioAdm.UsuarioAdm;
import br.hela.usuarioAdm.UsuarioAdmId;
import br.hela.usuarioAdm.UsuarioAdmService;
import br.hela.usuarioAdm.comandos.CriarUsuarioAdm;
import br.hela.usuarioAdm.comandos.EditarUsuarioAdm;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/adm")
public class UsuarioAdmController {
	@Autowired
	private UsuarioAdmService service;

	@Autowired
	private AutenticaAdm autentica;

	@ApiOperation(value = "Busque todos os administradores")
	@GetMapping
	public ResponseEntity<List<UsuarioAdm>> getUsuarioAdmins(@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<List<UsuarioAdm>> optionalUsuarioAdmin = service.encontrar();
			return ResponseEntity.ok(optionalUsuarioAdmin.get());
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Busque um administrador pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioAdm> getUsuarioAdminId(@PathVariable UsuarioAdmId id, @RequestHeader String token)
			throws NullPointerException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (verificaUsuarioAdminExistente(id)) {
				Optional<UsuarioAdm> optionalUsuarioAdmin = service.encontrar(id);
				return ResponseEntity.ok(optionalUsuarioAdmin.get());
			}
			throw new NullPointerException("O administrador procurado não existe no banco de dados");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Delete um administrador pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deleteUsuarioAdmin(@PathVariable UsuarioAdmId id,
			@RequestHeader String token) throws NullPointerException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (verificaUsuarioAdminExistente(id)) {
				Optional<String> resultado = service.deletar(id);
				return ResponseEntity.ok(resultado);
			}
			throw new NullPointerException("O administrador a deletar não existe no banco de dados");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Cadastre um novo administrador")
	@PostMapping
	public ResponseEntity<String> postUsuarioAdmin(@RequestBody CriarUsuarioAdm comando, @RequestHeader String token) throws Exception {
		if (service.encontrar().get().size() > 0) {
			if (autentica.autenticaRequisicao(token)) {
				Optional<UsuarioAdmId> optionalUsuarioAdminId = service.salvar(comando);
				if (optionalUsuarioAdminId.isPresent()) {
					URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
							.buildAndExpand(optionalUsuarioAdminId.get()).toUri();
					return ResponseEntity.created(location).body("Administrador cadastrado com sucesso");
				}
				throw new Exception("O administrador não foi salvo devido a um erro interno");
			}
			throw new AccessDeniedException("Acesso negado");
		}
		Optional<UsuarioAdmId> optionalUsuarioAdminId = service.salvar(comando);
		if (optionalUsuarioAdminId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalUsuarioAdminId.get()).toUri();
			return ResponseEntity.created(location).body("Administrador cadastrado com sucesso");
		}
		throw new Exception("O administrador não foi salvo devido a um erro interno");
	}

	@ApiOperation(value = "Altere um administrador")
	@PutMapping
	public ResponseEntity<String> putUsuarioAdmin(@RequestBody EditarUsuarioAdm comando, @RequestHeader String token)
			throws NullPointerException, InternalError, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaUsuarioAdminExistente(comando.getId())) {
				throw new NullPointerException("O administrador a ser alterado não existe no banco de dados");
			}
			Optional<UsuarioAdmId> optionalUsuarioAdminId = service.alterar(comando);
			if (optionalUsuarioAdminId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalUsuarioAdminId.get()).toUri();
				return ResponseEntity.created(location).body("Administrador alterado com sucesso");
			} else {
				throw new InternalError("Erro interno durante a alteração do administrador");
			}
		}
		throw new AccessDeniedException("Acesso negado");
	}

	private boolean verificaUsuarioAdminExistente(UsuarioAdmId id) {
		if (!service.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}
