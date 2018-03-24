package br.hela.cirurgia;

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
import br.hela.cirurgia.comandos.CriarCirurgia;
import javassist.tools.web.BadHttpRequest;

@RestController
@RequestMapping("/cirurgias")
public class CirurgiaController {
	@Autowired
	private CirurgiaService cirurgiaService;

	@GetMapping
	public ResponseEntity<List<Cirurgia>> getCirurgias() throws TimeoutException, NullPointerException, BadHttpRequest{
		verificaListaCirurgia();
		verificaTempoResposta();
		Optional<List<Cirurgia>> optionalCirurgias = cirurgiaService.encontrar();
		if (optionalCirurgias.isPresent()) {
			return ResponseEntity.ok(optionalCirurgias.get());
		}
		throw new BadHttpRequest();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cirurgia> getCirurgiaPorId(@PathVariable CirurgiaId id)
			throws TimeoutException, NullPointerException, BadHttpRequest {
		verificaCirurgiaExistente(id);
		verificaTempoResposta();
		Optional<Cirurgia> optionalCirurgia = cirurgiaService.encontrar(id);
		if (optionalCirurgia.isPresent()) {
			return ResponseEntity.ok(optionalCirurgia.get());
		}
		throw new BadHttpRequest();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletarCirurgia(@PathVariable CirurgiaId id) throws TimeoutException, NullPointerException,  BadHttpRequest {
		verificaCirurgiaExistente(id);
		verificaTempoResposta();
		Optional<String> optionalCirurgia = cirurgiaService.deletar(id);
		if (optionalCirurgia.isPresent()) {
			return ResponseEntity.ok(optionalCirurgia.get());
		}
		throw new BadHttpRequest();
	}

	@PostMapping
	public ResponseEntity<String> postCirurgia(@RequestBody CriarCirurgia comando)
			throws TimeoutException, NullPointerException, BadHttpRequest {
		verificaTempoResposta();
		Optional<CirurgiaId> optionalCirurgiaId = cirurgiaService.executar(comando);
		verificaCirurgiaExistente(optionalCirurgiaId.get());
		if (optionalCirurgiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalCirurgiaId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		throw new BadHttpRequest();
	}

	@PutMapping()
	public ResponseEntity<String> putCirurgia(@RequestBody Cirurgia comando) throws TimeoutException, NullPointerException, BadHttpRequest {
		verificaCirurgiaExistente(comando.getIdCirurgia());
		verificaTempoResposta();
		Optional<CirurgiaId> optionalCirurgiaId = cirurgiaService.alterar(comando);
		if (optionalCirurgiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalCirurgiaId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		throw new BadHttpRequest();
	}
	private void verificaTempoResposta() throws TimeoutException {
		if (System.currentTimeMillis() == 10) {
			throw new TimeoutException("Servidor sem resposta");
		}
	}

	private void verificaCirurgiaExistente(CirurgiaId id) throws NullPointerException {
		if (!cirurgiaService.encontrar(id).isPresent()) {
			throw new NullPointerException("Cirurgia não encontrada");
		}
	}

	private void verificaListaCirurgia() throws NullPointerException {
		if (!cirurgiaService.encontrar().isPresent()) {
			throw new NullPointerException("Nenhuma cirurgia cadastrada");
		}
	}
}