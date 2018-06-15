package br.hela.planoDeSaude;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.hela.planoDeSaude.PlanoDeSaudeId;
import br.hela.planoDeSaude.PlanoDeSaudeService;
import br.hela.planoDeSaude.comandos.BuscarPlanoDeSaude;
import br.hela.planoDeSaude.comandos.CriarPlanoDeSaude;
import br.hela.planoDeSaude.comandos.EditarPlanoDeSaude;
import br.hela.security.AutenticaRequisicao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Plano De Saúde Controller")
@Controller
@RequestMapping("/planos")
public class PlanoDeSaudeController {
	@Autowired
	private PlanoDeSaudeService service;

	@Autowired
	private AutenticaRequisicao autentica;

	@ApiOperation("Busque todos os planos de saúde")
	@GetMapping
	public ResponseEntity<List<BuscarPlanoDeSaude>> getPlanoDeSaudes() throws Exception, SQLException {
		Optional<List<BuscarPlanoDeSaude>> optionalPlanoDeSaude = service.encontrar();
		return ResponseEntity.ok(optionalPlanoDeSaude.get());
	}

	@ApiOperation("Busque o plano de saúde pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarPlanoDeSaude> getPlanoDeSaudePorId(@PathVariable PlanoDeSaudeId id)
			throws NullPointerException, Exception, SQLException {
		Optional<BuscarPlanoDeSaude> optionalPlanoDeSaude = service.encontrar(id);
		if (verificaPlanoDeSaudeExistente(id)) {
			return ResponseEntity.ok(optionalPlanoDeSaude.get());
		}
		throw new NullPointerException("O plano de saúde procurado não existe no banco de dados");
	}

	@ApiOperation("Cadastre um novo plano de saúde")
	@PostMapping
	public ResponseEntity<String> postPlanoDeSaude(@RequestBody CriarPlanoDeSaude comando, @RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<PlanoDeSaudeId> optionalPlanoDeSaudeId = service.salvar(comando);
			if (optionalPlanoDeSaudeId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalPlanoDeSaudeId.get()).toUri();
				return ResponseEntity.created(location).body("O plano de saúde foi cadastrado com sucesso");
			}
			throw new Exception("O plano de saúde não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation("Altere um plano de saúde")
	@PutMapping
	public ResponseEntity<String> putPlanoDeSaude(@RequestBody EditarPlanoDeSaude comando, @RequestHeader String token)
			throws SQLException, NullPointerException, Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaPlanoDeSaudeExistente(comando.getId())) {
				throw new NullPointerException("O plano de saúde a ser alterado não existe no banco de dados");
			}

			Optional<PlanoDeSaudeId> optionalPlanoId = service.alterar(comando);
			if (optionalPlanoId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalPlanoId.get()).toUri();
				return ResponseEntity.created(location).body("O plano de saúde foi alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do plano de saúde");
			}
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation("Delete um plano de saúde pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletePlanoDeSaude(@PathVariable PlanoDeSaudeId id,
			@RequestHeader String token) throws NullPointerException, Exception, AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaPlanoDeSaudeExistente(id)) {
				throw new NullPointerException("O plano de saúde a ser deletado não existe no banco de dados");
			}
			Optional<String> resultado = service.deletar(id);
			return ResponseEntity.ok(resultado);
		}
		throw new AccessDeniedException("Acesso negado");
	}

	private boolean verificaPlanoDeSaudeExistente(PlanoDeSaudeId id) throws Exception {
		if (!service.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}
}
