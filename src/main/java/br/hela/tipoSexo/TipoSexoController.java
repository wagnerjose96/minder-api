package br.hela.tipoSexo;

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

import br.hela.security.AutenticaAdm;
import br.hela.tipoSexo.comandos.CriarTipoSexo;
import br.hela.tipoSexo.comandos.EditarTipoSexo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Genêro Controller")
@RestController
@RequestMapping("/generos")
public class TipoSexoController {
	@Autowired
	private TipoSexoService service;

	@Autowired
	private AutenticaAdm autentica;

	@ApiOperation("Busque todos os gêneros")
	@GetMapping
	public ResponseEntity<List<TipoSexo>> getGenero() throws SQLException {
		Optional<List<TipoSexo>> optionalGeneros = service.encontrarTudo();
		return ResponseEntity.ok(optionalGeneros.get());
	}

	@ApiOperation("Busque um gênero pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<TipoSexo> getGeneroPorId(@PathVariable TipoSexoId id) throws NullPointerException, SQLException {
		if (verificaGeneroExistente(id)) {
			Optional<TipoSexo> optionalGenero = service.encontrarPorId(id);
			return ResponseEntity.ok(optionalGenero.get());
		}
		throw new NullPointerException("O gênero procurado não existe no banco de dados");
	}

	@ApiOperation("Cadastre um novo gênero")
	@PostMapping
	public ResponseEntity<String> postGenero(@RequestBody CriarTipoSexo comando, @RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<TipoSexoId> optionalGeneroId = service.salvar(comando);
			if (optionalGeneroId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalGeneroId.get()).toUri();
				return ResponseEntity.created(location).body("O gênero foi cadastrado com sucesso");
			}
			throw new Exception("O gênero não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation("Altere um gênero")
	@PutMapping
	public ResponseEntity<String> putGenero(@RequestBody EditarTipoSexo comando, @RequestHeader String token)
			throws NullPointerException, Exception, SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaGeneroExistente(comando.getId())) {
				throw new NullPointerException("O gênero a ser alterado não existe no banco de dados");
			}
			Optional<TipoSexoId> optionalGeneroId = service.alterar(comando);
			if (optionalGeneroId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalGeneroId.get()).toUri();
				return ResponseEntity.created(location).body("Gênero alterado com sucesso");
			} else {
				throw new InternalError("Erro interno durante a alteração do gênero");
			}
		}
		throw new AccessDeniedException("Acesso negado");
	}

	private boolean verificaGeneroExistente(TipoSexoId id) {
		if (!service.encontrarPorId(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}
