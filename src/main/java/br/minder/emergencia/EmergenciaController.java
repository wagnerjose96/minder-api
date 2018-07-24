package br.minder.emergencia;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.minder.emergencia.comandos.BuscarEmergencia;
import br.minder.emergencia.comandos.CriarEmergencia;
import br.minder.emergencia.comandos.EditarEmergencia;
import br.minder.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Emergência Controller")
@RestController
@RequestMapping("/emergencias")
@CrossOrigin
public class EmergenciaController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private EmergenciaService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque a sua emergencias")
	@GetMapping
	public ResponseEntity<BuscarEmergencia> getEmergencia(@RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarEmergencia> optionalEmergencias = service.encontrar(autentica.idUser(token));
			if (optionalEmergencias.isPresent()) {
				return ResponseEntity.ok(optionalEmergencias.get());
			}
			throw new NullPointerException("Não existe nenhuma emergência cadastrada no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Cadastre uma nova emergência")
	@PostMapping
	public ResponseEntity<String> postEmergencia(@RequestBody CriarEmergencia comando, @RequestHeader String token)
			throws SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<EmergenciaId> optionalEmergenciaId = service.salvar(comando, autentica.idUser(token));
			if (optionalEmergenciaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalEmergenciaId.get()).toUri();
				return ResponseEntity.created(location).body("A emergência foi cadastrada com sucesso");
			}
			throw new SQLException("A emergência não foi salva devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Altere uma emergência")
	@PutMapping
	public ResponseEntity<String> putEmergencia(@RequestBody EditarEmergencia comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicao(token)) {
			if (!service.encontrar(comando.getId(), autentica.idUser(token)).isPresent()) {
				throw new NullPointerException("A emergência a ser alterada não existe no banco de dados");
			}
			Optional<EmergenciaId> optionalEmergenciaId = service.alterar(comando);
			if (optionalEmergenciaId.isPresent()) {
				return ResponseEntity.ok().body("A emergência foi alterada com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração da emergência");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

}