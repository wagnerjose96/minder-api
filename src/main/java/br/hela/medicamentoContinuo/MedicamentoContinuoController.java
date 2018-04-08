package br.hela.medicamentoContinuo;

import java.net.URI;
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

import br.hela.medicamentoContinuo.comandos.CriarMedicamentoContinuo;

@RestController
@RequestMapping("/medicamentoContinuo")
public class MedicamentoContinuoController {

	@Autowired
	private MedicamentoContinuoService service;
	
	@GetMapping
	public ResponseEntity<List<MedicamentoContinuo>> getMedicamentoContinuo(){
		
		Optional<List<MedicamentoContinuo>> optionalMedicamentosContinuos = service.encontrar();	
		return ResponseEntity.ok(optionalMedicamentosContinuos.get());
	
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<MedicamentoContinuo> getMedicamentoContinuo(@PathVariable MedicamentoContinuoId id) throws NullPointerException{
				
		Optional<MedicamentoContinuo> optionalMedicamentoContinuo = service.encontrar(id);
		if(verificaMedicamentoContinuoExistente(id)) {
			return ResponseEntity.ok(optionalMedicamentoContinuo.get());
		}	
		throw new NullPointerException("O alarme procurado não existe no banco de dados");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarMedicamentoContinuo(@PathVariable MedicamentoContinuoId id) throws NullPointerException{ 
				
		if(verificaMedicamentoContinuoExistente(id)) {
		Optional<String> resultado = service.deletar(id);
			return ResponseEntity.ok(resultado);
		}	
		throw new NullPointerException("O alarme continuo a deletar não existe no banco de dados");
	}
	
	@PostMapping
	public ResponseEntity<String> postMedicamentoContinuo(@RequestBody CriarMedicamentoContinuo comando)throws Exception{
		
		Optional<MedicamentoContinuoId> optionalMedicamentoContinuoId = service.salvar(comando);		
		if (optionalMedicamentoContinuoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalMedicamentoContinuoId.get()).toUri();
			return ResponseEntity.created(location).body("Alarme criado com sucesso");
		}
		throw new Exception("O alarme não foi salvo devido a algum erro interno");
	}
	
	@PutMapping
	public ResponseEntity<String> putMedicamentoContinuo(@RequestBody MedicamentoContinuo comando) throws NullPointerException, InternalError
	{
		
		if(!verificaMedicamentoContinuoExistente(comando.getIdMedicamentoContinuo())){
			throw new NullPointerException("O alarme a ser alterado não existe");
		}
		Optional<MedicamentoContinuoId> optionalMedicamentoContinuoId = service.alterar(comando);
		if (optionalMedicamentoContinuoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalMedicamentoContinuoId.get()).toUri();
			return ResponseEntity.created(location).body("Alarme alterado com sucesso");
		}
		else {
			throw new InternalError("Erro interno durante a alteração do alarme");
		}
		
	}
	
	
	private boolean verificaMedicamentoContinuoExistente(MedicamentoContinuoId id) throws NullPointerException{
		if (!service.encontrar(id).isPresent()) {
			return false;
		}
		else {
			return true;
		}
	}

}	

	


