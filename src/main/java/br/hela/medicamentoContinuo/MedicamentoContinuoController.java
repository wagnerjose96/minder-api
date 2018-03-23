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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.hela.medicamentoContinuo.comandos.CriarMedicamentoContinuo;
import javassist.tools.web.BadHttpRequest;

@RestController
@RequestMapping("/medicamentoContinuo")
public class MedicamentoContinuoController {

	@Autowired
	private MedicamentoContinuoService service;
	
	@GetMapping
	public ResponseEntity<List<MedicamentoContinuo>> getMedicamentoContinuo()
			throws SQLException, NullPointerException, BadHttpRequest {
		
		verificaListaDeMedicamentosContinuos();
		verificaRetornoSQL();
		Optional<List<MedicamentoContinuo>> optionalMedicamentosContinuos = service.encontrar();
		if(optionalMedicamentosContinuos.isPresent()) {
			return ResponseEntity.ok(optionalMedicamentosContinuos.get());
		}	
			throw new BadHttpRequest();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<MedicamentoContinuo> getMedicamentoContinuo(@PathVariable MedicamentoContinuoId id)
			throws SQLException, NullPointerException, BadHttpRequest {
		
		verificaMedicamentoContinuoExistente(id);
		
		Optional<MedicamentoContinuo> optionalMedicamentoContinuo = service.encontrar(id);
		if(optionalMedicamentoContinuo.isPresent()) {
			return ResponseEntity.ok(optionalMedicamentoContinuo.get());
		}	
			throw new BadHttpRequest();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarMedicamentoContinuo(@PathVariable MedicamentoContinuoId id) 
			throws SQLException, NullPointerException, BadHttpRequest { 
		
		verificaListaDeMedicamentosContinuos();
		verificaRetornoSQL();
		
		Optional<String> optionalMedicamentoContinuo = service.deletar(id);
		if (optionalMedicamentoContinuo.isPresent()) {
			return ResponseEntity.ok(optionalMedicamentoContinuo);
		}	
		throw new BadHttpRequest();
	}
	
	@PostMapping
	public ResponseEntity<MedicamentoContinuoId> postMedicamentoContinuo(@RequestBody CriarMedicamentoContinuo comando)
		throws SQLException, NullPointerException, BadHttpRequest {
		
		verificaRetornoSQL();
		Optional<MedicamentoContinuoId> optionalMedicamentoContinuoId = service.salvar(comando);
		verificaMedicamentoContinuoExistente(optionalMedicamentoContinuoId.get());
		
		
		if (optionalMedicamentoContinuoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalMedicamentoContinuoId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		throw new BadHttpRequest();
	}
	
	@PutMapping
	public ResponseEntity<MedicamentoContinuoId> putMedicamentoContinuo(@RequestBody MedicamentoContinuo comando)
		throws SQLException, NullPointerException, BadHttpRequest{
		
		verificaMedicamentoContinuoExistente(comando.getIdMedicamentoContinuo());
		verificaRetornoSQL();
		
		Optional<MedicamentoContinuoId> optionalMedicamentoContinuoId = service.alterar(comando);
		if (optionalMedicamentoContinuoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalMedicamentoContinuoId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		throw new BadHttpRequest();
	}
	
	private void verificaRetornoSQL() throws SQLException{
		if (System.currentTimeMillis() == 10) {
			throw new SQLException("Servidor SQL sem resposta!");
		}
	}
	
	private void verificaMedicamentoContinuoExistente(MedicamentoContinuoId id) throws NullPointerException{
		if (!service.encontrar(id).isPresent()) {
			throw new NullPointerException("Medicamento continuo não encontrado!");
		}
	}
	
	private void verificaListaDeMedicamentosContinuos() throws NullPointerException {
		if (!service.encontrar().isPresent()) {
			throw new NullPointerException("Nenhum medicamento continuo cadastrado!");
		}
	}
}	

	


