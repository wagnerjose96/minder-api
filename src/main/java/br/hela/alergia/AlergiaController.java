package br.hela.alergia;

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
import br.hela.alergia.comandos.BuscarAlergia;
import br.hela.alergia.comandos.CriarAlergia;
import br.hela.alergia.comandos.EditarAlergia;
import br.hela.security.AutenticaRequisicao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Alergia Controller")
@RestController
@RequestMapping("/alergias")
@CrossOrigin
public class AlergiaController {
	@Autowired
	private AlergiaService alergiaService;

	@Autowired
	private AutenticaRequisicao autentica;

	@ApiOperation("Busque todas as alergias")
	@GetMapping
	public ResponseEntity<List<BuscarAlergia>> getAlergias() throws SQLException, Exception {
		Optional<List<BuscarAlergia>> optionalAlergias = alergiaService.encontrar();
		return ResponseEntity.ok(optionalAlergias.get());
	}

	@ApiOperation("Busque a alergia pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarAlergia> getAlergiaPorId(@PathVariable AlergiaId id)
			throws SQLException, NullPointerException, Exception {
		Optional<BuscarAlergia> optionalAlergia = alergiaService.encontrar(id);
		if (verificaAlergiaExistente(id)) {
			return ResponseEntity.ok(optionalAlergia.get());
		}
		throw new NullPointerException("A alergia procurada não existe no banco de dados");
	}

	@ApiOperation("Cadastre uma nova alergia")
	@PostMapping
	public ResponseEntity<String> postAlergia(@RequestBody CriarAlergia comando, @RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<AlergiaId> optionalAlergiaId = alergiaService.salvar(comando);
			if (optionalAlergiaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalAlergiaId.get()).toUri();
				return ResponseEntity.created(location).body("A alergia foi cadastrada com sucesso");
			}
			throw new Exception("A alergia não foi salva devido a um erro interno");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation("Altere uma alergia")
	@PutMapping
	public ResponseEntity<String> putAlergia(@RequestBody EditarAlergia comando, @RequestHeader String token)
			throws SQLException, NullPointerException, Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {

			if (!verificaAlergiaExistente(comando.getIdAlergia())) {
				throw new NullPointerException("A alergia a ser alterada não existe no banco de dados");
			}
			Optional<AlergiaId> optionalAlergiaId = alergiaService.alterar(comando);
			if (optionalAlergiaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalAlergiaId.get()).toUri();
				return ResponseEntity.created(location).body("A alergia foi alterada com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração da alergia");
			}
		}
		throw new AccessDeniedException("Acesso negado");
	}

	private boolean verificaAlergiaExistente(AlergiaId id) throws Exception {
		if (!alergiaService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}