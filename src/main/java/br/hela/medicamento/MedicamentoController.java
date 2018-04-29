package br.hela.medicamento;

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
import br.hela.medicamento.comandos.CriarMedicamento;
import br.hela.medicamento.comandos.EditarMedicamento;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Medicamento Controller")
@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {

	@Autowired
	private MedicamentoService service;

	@ApiOperation(value = "Busque todos os medicamentos")
	@GetMapping
	public ResponseEntity<List<Medicamento>> getMedicamento() {
		Optional<List<Medicamento>> optionalMedicamentos = service.encontrar();
		return ResponseEntity.ok(optionalMedicamentos.get());
	}

	@ApiOperation(value = "Busque um medicamento pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<Medicamento> getMedicamentoPorId(@PathVariable MedicamentoId id) throws NullPointerException {

		if (verificaMedicamentoExistente(id)) {
			Optional<Medicamento> optionalMedicamento = service.encontrar(id);
			return ResponseEntity.ok(optionalMedicamento.get());
		}
		throw new NullPointerException("O medicamento procurado não existe no banco de dados");
	}

	@ApiOperation(value = "Delete um medicamento pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarMedicamento(@PathVariable MedicamentoId id)
			throws NullPointerException {

		if(verificaMedicamentoExistente(id)) {
			Optional<String> optionalMedicamento = service.deletar(id);
			return ResponseEntity.ok(optionalMedicamento);
		}
		throw new NullPointerException("O medicamento a deletar não existe no banco de dados");
	}

	@ApiOperation(value = "Cadastre um novo medicamento")
	@PostMapping
	public ResponseEntity<String> postMedicamento(@RequestBody CriarMedicamento comando)
			throws Exception {

		Optional<MedicamentoId> optionalMedicamentoId = service.salvar(comando);
		if (optionalMedicamentoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalMedicamentoId.get()).toUri();
			return ResponseEntity.created(location).body("O medicamento foi criado com sucesso");
		}
		throw new Exception("O medicamento não foi salvo devido a um erro interno");
	}

	@ApiOperation(value = "Altere um medicamento")
	@PutMapping
	public ResponseEntity<String> putMedicamentoContinuo(@RequestBody EditarMedicamento comando)
			throws NullPointerException, InternalError {

		if(!verificaMedicamentoExistente(comando.getIdMedicamento())) {
			throw new NullPointerException("O medicamento a ser alterado não existe no banco de dados");
		}
		Optional<MedicamentoId> optionalMedicamentoId = service.alterar(comando);
		if (optionalMedicamentoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalMedicamentoId.get()).toUri();
			return ResponseEntity.created(location).body("Medicamento alterado com sucesso");
		} else {
			throw new InternalError("Erro interno durante a alteração do usuário");
		}
	}

	private boolean verificaMedicamentoExistente(MedicamentoId id) {
		if (!service.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}