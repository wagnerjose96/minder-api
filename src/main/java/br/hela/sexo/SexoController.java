package br.hela.sexo;

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
import br.hela.sexo.comandos.BuscarSexo;
import br.hela.sexo.comandos.CriarSexo;
import br.hela.sexo.comandos.EditarSexo;
import br.hela.security.AutenticaAdm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Genêro Controller")
@RestController
@RequestMapping("/generos")
public class SexoController {
	@Autowired
	private SexoService service;

	@Autowired
	private AutenticaAdm autentica;

	@ApiOperation("Busque todos os genêros")
	@GetMapping
	public ResponseEntity<List<BuscarSexo>> getSexo() throws SQLException, Exception {
		Optional<List<BuscarSexo>> optionalGeneros = service.encontrar();
		return ResponseEntity.ok(optionalGeneros.get());
	}

	@ApiOperation("Busque um genêro pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarSexo> getSexoPorId(@PathVariable SexoId id)
			throws SQLException, Exception, NullPointerException {
		if (verificaSexoExistente(id)) {
			Optional<BuscarSexo> optionalGenero = service.encontrar(id);
			return ResponseEntity.ok(optionalGenero.get());
		}
		throw new NullPointerException("O genêro procurado não existe no banco de dados");
	}

	@ApiOperation("Cadastre um novo genêro")
	@PostMapping
	public ResponseEntity<String> postSexo(@RequestBody CriarSexo comando, @RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<SexoId> optionalGeneroId = service.salvar(comando);
			if (optionalGeneroId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalGeneroId.get()).toUri();
				return ResponseEntity.created(location).body("O genêro foi cadastrado com sucesso");
			}
			throw new Exception("O genêro não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation("Altere um genêro")
	@PutMapping
	public ResponseEntity<String> putSexo(@RequestBody EditarSexo comando, @RequestHeader String token)
			throws NullPointerException, Exception, SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaSexoExistente(comando.getIdSexo())) {
				throw new NullPointerException("O genêro a ser alterado não existe no banco de dados");
			}
			Optional<SexoId> optionalSexoId = service.alterar(comando);
			if (optionalSexoId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalSexoId.get()).toUri();
				return ResponseEntity.created(location).body("O genêro foi alterado com sucesso");
			} else {
				throw new InternalError("Ocorreu um erro interno durante a alteração do genêro");
			}
		}
		throw new AccessDeniedException("Acesso negado");
	}

	private boolean verificaSexoExistente(SexoId id) {
		if (!service.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}
}