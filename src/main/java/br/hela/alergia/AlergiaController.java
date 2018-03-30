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
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import br.hela.alergia.comandos.CriarAlergia;
import br.hela.alergia.comandos.EditarAlergia;
import javassist.tools.web.BadHttpRequest;

@Api(value = "alergia", description = "Documentação dos métodos da classe AlergiaController")
@RestController
@RequestMapping("/alergias")
public class AlergiaController {
	@Autowired
	private AlergiaService alergiaService;
	
	@ApiOperation(value = "Busque todas as alergias")
	@GetMapping
	public ResponseEntity<List<Alergia>> getAlergias() throws TimeoutException, NullPointerException, BadHttpRequest{
		verificaListaAlergia();
		verificaTempoResposta();
		Optional<List<Alergia>> optionalAlergias = alergiaService.encontrar();
		if (optionalAlergias.isPresent()) {
			return ResponseEntity.ok(optionalAlergias.get());
		}
		throw new BadHttpRequest();
	}
	
	@ApiOperation(value = "Busque uma alergia pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<Alergia> getAlergiaPorId(@PathVariable AlergiaId id)
			throws TimeoutException, NullPointerException, BadHttpRequest {
		verificaAlergiaExistente(id);
		verificaTempoResposta();
		Optional<Alergia> optionalAlergia = alergiaService.encontrar(id);
		if (optionalAlergia.isPresent()) {
			return ResponseEntity.ok(optionalAlergia.get());
		}
		throw new BadHttpRequest();
	}
	
	@ApiOperation(value = "Delete uma alergia pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletarAlergia(@PathVariable AlergiaId id) throws TimeoutException, NullPointerException,  BadHttpRequest {
		verificaAlergiaExistente(id);
		verificaTempoResposta();
		Optional<String> optionalAlergia = alergiaService.deletar(id);
		if (optionalAlergia.isPresent()) {
			return ResponseEntity.ok("Alergia deletada com sucesso");
		}
		throw new BadHttpRequest();
	}

	@ApiOperation(value = "Cadastre uma nova alergia")
	@PostMapping
	public ResponseEntity<String> postAlergia(@RequestBody CriarAlergia comando)
			throws TimeoutException, NullPointerException, BadHttpRequest {
		verificaTempoResposta();
		Optional<AlergiaId> optionalAlergiaId = alergiaService.salvar(comando);
		verificaAlergiaExistente(optionalAlergiaId.get());
		if (optionalAlergiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalAlergiaId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		throw new BadHttpRequest();
	}

	@ApiOperation(value = "Altere uma alergia")
	@PutMapping
	public ResponseEntity<String> putAlergia(@RequestBody EditarAlergia comando) throws TimeoutException, NullPointerException, BadHttpRequest {
		verificaAlergiaExistente(comando.getIdAlergia());
		verificaTempoResposta();
		Optional<AlergiaId> optionalAlergiaId = alergiaService.alterar(comando);
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

	private void verificaAlergiaExistente(AlergiaId id) throws NullPointerException {
		if (!alergiaService.encontrar(id).isPresent()) {
			throw new NullPointerException("Alergia não encontrada");
		}
	}

	private void verificaListaAlergia() throws NullPointerException {
		if (!alergiaService.encontrar().isPresent()) {
			throw new NullPointerException("Nenhuma alergia cadastrada");
		}
	}
}