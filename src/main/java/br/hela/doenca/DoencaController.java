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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.hela.doenca.comandos.CriarDoenca;

@RestController
@RequestMapping("/doencas")
public class DoencaController {
	@Autowired
	private DoencaService doencaService;
	
	@GetMapping
	public ResponseEntity<List<Doenca>> getDoencas(){
		List<Doenca> optionalDoencas = doencaService.encontrar();
		if(!optionalDoencas.isEmpty()) {
			return ResponseEntity.ok(optionalDoencas);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Doenca> getDoencaPorId(@PathVariable DoencaId id){
		Optional<Doenca> optionalDoenca = doencaService.encontrar(id);
		if(optionalDoenca.isPresent()) {
			return ResponseEntity.ok(optionalDoenca.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletarDoenca (@PathVariable DoencaId id) throws SQLException{
		Optional<Doenca> optionalDoenca = doencaService.encontrar(id);
		if(optionalDoenca.isPresent()) {
			doencaService.deletar(id);
			return ResponseEntity.accepted().body("EXCUIDO COM SUCESSO");
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping
	public ResponseEntity<String> postDoenca(@RequestBody CriarDoenca comando) throws SQLException{
		Optional<DoencaId> optionalDoencaId = doencaService.executar(comando);
		if(optionalDoencaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalDoencaId.get()).toUri();
			return ResponseEntity.created(location).body("DOENCA CADASTRADA COM SUCESSO");
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> putDoenca(@RequestBody Doenca comando) throws SQLException{
		Optional<DoencaId> optionalDoencaId = doencaService.alterar(comando);
		if(optionalDoencaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalDoencaId.get()).toUri();
			return ResponseEntity.created(location).body("ALTERACAO FEITA COM SUCESSO");
		}
		return ResponseEntity.badRequest().build();
	}

}
