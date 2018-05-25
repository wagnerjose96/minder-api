package br.hela.cirurgia;

import java.net.URI;
import java.sql.SQLException;
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
import br.hela.cirurgia.comandos.BuscarCirurgia;
import br.hela.cirurgia.comandos.CriarCirurgia;
import br.hela.cirurgia.comandos.EditarCirurgia;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Cirurgia Controller")
@RestController
@RequestMapping("/cirurgias")
public class CirurgiaController {
	@Autowired
	private CirurgiaService cirurgiaService;

	@ApiOperation(value = "Busque todas as cirurgias")
	@GetMapping
	public ResponseEntity<List<BuscarCirurgia>> getCirurgias() throws Exception {
		Optional<List<BuscarCirurgia>> optionalCirurgias = cirurgiaService.encontrar();
		return ResponseEntity.ok(optionalCirurgias.get());
	}

	@ApiOperation(value = "Busque uma cirurgia pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarCirurgia> getCirurgiaPorId(@PathVariable CirurgiaId id)
			throws NullPointerException, Exception {

		Optional<BuscarCirurgia> optionalCirurgia = cirurgiaService.encontrar(id);
		if (verificaCirurgiaExistente(id)) {
			return ResponseEntity.ok(optionalCirurgia.get());
		}
		throw new NullPointerException("A cirurgia procurada não existe no banco de dados");
	}

	@ApiOperation(value = "Cadastre uma nova cirurgia")
	@PostMapping
	public ResponseEntity<String> postCirurgia(@RequestBody CriarCirurgia comando) throws Exception {
		Optional<CirurgiaId> optionalCirurgiaId = cirurgiaService.salvar(comando);
		if (optionalCirurgiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalCirurgiaId.get()).toUri();
			return ResponseEntity.created(location).body("Cirurgia cadastrada com sucesso");
		}
		throw new Exception("A cirurgia não foi salva devido a um erro interno");
	}

	@ApiOperation(value = "Altere uma cirurgia")
	@PutMapping
	public ResponseEntity<String> putCirurgia(@RequestBody EditarCirurgia comando)
			throws SQLException, NullPointerException, Exception {

		if (!verificaCirurgiaExistente(comando.getIdCirurgia())) {
			throw new NullPointerException("A cirurgia a ser alterada não existe no banco de dados");
		}
		Optional<CirurgiaId> optionalCirurgiaId = cirurgiaService.alterar(comando);
		if (optionalCirurgiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalCirurgiaId.get()).toUri();
			return ResponseEntity.created(location).body("Cirurgia alterada com sucesso");
		} else {
			throw new SQLException("Erro interno durante a alteração do cirurgia");
		}
	}

	private boolean verificaCirurgiaExistente(CirurgiaId id) throws Exception {
		if (!cirurgiaService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}