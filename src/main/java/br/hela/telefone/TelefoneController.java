package br.hela.telefone;

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
import br.hela.telefone.comandos.CriarTelefone;
import br.hela.telefone.comandos.EditarTelefone;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/telefones")
public class TelefoneController {
	@Autowired
	private TelefoneService service;
	
	@Autowired
	private AutenticaRequisicao autentica;

	@ApiOperation(value = "Busque todos os telefones")
	@GetMapping
	public ResponseEntity<List<Telefone>> getTelefone(@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<List<Telefone>> optionalTelefones = service.encontrar();
			return ResponseEntity.ok(optionalTelefones.get());
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Busque um telefone pelo id")
	@GetMapping("/{id}")
	public ResponseEntity<Telefone> getTelefonePorId(@PathVariable TelefoneId id, @RequestHeader String token)
			throws NullPointerException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (verificaTelefoneExistente(id)) {
				Optional<Telefone> optionalTelefone = service.encontrar(id);
				return ResponseEntity.ok(optionalTelefone.get());
			}
			throw new NullPointerException("O telefone procurado não existe no banco de dados");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Delete um telefone pelo id")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarTelefone(@PathVariable TelefoneId id, @RequestHeader String token)
			throws NullPointerException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (verificaTelefoneExistente(id)) {
				Optional<String> optionalTelefone = service.deletar(id);
				return ResponseEntity.ok(optionalTelefone);
			}
			throw new NullPointerException("O telefone a deletar não existe no banco de dados");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Cadastre um novo telefone")
	@PostMapping
	public ResponseEntity<String> postTelefone(@RequestBody CriarTelefone comando, @RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<TelefoneId> optionalTelefoneId = service.salvar(comando);
			if (optionalTelefoneId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalTelefoneId.get()).toUri();
				return ResponseEntity.created(location).body("O telefone foi cadastrado com sucesso");
			}
			throw new Exception("O telefone não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Altere um telefone")
	@PutMapping
	public ResponseEntity<String> putTelefoneContinuo(@RequestBody EditarTelefone comando, @RequestHeader String token)
			throws NullPointerException, InternalError, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaTelefoneExistente(comando.getIdTelefone())) {
				throw new NullPointerException("O telefone a ser alterado não existe no banco de dados");
			}
			Optional<TelefoneId> optionalTelefoneId = service.alterar(comando);
			if (optionalTelefoneId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalTelefoneId.get()).toUri();
				return ResponseEntity.created(location).body("Telefone alterado com sucesso");
			} else {
				throw new InternalError("Erro interno durante a alteração do telefone");
			}
		}
		throw new AccessDeniedException("Acesso negado");
	}

	private boolean verificaTelefoneExistente(TelefoneId id) {
		if (!service.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}