package br.hela.emergencia;

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
import br.hela.emergencia.comandos.BuscarEmergencia;
import br.hela.emergencia.comandos.CriarEmergencia;
import br.hela.emergencia.comandos.EditarEmergencia;
import br.hela.security.AutenticaRequisicao;
import br.hela.usuario.UsuarioId;
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
	private AutenticaRequisicao autentica;

	@ApiOperation("Busque todas as emergencias")
	@GetMapping
	public ResponseEntity<List<BuscarEmergencia>> getEmergencias() throws Exception {
		Optional<List<BuscarEmergencia>> optionalEmergencias = service.encontrar();
		return ResponseEntity.ok(optionalEmergencias.get());
	}

	@ApiOperation("Busque uma emergência pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarEmergencia> getEmergenciaPorId(@PathVariable EmergenciaId id) throws Exception {
		if (verificaEmergenciaExistente(id)) {
			Optional<BuscarEmergencia> optionalEmergencia = service.encontrar(id);
			return ResponseEntity.ok(optionalEmergencia.get());
		}
		throw new NullPointerException("A emergência procurada não existe no banco de dados");
	}

	@ApiOperation("Cadastre uma nova emergência")
	@PostMapping
	public ResponseEntity<String> postEmergencia(@RequestBody CriarEmergencia comando, @RequestHeader String token)
			throws Exception {
		if (autentica.autenticaRequisicao(token)) {
			UsuarioId id = autentica.idUser(token);
			comando.setIdUsuario(id);
			Optional<EmergenciaId> optionalEmergenciaId = service.salvar(comando);
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
			throws Exception {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaEmergenciaExistente(comando.getId())) {
				throw new NullPointerException("A emergência a ser alterada não existe no banco de dados");
			}
			Optional<EmergenciaId> optionalEmergenciaId = service.alterar(comando);
			if (optionalEmergenciaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalEmergenciaId.get()).toUri();
				return ResponseEntity.created(location).body("A emergência foi alterada com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração da emergência");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	private boolean verificaEmergenciaExistente(EmergenciaId id) throws Exception {
		if (!service.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}
}