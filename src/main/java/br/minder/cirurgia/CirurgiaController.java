package br.minder.cirurgia;

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

import br.minder.cirurgia.comandos.BuscarCirurgia;
import br.minder.cirurgia.comandos.CriarCirurgia;
import br.minder.cirurgia.comandos.EditarCirurgia;
import br.minder.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Cirurgia Controller")
@RestController
@RequestMapping("/api/cirurgia")
@CrossOrigin
public class CirurgiaController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private CirurgiaService cirurgiaService;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque todas as cirurgias")
	@GetMapping
	public ResponseEntity<Page<BuscarCirurgia>> getCirurgias(Pageable p, @RequestHeader String token,
			@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<Page<BuscarCirurgia>> optionalCirurgias = cirurgiaService.encontrar(p, autentica.idUser(token),
					searchTerm);
			if (optionalCirurgias.isPresent()) {
				return ResponseEntity.ok(optionalCirurgias.get());
			}
			throw new NullPointerException("Não existe nenhuma cirurgia cadastrada no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Busque uma cirurgia pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarCirurgia> getCirurgiaPorId(@PathVariable CirurgiaId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarCirurgia> optionalCirurgia = cirurgiaService.encontrar(id, autentica.idUser(token));
			if (optionalCirurgia.isPresent()) {
				return ResponseEntity.ok(optionalCirurgia.get());
			}
			throw new NullPointerException("A cirurgia procurada não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Cadastre uma nova cirurgia")
	@PostMapping
	public ResponseEntity<String> postCirurgia(@RequestBody CriarCirurgia comando, @RequestHeader String token)
			throws SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<CirurgiaId> optionalCirurgiaId = cirurgiaService.salvar(comando, autentica.idUser(token));
			if (optionalCirurgiaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalCirurgiaId.get()).toUri();
				return ResponseEntity.created(location).body("A cirurgia foi cadastrada com sucesso");
			}
			throw new SQLException("A cirurgia não foi salva devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Altere uma cirurgia")
	@PutMapping
	public ResponseEntity<String> putCirurgia(@RequestBody EditarCirurgia comando, @RequestHeader String token)
			throws SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (cirurgiaService.encontrar(comando.getIdCirurgia(), autentica.idUser(token)).isPresent()) {
				Optional<CirurgiaId> optionalCirurgiaId = cirurgiaService.alterar(comando);
				if (optionalCirurgiaId.isPresent()) {
					return ResponseEntity.ok().body("A cirurgia foi alterada com sucesso");
				} else {
					throw new SQLException("Ocorreu um erro interno durante a alteração do cirurgia");
				}
			}
			throw new NullPointerException("A cirurgia a ser alterada não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

}
