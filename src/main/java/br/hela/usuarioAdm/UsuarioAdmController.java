package br.hela.usuarioAdm;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
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
import br.hela.usuarioAdm.UsuarioAdmId;
import br.hela.usuarioAdm.UsuarioAdmService;
import br.hela.usuarioAdm.comandos.BuscarUsuarioAdm;
import br.hela.usuarioAdm.comandos.CriarUsuarioAdm;
import br.hela.usuarioAdm.comandos.EditarUsuarioAdm;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/adm")
public class UsuarioAdmController {
	@Autowired
	private UsuarioAdmService service;

	@Autowired
	private AutenticaAdm autentica;

	@GetMapping
	public ResponseEntity<List<BuscarUsuarioAdm>> getUsuarioAdmins(@RequestHeader String token)
			throws SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<List<BuscarUsuarioAdm>> optionalUsuarioAdmin = service.encontrar();
			return ResponseEntity.ok(optionalUsuarioAdmin.get());
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@GetMapping("/{id}")
	public ResponseEntity<BuscarUsuarioAdm> getUsuarioAdminId(@PathVariable UsuarioAdmId id, @RequestHeader String token)
			throws NullPointerException, SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (verificaUsuarioAdminExistente(id)) {
				Optional<BuscarUsuarioAdm> optionalUsuarioAdmin = service.encontrar(id);
				return ResponseEntity.ok(optionalUsuarioAdmin.get());
			}
			throw new NullPointerException("O administrador procurado não existe no banco de dados");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deleteUsuarioAdmin(@PathVariable UsuarioAdmId id,
			@RequestHeader String token) throws NullPointerException, AccessDeniedException, SQLException, Exception {
		if (autentica.autenticaRequisicao(token)) {
			if (verificaUsuarioAdminExistente(id)) {
				Optional<String> resultado = service.deletar(id);
				return ResponseEntity.ok(resultado);
			}
			throw new NullPointerException("O administrador a deletar não existe no banco de dados");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@PostMapping
	public ResponseEntity<String> postUsuarioAdmin(@RequestBody CriarUsuarioAdm comando) throws Exception {
		Optional<UsuarioAdmId> optionalUsuarioAdminId = service.salvar(comando);
		if (optionalUsuarioAdminId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalUsuarioAdminId.get()).toUri();
			return ResponseEntity.created(location).body("O administrador foi cadastrado com sucesso");
		}
		throw new Exception("O administrador não foi salvo devido a um erro interno");
	}

	@PutMapping
	public ResponseEntity<String> putUsuarioAdmin(@RequestHeader String token, @RequestBody EditarUsuarioAdm comando)
			throws NullPointerException, Exception, SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaUsuarioAdminExistente(comando.getId())) {
				throw new NullPointerException("O administrador a ser alterado não existe no banco de dados");
			}
			Optional<UsuarioAdmId> optionalUsuarioAdminId = service.alterar(comando);
			if (optionalUsuarioAdminId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalUsuarioAdminId.get()).toUri();
				return ResponseEntity.created(location).body("O administrador foi alterado com sucesso");
			} else {
				throw new InternalError("Ocorreu um erro interno durante a alteração do administrador");
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
