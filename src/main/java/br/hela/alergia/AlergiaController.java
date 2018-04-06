package br.hela.alergia;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.hela.alergia.comandos.CriarAlergia;
import br.hela.alergia.comandos.EditarAlergia;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.tools.web.BadHttpRequest;

@Api(description = "Basic Alergia Controller")
@RestController
@RequestMapping("/alergias")
public class AlergiaController {
	@Autowired
	private AlergiaService service;

	@ApiOperation(value = "Busque todas as alergias")
	@GetMapping
	public ResponseEntity<List<Alergia>> getAlergias() 
			throws TimeoutException, NullPointerException, BadHttpRequest {

		verificaListaAlergia();
		verificaTempoResposta();
		Optional<List<Alergia>> optionalAlergia = service.encontrar();
		if (optionalAlergia.isPresent()) {
			return ResponseEntity.ok(optionalAlergia.get());
		}
		throw new BadHttpRequest();
	}

	@ApiOperation(value = "Busque a alergia pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<Alergia> getAlergiaId(@PathVariable AlergiaId id)
			throws TimeoutException, NullPointerException, BadHttpRequest {

		verificaAlergiaExitente(id);
		verificaTempoResposta();
		Optional<Alergia> optionalAlergia = service.encontrar(id);
		if (optionalAlergia.isPresent()) {
			return ResponseEntity.ok(optionalAlergia.get());
		}
		throw new BadHttpRequest();
	}
	
	@ApiOperation(value = "Delete uma alergia pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deleteAlergia(@PathVariable AlergiaId id)
			throws TimeoutException, NullPointerException, BadHttpRequest {

		verificaAlergiaExitente(id);
		verificaTempoResposta();
		Optional<String> resultado = service.deletar(id);
		if (resultado.isPresent()) {
			return ResponseEntity.ok(resultado);
		}
		throw new BadHttpRequest();
	}

	@ApiOperation(value = "Cadastre uma nova alergia")
	@PostMapping
	public ResponseEntity<AlergiaId> postAlergia(@RequestBody CriarAlergia comando)
			throws TimeoutException, NullPointerException, BadHttpRequest {

		verificaTempoResposta();
		Optional<AlergiaId> optionalAlergiaId = service.salvar(comando);
		verificaAlergiaExitente(optionalAlergiaId.get());
		if (optionalAlergiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalAlergiaId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		throw new BadHttpRequest();
	}
	
	@ApiOperation(value = "Altere uma alergia")
	@PutMapping
	public ResponseEntity<AlergiaId> putAlergia(@RequestBody EditarAlergia comando)
			throws TimeoutException, NullPointerException, BadHttpRequest {

		verificaAlergiaExitente(comando.getId());
		verificaTempoResposta();
		Optional<AlergiaId> optionalAlergiaId = service.alterar(comando);
		if (optionalAlergiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalAlergiaId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		throw new BadHttpRequest();
	}

	private void verificaTempoResposta() throws TimeoutException {
		if (System.currentTimeMillis() == 10) {
			throw new TimeoutException("Servidor sem resposta");
		}
	}

	private void verificaAlergiaExitente(AlergiaId id) throws NullPointerException {
		if (!service.encontrar(id).isPresent()) {
			throw new NullPointerException("Alergia não encontrada");
		}
	}

	private void verificaListaAlergia() throws NullPointerException {
		if (!service.encontrar().isPresent()) {
			throw new NullPointerException("Nenhuma alergia cadastrada");
		}
	}

}
