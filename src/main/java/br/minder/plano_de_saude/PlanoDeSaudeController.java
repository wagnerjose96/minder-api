package br.minder.plano_de_saude;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.minder.plano_de_saude.PlanoDeSaudeId;
import br.minder.plano_de_saude.PlanoDeSaudeService;
import br.minder.plano_de_saude.comandos.BuscarPlanoDeSaude;
import br.minder.plano_de_saude.comandos.CriarPlanoDeSaude;
import br.minder.plano_de_saude.comandos.EditarPlanoDeSaude;
import br.minder.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Plano De Saúde Controller")
@Controller
@RequestMapping("/planos")
@CrossOrigin
public class PlanoDeSaudeController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private PlanoDeSaudeService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque todos os planos de saúde")
	@GetMapping
	public ResponseEntity<List<BuscarPlanoDeSaude>> getPlanoDeSaudes(@RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<List<BuscarPlanoDeSaude>> optionalPlanoDeSaude = service.encontrar(autentica.idUser(token));
			if (optionalPlanoDeSaude.isPresent()) {
				return ResponseEntity.ok(optionalPlanoDeSaude.get());
			}
			throw new NullPointerException("Não existe nenhum plano de saúde cadastrado no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Busque um plano de saúde pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarPlanoDeSaude> getPlanoDeSaudePorId(@PathVariable PlanoDeSaudeId id,
			@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarPlanoDeSaude> optionalPlanoDeSaude = service.encontrar(id, autentica.idUser(token));
			if (optionalPlanoDeSaude.isPresent()) {
				return ResponseEntity.ok(optionalPlanoDeSaude.get());
			}
			throw new NullPointerException("O plano de saúde procurado não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Cadastre um novo plano de saúde")
	@PostMapping
	public ResponseEntity<String> postPlanoDeSaude(@RequestBody CriarPlanoDeSaude comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<PlanoDeSaudeId> optionalPlanoDeSaudeId = service.salvar(comando, autentica.idUser(token));
			if (optionalPlanoDeSaudeId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalPlanoDeSaudeId.get()).toUri();
				return ResponseEntity.created(location).body("O plano de saúde foi cadastrado com sucesso");
			}
			throw new SQLException("O plano de saúde não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Altere um plano de saúde")
	@PutMapping
	public ResponseEntity<String> putPlanoDeSaude(@RequestBody EditarPlanoDeSaude comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicao(token)) {
			if (!service.encontrar(comando.getId(), autentica.idUser(token)).isPresent()) {
				throw new NullPointerException("O plano de saúde a ser alterado não existe no banco de dados");
			}

			Optional<PlanoDeSaudeId> optionalPlanoId = service.alterar(comando);
			if (optionalPlanoId.isPresent()) {
				return ResponseEntity.ok().body("O plano de saúde foi alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do plano de saúde");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Delete um plano de saúde pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePlanoDeSaude(@PathVariable PlanoDeSaudeId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!service.encontrar(id, autentica.idUser(token)).isPresent()) {
				throw new NullPointerException("O plano de saúde a ser deletado não existe no banco de dados");
			}
			Optional<String> resultado = service.deletar(id);
			if (resultado.isPresent())
				return ResponseEntity.ok(resultado.get());
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}
}
