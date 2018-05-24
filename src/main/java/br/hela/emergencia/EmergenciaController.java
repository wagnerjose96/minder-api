package br.hela.emergencia;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.hela.emergencia.comandos.CriarEmergencia;
import br.hela.emergencia.comandos.EditarEmergencia;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Emergência Controller")
@RestController
@RequestMapping("/emergencias")
public class EmergenciaController {

	@Autowired
	private EmergenciaService service;

	@ApiOperation(value = "Busque todas as emergências")
	@GetMapping
	public ResponseEntity<List<Emergencia>> getEmergencias() throws Exception {
		Optional<List<Emergencia>> optionalEmergencia = service.encontrar();
		return ResponseEntity.ok(optionalEmergencia.get());
	}

	@ApiOperation(value = "Busque uma emergência pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<Emergencia> getEmergenciaId(@PathVariable EmergenciaId id) throws NullPointerException {

		if (verificaEmergenciaExistente(id)) {
			Optional<Emergencia> optionalEmergencia = service.encontrar(id);
			return ResponseEntity.ok(optionalEmergencia.get());
		}
		throw new NullPointerException("A emergência procurada não existe no banco de dados");
	}

	@ApiOperation(value = "Cadastre uma nova emergência")
	@PostMapping
	public ResponseEntity<String> postEmergencia(@RequestBody CriarEmergencia comando) throws Exception {

		Optional<EmergenciaId> optionalEmergenciaId = service.salvar(comando);
		if (optionalEmergenciaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalEmergenciaId.get()).toUri();
			return ResponseEntity.created(location).body("Emergência criada com sucesso");
		}
		throw new Exception("A emergência não foi salva devido a um erro interno");
	}

	@ApiOperation(value = "Altere uma emergência")
	@PutMapping
	public ResponseEntity<String> putEmergencia(@RequestBody EditarEmergencia comando)
			throws NullPointerException, InternalError {

		if (!verificaEmergenciaExistente(comando.getId())) {
			throw new NullPointerException("A emergência a ser alterada não existe no banco de dados");
		}
		Optional<EmergenciaId> optionalEmergenciaId = service.alterar(comando);
		if (optionalEmergenciaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalEmergenciaId.get()).toUri();
			return ResponseEntity.created(location).body("Emergência alterada com sucesso");
		} else {
			throw new InternalError("Erro interno durante a alteração da emergência");
		}

	}

	private boolean verificaEmergenciaExistente(EmergenciaId id) {
		if (!service.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}
