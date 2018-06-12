package br.hela.doenca;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.hela.doenca.comandos.BuscarDoenca;
import br.hela.doenca.comandos.CriarDoenca;
import br.hela.doenca.comandos.EditarDoenca;
import br.hela.security.AutenticaRequisicao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Doença Controller")
@RestController
@RequestMapping("/doencas")
public class DoencaController {
	@Autowired
	private DoencaService doencaService;
	@Autowired
	private AutenticaRequisicao autentica;
	
	@ApiOperation(value = "Busque todas as doenças")
	@GetMapping
	public ResponseEntity<List<BuscarDoenca>> getDoencas(@RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<List<BuscarDoenca>> optionalDoencas = doencaService.encontrar();
			return ResponseEntity.ok(optionalDoencas.get());
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Busque uma doença pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarDoenca> getDoencaPorId(@PathVariable DoencaId id, @RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarDoenca> optionalDoenca = doencaService.encontrar(id);
			if (verificaDoencaExistente(id)) {
				return ResponseEntity.ok(optionalDoenca.get());
			}
			throw new NullPointerException("A doença procurada não existe no banco de dados");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Cadastre uma nova doença")
	@PostMapping
	public ResponseEntity<String> postDoenca(@RequestBody CriarDoenca comando, @RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<DoencaId> optionalDoencaId = doencaService.salvar(comando);
			if (optionalDoencaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalDoencaId.get()).toUri();
				return ResponseEntity.created(location).body("Doença cadastrada com sucesso");
			}
			throw new Exception("A doença não foi salva devido a um erro interno");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Altere uma doença")
	@PutMapping
	public ResponseEntity<String> putDoenca(@RequestBody EditarDoenca comando, @RequestHeader String token)
			throws NullPointerException, Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaDoencaExistente(comando.getIdDoenca())) {
				throw new NullPointerException("A doença a ser alterada não existe no banco de dados");
			}
			Optional<DoencaId> optionalDoencaId = doencaService.alterar(comando);
			if (optionalDoencaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalDoencaId.get()).toUri();
				return ResponseEntity.created(location).body("Doença alterada com sucesso");
			} else {
				throw new SQLException("Erro interno durante a alteração da doença");
			}
		}
		throw new AccessDeniedException("Acesso negado");
	}

	private boolean verificaDoencaExistente(DoencaId id) throws Exception {
		if (!doencaService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}
}