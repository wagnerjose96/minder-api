package br.unicesumar.cor;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.unicesumar.cor.comandos.CriarCor;

@RestController
@RequestMapping("/cores")
public class CorController {
	@Autowired
	private CorService service;

	@GetMapping
	public ResponseEntity<List<Cor>> get(){
		List<Cor> optionalCores = service.encontrar();
		if(!optionalCores.isEmpty()) {
			return ResponseEntity.ok(optionalCores);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cor> get(@PathVariable CorId id) {
		Optional<Cor> optionalCor = service.encontrar(id);
		if (optionalCor.isPresent()) {
			return ResponseEntity.ok(optionalCor.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<CorId> deletar(@PathVariable CorId id) throws SQLException {
		Optional<Cor> optionalCor = service.encontrar(id);
		if (optionalCor.isPresent()) {
			service.deletar(id);
			return ResponseEntity.accepted().build();
		}
		return ResponseEntity.badRequest().build();
	}

	@PostMapping
	public ResponseEntity<CorId> post(@RequestBody CriarCor comando) throws SQLException {
		Optional<CorId> optionalCorId = service.executar(comando);
		if (optionalCorId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalCorId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		return ResponseEntity.badRequest().build();
	}

}
