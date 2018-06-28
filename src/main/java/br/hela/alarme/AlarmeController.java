package br.hela.alarme;

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
import br.hela.alarme.comandos.BuscarAlarme;
import br.hela.alarme.comandos.CriarAlarme;
import br.hela.alarme.comandos.EditarAlarme;
import br.hela.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Alarme Controller")
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
			return ResponseEntity.notFound().build();
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Busque o alarme pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarAlarme> getAlarmePorId(@PathVariable AlarmeId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarAlarme> optionalAlarme = alarmeService.encontrar(id);
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
				return ResponseEntity.created(location).body("Alarme cadastrado com sucesso");
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
			if (!alarmeService.encontrar(comando.getId()).isPresent()) {
				throw new NullPointerException("O alarme a ser alterado não existe no banco de dados");
			}
			Optional<AlarmeId> optionalAlarmeId = alarmeService.alterar(comando);
			if (optionalAlarmeId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalAlarmeId.get()).toUri();
				return ResponseEntity.created(location).body("Alarme alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do alarme");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Delete um alarme pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deleteAlarme(@PathVariable AlarmeId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!alarmeService.encontrar(id).isPresent()) {
				throw new NullPointerException("O alarme a ser deletado não existe no banco de dados");
			}
			Optional<String> resultado = alarmeService.deletar(id);
			return ResponseEntity.ok(resultado);
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

}