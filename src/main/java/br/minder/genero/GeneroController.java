package br.minder.genero;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.minder.genero.comandos.BuscarGenero;
import br.minder.genero.comandos.CriarGenero;
import br.minder.genero.comandos.EditarGenero;
import br.minder.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Gênero Controller")
@RestController
@RequestMapping("/generos")
@CrossOrigin
public class GeneroController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private GeneroService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque todos os gêneros")
	@GetMapping
	public ResponseEntity<List<BuscarGenero>> getSexo() {
		Optional<List<BuscarGenero>> optionalGeneros = service.encontrar();
		if (optionalGeneros.isPresent()) {
			return ResponseEntity.ok(optionalGeneros.get());
		}
		throw new NullPointerException("Não existe nenhum gênero cadastrado no banco de dados");
	}

	@ApiOperation("Busque um gênero pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarGenero> getSexoPorId(@PathVariable GeneroId id) {
		Optional<BuscarGenero> optionalGenero = service.encontrar(id);
		if (optionalGenero.isPresent()) {
			return ResponseEntity.ok(optionalGenero.get());
		}
		throw new NullPointerException("O gênero procurado não existe no banco de dados");
	}

	@ApiOperation("Cadastre um novo gênero")
	@PostMapping
	public ResponseEntity<String> postSexo(@RequestBody CriarGenero comando, @RequestHeader String token)
			throws SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			Optional<GeneroId> optionalGeneroId = service.salvar(comando);
			if (optionalGeneroId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalGeneroId.get()).toUri();
				return ResponseEntity.created(location).body("O gênero foi cadastrado com sucesso");
			}
			throw new SQLException("O gênero não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Altere um gênero")
	@PutMapping
	public ResponseEntity<String> putSexo(@RequestBody EditarGenero comando, @RequestHeader String token)
			throws SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			if (!service.encontrar(comando.getId()).isPresent()) {
				throw new NullPointerException("O gênero a ser alterado não existe no banco de dados");
			}
			Optional<GeneroId> optionalSexoId = service.alterar(comando);
			if (optionalSexoId.isPresent()) {
				return ResponseEntity.ok().body("O gênero foi alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do gênero");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}
}