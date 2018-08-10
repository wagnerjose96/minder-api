package br.minder.alergia;

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
import br.minder.alergia.comandos.BuscarAlergia;
import br.minder.alergia.comandos.CriarAlergia;
import br.minder.alergia.comandos.EditarAlergia;
import br.minder.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Alergia Controller")
@RestController
@RequestMapping("/alergias")
@CrossOrigin
public class AlergiaController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private AlergiaService alergiaService;

	@Autowired
	private Autentica autentica;

	@Autowired
	private AlergiaRepository alergiaRepo;

	@ApiOperation("Busque todas as alergias")
	@GetMapping
	public Page<Alergia> getAlergias(@RequestHeader String token, Pageable p,
			@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (alergiaRepo.findAll(autentica.idUser(token).toString()).isEmpty())
				throw new NullPointerException("Não existe nenhuma alergia cadastrada no banco de dados");
			else {
				if (searchTerm.isEmpty()) {
					return alergiaRepo.findAll(p, autentica.idUser(token).toString());
				}
				return alergiaRepo.findAll(p, "%" + searchTerm + "%", autentica.idUser(token).toString());
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Busque uma alergia pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarAlergia> getAlergiaPorId(@PathVariable AlergiaId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarAlergia> optionalAlergia = alergiaService.encontrar(id, autentica.idUser(token));
			if (optionalAlergia.isPresent()) {
				return ResponseEntity.ok(optionalAlergia.get());
			}
			throw new NullPointerException("A alergia procurada não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Cadastre uma nova alergia")
	@PostMapping
	public ResponseEntity<String> postAlergia(@RequestBody CriarAlergia comando, @RequestHeader String token)
			throws SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<AlergiaId> optionalAlergiaId = alergiaService.salvar(comando, autentica.idUser(token));
			if (optionalAlergiaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalAlergiaId.get()).toUri();
				return ResponseEntity.created(location).body("A alergia foi cadastrada com sucesso");
			}
			throw new SQLException("A alergia não foi salva devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Altere uma alergia")
	@PutMapping
	public ResponseEntity<String> putAlergia(@RequestBody EditarAlergia comando, @RequestHeader String token)
			throws SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!alergiaService.encontrar(comando.getIdAlergia(), autentica.idUser(token)).isPresent()) {
				throw new NullPointerException("A alergia a ser alterada não existe no banco de dados");
			}
			Optional<AlergiaId> optionalAlergiaId = alergiaService.alterar(comando);
			if (optionalAlergiaId.isPresent()) {
				return ResponseEntity.ok().body("A alergia foi alterada com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração da alergia");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

}