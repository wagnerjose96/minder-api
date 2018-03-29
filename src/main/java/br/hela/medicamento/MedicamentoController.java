package br.hela.medicamento;

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

import br.hela.medicamento.comandos.CriarMedicamento;
import javassist.tools.web.BadHttpRequest;


@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {
	
	@Autowired
	private MedicamentoService service;
	
	@GetMapping
	public ResponseEntity<List<Medicamento>> getMedicamento()
			throws SQLException, NullPointerException, BadHttpRequest {
		
		verificaListaDeMedicamentos();
		verificaRetornoSQL();
		Optional<List<Medicamento>> optionalMedicamentos = service.encontrar();
		if(optionalMedicamentos.isPresent()) {
			return ResponseEntity.ok(optionalMedicamentos.get());
		}	
			throw new BadHttpRequest();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Medicamento> getMedicamentoPorId(@PathVariable MedicamentoId id)
			throws SQLException, NullPointerException, BadHttpRequest {
		
		verificaMedicamentoExistente(id);
		
		Optional<Medicamento> optionalMedicamento = service.encontrar(id);
		if(optionalMedicamento.isPresent()) {
			return ResponseEntity.ok(optionalMedicamento.get());
		}	
			throw new BadHttpRequest();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarMedicamento(@PathVariable MedicamentoId id) 
			throws SQLException, NullPointerException, BadHttpRequest { 
		
		verificaListaDeMedicamentos();
		verificaRetornoSQL();
		
		Optional<String> optionalMedicamento = service.deletar(id);
		if (optionalMedicamento.isPresent()) {
			return ResponseEntity.ok(optionalMedicamento);
		}	
		throw new BadHttpRequest();
	}
	
	@PostMapping
	public ResponseEntity<MedicamentoId> postMedicamento(@RequestBody CriarMedicamento comando)
		throws SQLException, NullPointerException, BadHttpRequest {
		
		verificaRetornoSQL();
		Optional<MedicamentoId> optionalMedicamentoId = service.salvar(comando);
		verificaMedicamentoExistente(optionalMedicamentoId.get());
		
		
		if (optionalMedicamentoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalMedicamentoId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		throw new BadHttpRequest();
	}
	
	@PutMapping
	public ResponseEntity<MedicamentoId> putMedicamentoContinuo(@RequestBody Medicamento comando)
		throws SQLException, NullPointerException, BadHttpRequest{
		
		verificaMedicamentoExistente(comando.getIdMedicamento());
		verificaRetornoSQL();
		
		Optional<MedicamentoId> optionalMedicamentoId = service.alterar(comando);
		if (optionalMedicamentoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalMedicamentoId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		throw new BadHttpRequest();
	}
	
	private void verificaRetornoSQL() throws SQLException{
		if (System.currentTimeMillis() == 10) {
			throw new SQLException("Servidor SQL sem resposta!");
		}
	}
	
	private void verificaMedicamentoExistente(MedicamentoId id) throws NullPointerException{
		if (!service.encontrar(id).isPresent()) {
			throw new NullPointerException("Medicamento não encontrado!");
		}
	}
	
	private void verificaListaDeMedicamentos() throws NullPointerException {
		if (!service.encontrar().isPresent()) {
			throw new NullPointerException("Nenhum medicamento cadastrado!");
		}
	}

}
