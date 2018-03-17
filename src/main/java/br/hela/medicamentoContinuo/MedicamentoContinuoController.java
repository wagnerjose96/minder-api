package br.hela.medicamentoContinuo;

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

import br.hela.medicamentoContinuo.comandos.CriarMedicamentoContinuo;

@RestController
@RequestMapping("/medicamentoContinuo")
public class MedicamentoContinuoController {

	@Autowired
	private MedicamentoContinuoService service;
	
	@GetMapping
	public ResponseEntity<List<MedicamentoContinuo>> getMedicamentoContinuo(){
		List<MedicamentoContinuo> optionalMedicamentosContinuos = service.encontrar();
		if(!optionalMedicamentosContinuos.isEmpty()) {
			return ResponseEntity.ok(optionalMedicamentosContinuos);
		}	
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<MedicamentoContinuo> getMedicamentoContinuo(@PathVariable MedicamentoContinuoId id){
		Optional<MedicamentoContinuo> optionalMedicamentoContinuo = service.encontrar(id);
		if(!optionalMedicamentoContinuo.isPresent()) {
			return ResponseEntity.ok(optionalMedicamentoContinuo.get());
		}	
			return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<MedicamentoContinuoId> deletarMedicamentoContinuo(@PathVariable MedicamentoContinuoId id) throws SQLException {
		Optional<MedicamentoContinuo> optionalMedicamentoContinuo = service.encontrar(id);
		if (optionalMedicamentoContinuo.isPresent()) {
			service.deletar(id);
			return ResponseEntity.accepted().build();
		}	
			return ResponseEntity.badRequest().build();
	}
	
	@PostMapping
	public ResponseEntity<MedicamentoContinuoId> postMedicamentoContinuo(@RequestBody CriarMedicamentoContinuo comando) throws SQLException {
		Optional<MedicamentoContinuoId> optionalMedicamentoContinuoId = service.executar(comando);
		if (optionalMedicamentoContinuoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalMedicamentoContinuoId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		return ResponseEntity.badRequest().build();
	}
	
}	

	


