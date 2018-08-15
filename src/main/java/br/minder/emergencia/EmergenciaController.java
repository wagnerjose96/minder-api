package br.minder.emergencia;

import java.net.URI;
import java.nio.file.AccessDeniedException;
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
import br.minder.emergencia.comandos.BuscarEmergenciaPdf;
import br.minder.emergencia.comandos.CriarEmergencia;
import br.minder.emergencia.comandos.EditarEmergencia;
import br.minder.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Emergência Controller")
@RestController
@RequestMapping("/api/emergencia")
@CrossOrigin
public class EmergenciaController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private EmergenciaService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque a sua emergência")
	@GetMapping
	public ResponseEntity<BuscarEmergencia> getEmergencia(@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarEmergencia> optionalEmergencias = service.encontrar(autentica.idUser(token));
			if (optionalEmergencias.isPresent()) {
				return ResponseEntity.ok(optionalEmergencias.get());
			}
			throw new NullPointerException("Não existe nenhuma emergência cadastrada no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Gere o PDF da sua emergência")
	@GetMapping("/pdf")
	public ResponseEntity<BuscarEmergenciaPdf> getEmergenciaPdf(@RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarEmergenciaPdf> optionalEmergencias = service.encontrarPdf(autentica.idUser(token));
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
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<EmergenciaId> optionalEmergenciaId = service.salvar(comando, autentica.idUser(token));
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalEmergenciaId).toUri();
			return ResponseEntity.created(location).body("A emergência foi cadastrada com sucesso");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Altere uma emergência")
	@PutMapping
	public ResponseEntity<String> putEmergencia(@RequestBody EditarEmergencia comando, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (service.alterar(comando, autentica.idUser(token)).isPresent())
				return ResponseEntity.ok().body("A emergência foi alterada com sucesso");
			throw new NullPointerException("A emergência a ser alterada não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

}