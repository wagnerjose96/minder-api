package br.hela.alergia;

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
import br.hela.alergia.comandos.CriarAlergia;
import br.hela.alergia.comandos.EditarAlergia;

@RestController
@RequestMapping("/alergias")
public class AlergiaController {
	@Autowired
	private AlergiaService alergiaService;

	@GetMapping
	public ResponseEntity<List<Alergia>> getAlergias(){
		Optional<List<Alergia>> optionalAlergias = alergiaService.encontrar();
		return ResponseEntity.ok(optionalAlergias.get());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Alergia> getAlergiaPorId(@PathVariable AlergiaId id)
	throws NullPointerException {
		
		Optional<Alergia> optionalAlergia = alergiaService.encontrar(id);
		if (verificaAlergiaExistente(id)) {
			return ResponseEntity.ok(optionalAlergia.get());
		}
		throw new NullPointerException("A alergia procurada não existe no banco de dados");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarAlergia(@PathVariable AlergiaId id) throws NullPointerException {
		
		if(verificaAlergiaExistente(id)) {					
			Optional<String> resultado = alergiaService.deletar(id);
			return ResponseEntity.ok(resultado);
		}
		throw new NullPointerException("A alergia a deletar não existe no banco de dados");
	}

	@PostMapping
	public ResponseEntity<String> postAlergia(@RequestBody CriarAlergia comando) throws Exception {
	
		Optional<AlergiaId> optionalAlergiaId = alergiaService.salvar(comando);
		if (optionalAlergiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalAlergiaId.get()).toUri();
			return ResponseEntity.created(location).body("Alergia cadastrada com sucesso");
		}
		throw new Exception("A alergia não foi salva devido a um erro interno");
	}

	@PutMapping
	public ResponseEntity<String> putAlergia(@RequestBody EditarAlergia comando) throws SQLException, NullPointerException {
		
		if(!verificaAlergiaExistente(comando.getIdAlergia())) {
			throw new NullPointerException("A alergia a ser alterada não existe no banco de dados");
		}
		
		Optional<AlergiaId> optionalAlergiaId = alergiaService.alterar(comando);
		if (optionalAlergiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalAlergiaId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		else {
			throw new SQLException("Erro interno durante a alteração do alergia");
		}
		
	}

	private boolean verificaAlergiaExistente(AlergiaId id) {
		if (!alergiaService.encontrar(id).isPresent()) {
			return false;
		}
		else {
			return true;
		}
	}

}