package br.hela.cirurgia;

import java.net.URI;
import java.sql.SQLException;
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
	public ResponseEntity<List<Cirurgia>> getCirurgias() {
		
		Optional<List<Cirurgia>> optionalCirurgias = cirurgiaService.encontrar();
		return ResponseEntity.ok(optionalCirurgias.get());
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cirurgia> getCirurgiaPorId(@PathVariable CirurgiaId id)
			throws NullPointerException {
		
		Optional<Cirurgia> optionalCirurgia = cirurgiaService.encontrar(id);
		if (verificaCirurgiaExistente(id)) {
			return ResponseEntity.ok(optionalCirurgia.get());
		}
		throw new NullPointerException("A cirurgia procurada não existe no banco de dados");			
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletarCirurgia(@PathVariable CirurgiaId id) throws TimeoutException, NullPointerException,  BadHttpRequest {
		
		
		if (verificaCirurgiaExistente(id)) {
			Optional<String> optionalCirurgia = cirurgiaService.deletar(id);
			return ResponseEntity.ok(optionalCirurgia.get());
		}
		throw new NullPointerException("O usuário a deletar não existe no banco de dados");
	}

	@PostMapping
	public ResponseEntity<String> postCirurgia(@RequestBody CriarCirurgia comando)
			throws Exception {
		
		Optional<CirurgiaId> optionalCirurgiaId = cirurgiaService.executar(comando);
		if (optionalCirurgiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalCirurgiaId.get()).toUri();
			return ResponseEntity.created(location).body("Usuário criado com sucesso");
		}
		throw new Exception("A cirurgia não foi salva devido a um erro interno");
	}

	@PutMapping
	public ResponseEntity<String> putCirurgia(@RequestBody Cirurgia comando) throws NullPointerException, SQLException {
		
		if(!verificaCirurgiaExistente(comando.getIdCirurgia())) {
			throw new NullPointerException("O usuário a ser alterado não existe no banco de dados");
		}		
		Optional<CirurgiaId> optionalCirurgiaId = cirurgiaService.alterar(comando);
		if (optionalCirurgiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalCirurgiaId.get()).toUri();
			return ResponseEntity.created(location).body("Cirurgia alterada com sucesso");
		}
		else {
			throw new SQLException("Erro interno durante a alteração do cirurgia");
		}
		
	}

	private boolean verificaCirurgiaExistente(CirurgiaId id) throws NullPointerException {
		if (!cirurgiaService.encontrar(id).isPresent()) {
			return false;
		}
		else {
			return true;
		}
	}

}