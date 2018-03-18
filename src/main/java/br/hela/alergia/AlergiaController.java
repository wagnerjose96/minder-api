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
import br.hela.doenca.Doenca;
import br.hela.doenca.DoencaId;


@RestController
@RequestMapping("/alergias")
public class AlergiaController {
	@Autowired
	private AlergiaService alergiaService;
	
	@GetMapping
	public ResponseEntity<List<Alergia>> getAlergias(){
		List<Alergia> optionalAlergias = alergiaService.encontrar();
		if(!optionalAlergias.isEmpty()) {
			return ResponseEntity.ok(optionalAlergias);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Alergia> getAlergiaPorId(@PathVariable AlergiaId id){
		Optional<Alergia> optionalAlergia = alergiaService.encontrar(id);
		if(optionalAlergia.isPresent()) {
			return ResponseEntity.ok(optionalAlergia.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletarAlergia (@PathVariable AlergiaId id) throws SQLException{
		Optional<Alergia> optionalAlergia = alergiaService.encontrar(id);
		if(optionalAlergia.isPresent()) {
			alergiaService.deletar(id);
			return ResponseEntity.accepted().body("ALERGIA EXCLUÍDA COM SUCESSO");
		}
		return ResponseEntity.badRequest().body("A ALERGIA DESEJADA NÃO EXISTE");
	}
	
	@PostMapping
	public ResponseEntity<String> postAlergia(@RequestBody CriarAlergia comando) throws SQLException{
		Optional<AlergiaId> optionalAlergiaId = alergiaService.executar(comando);
		if(optionalAlergiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalAlergiaId.get()).toUri();
			return ResponseEntity.created(location).body("ALERGIA CADASTRADA COM SUCESSO");
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> putAlergia(@RequestBody Alergia comando) throws SQLException{
		Optional<AlergiaId> optionalAlergiaId = alergiaService.alterar(comando);
		if(optionalAlergiaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalAlergiaId.get()).toUri();
			return ResponseEntity.created(location).body("ALERGIA ALTERADA COM SUCESSO");
		}
		return ResponseEntity.badRequest().build();
	}
}