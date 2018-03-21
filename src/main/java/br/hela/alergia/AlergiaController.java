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

@RestController
@RequestMapping("/alergias")
public class AlergiaController {
	@Autowired
	private AlergiaService alergiaService;

	@GetMapping
	public ResponseEntity<List<Alergia>> getAlergias() throws SQLException, NullPointerException {
		if (System.currentTimeMillis() == 10) {
			throw new SQLException();
		}
		List<Alergia> optionalAlergias = alergiaService.encontrar();
		if (!optionalAlergias.isEmpty()) {
			return ResponseEntity.ok(optionalAlergias);
		}
		throw new NullPointerException("Não existem alergias cadastradas");
	}

	@GetMapping("/{id}")
	public ResponseEntity<Alergia> getAlergiaPorId(@PathVariable AlergiaId id)
			throws SQLException, NullPointerException {
		if (System.currentTimeMillis() == 10) {
			throw new SQLException();
		}
		Optional<Alergia> optionalAlergia = alergiaService.encontrar(id);
		if (optionalAlergia.isPresent()) {
			return ResponseEntity.ok(optionalAlergia.get());
		}
		throw new NullPointerException("Não foi encontrada nenhuma alergia para esse id");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletarAlergia(@PathVariable AlergiaId id) throws SQLException, NullPointerException {
		if (System.currentTimeMillis() == 10) {
			throw new SQLException();
		}
		Optional<Alergia> optionalAlergia = alergiaService.encontrar(id);
		if (optionalAlergia.isPresent()) {
			alergiaService.deletar(id);
			return ResponseEntity.accepted().body("Alergia excluída com sucesso");
		}
		throw new NullPointerException("Não foi encontrada nenhuma alergia para esse id");
	}

	@PostMapping
	public ResponseEntity<String> postAlergia(@RequestBody CriarAlergia comando)
			throws SQLException, NullPointerException {
		if (System.currentTimeMillis() == 10) {
			throw new SQLException();
		}
		Optional<AlergiaId> optionalAlergiaId = alergiaService.executar(comando);
		if (optionalAlergiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalAlergiaId.get()).toUri();
			return ResponseEntity.created(location).body("Alergia cadastrada com sucesso");
		}
		return ResponseEntity.badRequest().build();
	}

	@PutMapping()
	public ResponseEntity<String> putAlergia(@RequestBody Alergia comando) throws SQLException, NullPointerException {
		if (System.currentTimeMillis() == 10) {
			throw new SQLException();
		}
		Optional<AlergiaId> optionalAlergiaId = alergiaService.alterar(comando);
		if (optionalAlergiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalAlergiaId.get()).toUri();
			return ResponseEntity.created(location).body("Alergia alterada com sucesso");
		}
		throw new NullPointerException("Não foi encontrada nenhuma alergia para esse id");
	}
}