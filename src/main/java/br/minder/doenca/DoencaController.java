package br.minder.doenca;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.minder.doenca.comandos.BuscarDoenca;
import br.minder.doenca.comandos.CriarDoenca;
import br.minder.doenca.comandos.EditarDoenca;
import br.minder.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Doença Controller")
@RestController
@RequestMapping("/api/doenca")
@CrossOrigin
public class DoencaController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private DoencaService doencaService;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque todas as doenças")
	@GetMapping
	public ResponseEntity<Page<BuscarDoenca>> getDoencas(Pageable p, @RequestHeader String token,
			@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<Page<BuscarDoenca>> optionalDoencas = doencaService.encontrar(p, autentica.idUser(token),
					searchTerm);
			if (optionalDoencas.isPresent()) {
				return ResponseEntity.ok(optionalDoencas.get());
			}
			throw new NullPointerException("Não existe nenhuma doença cadastrada no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Busque uma doença pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarDoenca> getDoencaPorId(@PathVariable DoencaId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarDoenca> optionalDoenca = doencaService.encontrar(id, autentica.idUser(token));
			if (optionalDoenca.isPresent()) {
				return ResponseEntity.ok(optionalDoenca.get());
			}
			throw new NullPointerException("A doença procurada não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Cadastre uma nova doença")
	@PostMapping
	public ResponseEntity<String> postDoenca(@RequestBody CriarDoenca comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<DoencaId> optionalDoencaId = doencaService.salvar(comando, autentica.idUser(token));
			if (optionalDoencaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalDoencaId.get()).toUri();
				return ResponseEntity.created(location).body("A doença foi cadastrada com sucesso");
			}
			throw new SQLException("A doença não foi salva devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Altere uma doença")
	@PutMapping
	public ResponseEntity<String> putDoenca(@RequestBody EditarDoenca comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicao(token)) {
			if (doencaService.encontrar(comando.getIdDoenca(), autentica.idUser(token)).isPresent()) {
				Optional<DoencaId> optionalDoencaId = doencaService.alterar(comando);
				if (optionalDoencaId.isPresent()) {
					return ResponseEntity.ok().body("A doença foi alterada com sucesso");
				} else {
					throw new SQLException("Ocorreu um erro interno durante a alteração da doença");
				}
			}
			throw new NullPointerException("A doença a ser alterada não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

}
