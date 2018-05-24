package br.hela.convenio;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.hela.convenio.Convenio;
import br.hela.convenio.ConvenioId;
import br.hela.convenio.ConvenioService;
import br.hela.convenio.comandos.CriarConvenio;
import br.hela.convenio.comandos.EditarConvenio;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Convênio Controller")
@Controller
@RequestMapping("/convenios")
public class ConvenioController {
	@Autowired
	private ConvenioService service;

	@ApiOperation(value = "Busque todos os convênios")
	@GetMapping
	public ResponseEntity<List<Convenio>> getConvenio() {
		Optional<List<Convenio>> optionalConvenios = service.encontrar();
		return ResponseEntity.ok(optionalConvenios.get());
	}

	@ApiOperation(value = "Busque um convênio pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<Convenio> getConvenioPorId(@PathVariable ConvenioId id) throws NullPointerException {
		if (verificaConvenioExistente(id)) {
			Optional<Convenio> optionalConvenio = service.encontrar(id);
			return ResponseEntity.ok(optionalConvenio.get());
		}
		throw new NullPointerException("O convênio procurado não existe no banco de dados");
	}

	@ApiOperation(value = "Delete um convênio pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarConvenio(@PathVariable ConvenioId id) throws NullPointerException {
		if (verificaConvenioExistente(id)) {
			Optional<String> optionalConvenio = service.deletar(id);
			return ResponseEntity.ok(optionalConvenio);
		}
		throw new NullPointerException("O convênio a deletar não existe no banco de dados");
	}

	@ApiOperation(value = "Cadastre um novo convênio")
	@PostMapping
	public ResponseEntity<String> postConvenio(@RequestBody CriarConvenio comando) throws Exception {
		Optional<ConvenioId> optionalConvenioId = service.salvar(comando);
		if (optionalConvenioId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalConvenioId.get()).toUri();
			return ResponseEntity.created(location).body("O convênio foi criado com sucesso");
		}
		throw new Exception("O convênio não foi salvo devido a um erro interno");
	}

	@ApiOperation(value = "Altere um convênio")
	@PutMapping
	public ResponseEntity<String> putConvenioContinuo(@RequestBody EditarConvenio comando)
			throws NullPointerException, InternalError {

		if (!verificaConvenioExistente(comando.getId())) {
			throw new NullPointerException("O convênio a ser alterado não existe no banco de dados");
		}
		Optional<ConvenioId> optionalConvenioId = service.alterar(comando);
		if (optionalConvenioId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalConvenioId.get()).toUri();
			return ResponseEntity.created(location).body("Convenio alterado com sucesso");
		} else {
			throw new InternalError("Erro interno durante a alteração do usuário");
		}
	}

	private boolean verificaConvenioExistente(ConvenioId id) {
		if (!service.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}
