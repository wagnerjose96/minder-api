package br.hela.medicamento;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.hela.medicamento.comandos.BuscarMedicamento;
import br.hela.medicamento.comandos.CriarMedicamento;
import br.hela.medicamento.comandos.EditarMedicamento;
import br.hela.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Medicamento Controller")
@RestController
@RequestMapping("/medicamentos")
@CrossOrigin
public class MedicamentoController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private MedicamentoService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque todos os medicamentos")
	@GetMapping
	public ResponseEntity<List<BuscarMedicamento>> getMedicamento() {
		Optional<List<BuscarMedicamento>> optionalMedicamentos = service.encontrar();
		if (optionalMedicamentos.isPresent()) {
			return ResponseEntity.ok(optionalMedicamentos.get());
		}
		throw new NullPointerException("Não existe nenhum medicamento cadastrado no banco de dados");
	}

	@ApiOperation("Busque um medicamento pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarMedicamento> getMedicamentoPorId(@PathVariable MedicamentoId id) {
		Optional<BuscarMedicamento> optionalMedicamento = service.encontrar(id);
		if (optionalMedicamento.isPresent()) {
			return ResponseEntity.ok(optionalMedicamento.get());
		}
		throw new NullPointerException("O medicamento procurado não existe no banco de dados");
	}

	@ApiOperation("Delete um medicamento pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletarMedicamento(@PathVariable MedicamentoId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			if (service.encontrar().isPresent()) {
				Optional<String> optionalMedicamento = service.deletar(id);
				return ResponseEntity.ok(optionalMedicamento.get());
			}
			throw new NullPointerException("O medicamento a deletar não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Cadastre um novo medicamento")
	@PostMapping
	public ResponseEntity<String> postMedicamento(@RequestBody CriarMedicamento comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			Optional<MedicamentoId> optionalMedicamentoId = service.salvar(comando);
			if (optionalMedicamentoId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalMedicamentoId.get()).toUri();
				return ResponseEntity.created(location).body("O medicamento foi cadastrado com sucesso");
			}
			throw new SQLException("O medicamento não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Altere um medicamento")
	@PutMapping
	public ResponseEntity<String> putMedicamento(@RequestBody EditarMedicamento comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			if (!service.encontrar(comando.getIdMedicamento()).isPresent()) {
				throw new NullPointerException("O medicamento a ser alterado não existe no banco de dados");
			}
			Optional<MedicamentoId> optionalMedicamentoId = service.alterar(comando);
			if (optionalMedicamentoId.isPresent()) {
				return ResponseEntity.ok().body("O medicamento foi alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do medicamento");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

}