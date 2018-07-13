package br.hela.convenio;

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
import br.hela.convenio.ConvenioId;
import br.hela.convenio.ConvenioService;
import br.hela.convenio.comandos.BuscarConvenio;
import br.hela.convenio.comandos.CriarConvenio;
import br.hela.convenio.comandos.EditarConvenio;
import br.hela.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Convênios Controller")
@Controller
@RequestMapping("/convenios")
@CrossOrigin
public class ConvenioController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private ConvenioService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque todos os convênios")
	@GetMapping
	public ResponseEntity<List<BuscarConvenio>> getConvenio() {
		Optional<List<BuscarConvenio>> optionalConvenios = service.encontrar();
		if (optionalConvenios.isPresent()) {
			return ResponseEntity.ok(optionalConvenios.get());
		}
		throw new NullPointerException("Não existe nenhum convênio cadastrado no banco de dados");
	}

	@ApiOperation("Busque um convênio pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarConvenio> getConvenioPorId(@PathVariable ConvenioId id) {
		Optional<BuscarConvenio> optionalConvenio = service.encontrar(id);
		if (optionalConvenio.isPresent()) {
			return ResponseEntity.ok(optionalConvenio.get());
		}
		throw new NullPointerException("O convênio procurado não existe no banco de dados");
	}

	@ApiOperation("Delete um convênio pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletarConvenio(@PathVariable ConvenioId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			if (service.encontrar(id).isPresent()) {
				Optional<String> optionalConvenio = service.deletar(id);
				return ResponseEntity.ok(optionalConvenio.get());
			}
			throw new NullPointerException("O convênio a deletar não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation(value = "Cadastre um novo convênio")
	@PostMapping
	public ResponseEntity<String> postConvenio(@RequestBody CriarConvenio comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			Optional<ConvenioId> optionalConvenioId = service.salvar(comando);
			if (optionalConvenioId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalConvenioId.get()).toUri();
				return ResponseEntity.created(location).body("O convênio foi cadastrado com sucesso");
			}
			throw new SQLException("O convênio não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation(value = "Altere um convênio")
	@PutMapping
	public ResponseEntity<String> putConvenio(@RequestBody EditarConvenio comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			if (!service.encontrar(comando.getId()).isPresent()) {
				throw new NullPointerException("O convênio a ser alterado não existe no banco de dados");
			}
			Optional<ConvenioId> optionalConvenioId = service.alterar(comando);
			if (optionalConvenioId.isPresent()) {
				return ResponseEntity.ok().body("O convênio foi alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do convênio");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

}
