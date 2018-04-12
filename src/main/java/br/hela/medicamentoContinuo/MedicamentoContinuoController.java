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
import br.hela.medicamentoContinuo.comandos.EditarMedicamentoContinuo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Medicamento Contínuo Controller")
@RestController
@RequestMapping("/medicamentoContinuo")
public class MedicamentoContinuoController {

	@Autowired
	private MedicamentoContinuoService service;

	@ApiOperation(value = "Busque todos os medicamentos contínuos")
	@GetMapping
	public ResponseEntity<List<MedicamentoContinuo>> getMedicamentoContinuo() {

		Optional<List<MedicamentoContinuo>> optionalMedicamentosContinuos = service.encontrar();
		return ResponseEntity.ok(optionalMedicamentosContinuos.get());
	}

	@ApiOperation(value = "Busque um medicamento contínuo pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<MedicamentoContinuo> getMedicamentoContinuo(@PathVariable MedicamentoContinuoId id)
			throws NullPointerException {

		Optional<MedicamentoContinuo> optionalMedicamentoContinuo = service.encontrar(id);
		if (verificaMedicamentoContinuoExistente(id)) {
			return ResponseEntity.ok(optionalMedicamentoContinuo.get());
		}
		throw new NullPointerException("O mediacmento contínuo procurado não existe no banco de dados");
	}

	@ApiOperation(value = "Delete um medicamento contínuo pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarMedicamentoContinuo(@PathVariable MedicamentoContinuoId id)
			throws NullPointerException {

		if (verificaMedicamentoContinuoExistente(id)) {
			Optional<String> resultado = service.deletar(id);
			return ResponseEntity.ok(resultado);
		}
		throw new NullPointerException("O mediamento contínuo a deletar não existe no banco de dados");
	}

	@ApiOperation(value = "Cadastre um novo medicamento contínuo")
	@PostMapping

	public ResponseEntity<String> postMedicamentoContinuo(@RequestBody CriarMedicamentoContinuo comando)
			throws Exception {

		Optional<MedicamentoContinuoId> optionalMedicamentoContinuoId = service.salvar(comando);
		if (optionalMedicamentoContinuoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalMedicamentoContinuoId.get()).toUri();
			return ResponseEntity.created(location).body("Medicamento contínuo criado com sucesso");
		}
		throw new Exception("O medicamento contínuo não foi salvo devido a algum erro interno");
	}

	@ApiOperation(value = "Altere um medicamento contínuo")
	@PutMapping
	public ResponseEntity<String> putMedicamentoContinuo(@RequestBody EditarMedicamentoContinuo comando)
			throws NullPointerException, InternalError {

		if (!verificaMedicamentoContinuoExistente(comando.getIdMedicamentoContinuo())) {
			throw new NullPointerException("O medicamento contínuo a ser alterado não existe");
		}
		Optional<MedicamentoContinuoId> optionalMedicamentoContinuoId = service.alterar(comando);
		if (optionalMedicamentoContinuoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalMedicamentoContinuoId.get()).toUri();
			return ResponseEntity.created(location).body("Medicamento alterado com sucesso");
		} else {
			throw new InternalError("Erro interno durante a alteração do medica,ento contínuo");
		}

	}

	private boolean verificaMedicamentoContinuoExistente(MedicamentoContinuoId id) throws NullPointerException {
		if (!service.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}
