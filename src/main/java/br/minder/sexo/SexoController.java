package br.minder.sexo;

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

import br.minder.security.Autentica;
import br.minder.sexo.comandos.BuscarSexo;
import br.minder.sexo.comandos.CriarSexo;
import br.minder.sexo.comandos.EditarSexo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Genêro Controller")
@RestController
@RequestMapping("/generos")
@CrossOrigin
public class SexoController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private SexoService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque todos os genêros")
	@GetMapping
	public ResponseEntity<List<BuscarSexo>> getSexo() {
		Optional<List<BuscarSexo>> optionalGeneros = service.encontrar();
		if (optionalGeneros.isPresent()) {
			return ResponseEntity.ok(optionalGeneros.get());
		}
		throw new NullPointerException("Não existe nenhum genêro cadastrado no banco de dados");
	}

	@ApiOperation("Busque um genêro pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarSexo> getSexoPorId(@PathVariable SexoId id) {
		Optional<BuscarSexo> optionalGenero = service.encontrar(id);
		if (optionalGenero.isPresent()) {
			return ResponseEntity.ok(optionalGenero.get());
		}
		throw new NullPointerException("O genêro procurado não existe no banco de dados");
	}

	@ApiOperation("Cadastre um novo genêro")
	@PostMapping
	public ResponseEntity<String> postSexo(@RequestBody CriarSexo comando, @RequestHeader String token)
			throws SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			Optional<SexoId> optionalGeneroId = service.salvar(comando);
			if (optionalGeneroId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalGeneroId.get()).toUri();
				return ResponseEntity.created(location).body("O genêro foi cadastrado com sucesso");
			}
			throw new SQLException("O genêro não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Altere um genêro")
	@PutMapping
	public ResponseEntity<String> putSexo(@RequestBody EditarSexo comando, @RequestHeader String token)
			throws SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			if (!service.encontrar(comando.getId()).isPresent()) {
				throw new NullPointerException("O genêro a ser alterado não existe no banco de dados");
			}
			Optional<SexoId> optionalSexoId = service.alterar(comando);
			if (optionalSexoId.isPresent()) {
				return ResponseEntity.ok().body("O genêro foi alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do genêro");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}
}