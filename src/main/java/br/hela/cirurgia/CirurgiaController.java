package br.hela.cirurgia;

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

import br.hela.cirurgia.comandos.CriarCirurgia;

@RestController
@RequestMapping("/cirurgias")
public class CirurgiaController {
	@Autowired
	private CirurgiaService service;

	@GetMapping
	public ResponseEntity<List<Cirurgia>> get(){
		List<Cirurgia> optionalCirurgia = service.encontrar();
		if(!optionalCirurgia.isEmpty()) {
			return ResponseEntity.ok(optionalCirurgia);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cirurgia> getCirurgia(@PathVariable CirurgiaId id) {
		Optional<Cirurgia> optionalCirurgia = service.encontrar(id);
		if (optionalCirurgia.isPresent()) {
			return ResponseEntity.ok(optionalCirurgia.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<CirurgiaId> deletarCirurgia(@PathVariable CirurgiaId id) throws SQLException {
		Optional<Cirurgia> optionalCirurgia = service.encontrar(id);
		if (optionalCirurgia.isPresent()) {
			service.deletar(id);
			return ResponseEntity.accepted().build();
		}
		return ResponseEntity.badRequest().build();
	}

	@PostMapping
	public ResponseEntity<CirurgiaId> postCirurgia(@RequestBody CriarCirurgia comando) throws SQLException {
		Optional<CirurgiaId> optionalCirurgiaId = service.executar(comando);
		if (optionalCirurgiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalCirurgiaId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CirurgiaId> putCirurgia(@RequestBody Cirurgia comando) throws SQLException {
		Optional<CirurgiaId> optionalCirurgiaId = service.alterar(comando);
		if (optionalCirurgiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalCirurgiaId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		return ResponseEntity.badRequest().build();
	}
}
