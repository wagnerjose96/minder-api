package br.minder.alarme;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import br.minder.alarme.comandos.BuscarAlarme;
import br.minder.alarme.comandos.CriarAlarme;
import br.minder.alarme.comandos.EditarAlarme;
import br.minder.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Alarme Controller")
@RestController
@RequestMapping("/alarmes")
@CrossOrigin
public class AlarmeController {
	@Autowired
	private AlarmeService alarmeService;

	@Autowired
	private Autentica autentica;

	private static final String ACESSONEGADO = "Acesso negado";

	@ApiOperation("Busque todos os alarmes")
	@GetMapping
	public ResponseEntity<List<BuscarAlarme>> getAlarmes(@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<List<BuscarAlarme>> optionalAlarmes = alarmeService.encontrar(autentica.idUser(token));
			if (optionalAlarmes.isPresent()) {
				return ResponseEntity.ok(optionalAlarmes.get());
			}
			throw new NullPointerException("Não existe nenhum alarme cadastrado no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Busque um alarme pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarAlarme> getAlarmePorId(@PathVariable AlarmeId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarAlarme> optionalAlarme = alarmeService.encontrar(id, autentica.idUser(token));
			if (optionalAlarme.isPresent()) {
				return ResponseEntity.ok(optionalAlarme.get());
			}
			throw new NullPointerException("O alarme procurado não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Cadastre um novo alarme")
	@PostMapping
	public ResponseEntity<String> postAlarme(@RequestBody CriarAlarme comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<AlarmeId> optionalAlarmeId = alarmeService.salvar(comando, autentica.idUser(token));
			if (optionalAlarmeId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalAlarmeId.get()).toUri();
				return ResponseEntity.created(location).body("O alarme foi cadastrado com sucesso");
			}
			throw new SQLException("O alarme não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Altere um alarme")
	@PutMapping
	public ResponseEntity<String> putAlarme(@RequestBody EditarAlarme comando, @RequestHeader String token)
			throws SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!alarmeService.encontrar(comando.getId(), autentica.idUser(token)).isPresent()) {
				throw new NullPointerException("O alarme a ser alterado não existe no banco de dados");
			}
			Optional<AlarmeId> optionalAlarmeId = alarmeService.alterar(comando);
			if (optionalAlarmeId.isPresent()) {
				return ResponseEntity.ok().body("O alarme foi alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do alarme");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Delete um alarme pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAlarme(@PathVariable AlarmeId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<String> resultado = alarmeService.deletar(id);
			if (resultado.isPresent())
				return ResponseEntity.ok(resultado.get());
			throw new NullPointerException("O alarme a ser deletado não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

}