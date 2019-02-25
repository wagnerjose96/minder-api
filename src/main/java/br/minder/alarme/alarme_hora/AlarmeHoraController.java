package br.minder.alarme.alarme_hora;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.minder.alarme.AlarmeId;
import br.minder.alarme.alarme_hora.comandos.CriarAlarmeHora;
import br.minder.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Alarme Hora Controller")
@RestController
@RequestMapping("/api/alarme/hora")
@CrossOrigin
public class AlarmeHoraController {
	@Autowired
	private AlarmeHoraService alarmeHoraService;

	@Autowired
	private Autentica autentica;

	private static final String ACESSONEGADO = "Acesso negado";

	@ApiOperation("Busque todos os horários dos alarmes")
	@GetMapping
	public ResponseEntity<List<AlarmeHora>> getAlarmes(@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<List<AlarmeHora>> optionalAlarmes = alarmeHoraService.encontrar(autentica.idUser(token));
			if (optionalAlarmes.isPresent()) {
				return ResponseEntity.ok(optionalAlarmes.get());
			}
			throw new NullPointerException("Não existe nenhum alarme cadastrado no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Busque as horas de um alarme pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<List<AlarmeHora>> getAlarmePorId(@PathVariable AlarmeId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<List<AlarmeHora>> optionalAlarme = alarmeHoraService.encontrar(id, autentica.idUser(token));
			if (optionalAlarme.isPresent()) {
				return ResponseEntity.ok(optionalAlarme.get());
			}
			throw new NullPointerException("O alarme hora procurado não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Cadastre um novo alarme hora")
	@PostMapping
	public ResponseEntity<String> postAlarme(@RequestBody CriarAlarmeHora comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<AlarmeHoraId> optionalAlarmeId = alarmeHoraService.salvar(comando, autentica.idUser(token));
			if (optionalAlarmeId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalAlarmeId.get()).toUri();
				return ResponseEntity.created(location).body("O horário do alarme foi cadastrado com sucesso");
			}
			throw new SQLException("O alarme hora não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

}
