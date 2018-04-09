package br.hela.doenca;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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
import br.hela.doenca.comandos.CriarDoenca;
import br.hela.doenca.comandos.EditarDoenca;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Doença Controller")
@RestController
@RequestMapping("/doencas")
public class DoencaController {
	@Autowired
	private DoencaService doencaService;

	@ApiOperation(value = "Busque todas as doenças")
	@GetMapping
	public ResponseEntity<List<Doenca>> getDoencas() {

		Optional<List<Doenca>> optionalDoencas = doencaService.encontrar();
		return ResponseEntity.ok(optionalDoencas.get());
	}

	@ApiOperation(value = "Busque uma doença pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<Doenca> getDoencaPorId(@PathVariable DoencaId id) throws NullPointerException {

		Optional<Doenca> optionalDoenca = doencaService.encontrar(id);
		if (verificaDoencaExistente(id)) {
			return ResponseEntity.ok(optionalDoenca.get());
		}
		throw new NullPointerException("A doença procurada não existe no banco de dados");
	}

	@ApiOperation(value = "Delete uma doença pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarDoenca(@PathVariable DoencaId id) throws NullPointerException {

		if (verificaDoencaExistente(id)) {
			Optional<String> optionalDoenca = doencaService.deletar(id);
			return ResponseEntity.ok(optionalDoenca);
		}
		throw new NullPointerException("A doença a deletar não existe no banco de dados");
	}

	@ApiOperation(value = "Cadastre uma nova doença")
	@PostMapping
	public ResponseEntity<String> postDoenca(@RequestBody CriarDoenca comando) throws Exception {

		Optional<DoencaId> optionalDoencaId = doencaService.executar(comando);
		if (optionalDoencaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalDoencaId.get()).toUri();
			return ResponseEntity.created(location).body("Doença criada com sucesso");
		}
		throw new Exception("A doença não foi salva devido a um erro interno");

	}

	@ApiOperation(value = "Altere uma doença")
	@PutMapping
	public ResponseEntity<String> putDoenca(@RequestBody EditarDoenca comando) throws NullPointerException, Exception {
		if (!verificaDoencaExistente(comando.getIdDoenca())) {
			throw new NullPointerException("A doença a ser alterada não existe no banco de dados");
		}
		Optional<DoencaId> optionalDoencaId = doencaService.alterar(comando);
		if (optionalDoencaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalDoencaId.get()).toUri();
			return ResponseEntity.created(location).body("Doença alterada com sucesso");
		} else {
			throw new SQLException("Erro interno durante a alteração da doença");
		}

	}

	private boolean verificaDoencaExistente(DoencaId id) {
		if (!doencaService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}
