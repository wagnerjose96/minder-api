package br.hela.endereco;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.hela.endereco.comandos.BuscarEndereco;
import br.hela.endereco.comandos.CriarEndereco;
import br.hela.endereco.comandos.EditarEndereco;
import br.hela.security.AutenticaRequisicao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Endereço Controller")
@RestController
@RequestMapping("/enderecos")
@CrossOrigin
public class EnderecoController {
	@Autowired
	private EnderecoService enderecoService;

	@Autowired
	private AutenticaRequisicao autentica;

	@ApiOperation("Busque todos os endereços")
	@GetMapping
	public ResponseEntity<Optional<List<BuscarEndereco>>> getenderecos() throws Exception, SQLException {
		Optional<List<BuscarEndereco>> optionalenderecos = enderecoService.encontrar();
		return ResponseEntity.ok(optionalenderecos);
	}

	@ApiOperation("Busque o endereço pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarEndereco> getenderecoPorId(@PathVariable EnderecoId id)
			throws NullPointerException, Exception, SQLException {
		Optional<BuscarEndereco> optionalendereco = enderecoService.encontrar(id);
		if (verificaEnderecoExistente(id)) {
			return ResponseEntity.ok(optionalendereco.get());
		}
		throw new NullPointerException("O endereço procurado não existe no banco de dados");
	}

	@ApiOperation("Cadastre um novo endereço")
	@PostMapping
	public ResponseEntity<String> postendereco(@RequestBody CriarEndereco comando, @RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<EnderecoId> optionalenderecoId = enderecoService.salvar(comando);
			if (optionalenderecoId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalenderecoId.get()).toUri();
				return ResponseEntity.created(location).body("O endereço foi cadastrado com sucesso");
			}
			throw new Exception("O endereço não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation("Altere um endereço")
	@PutMapping
	public ResponseEntity<String> putendereco(@RequestBody EditarEndereco comando, @RequestHeader String token)
			throws SQLException, NullPointerException, Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaEnderecoExistente(comando.getId())) {
				throw new NullPointerException("O endereço a ser alterado não existe no banco de dados");
			}

			Optional<EnderecoId> optionalenderecoId = enderecoService.alterar(comando);
			if (optionalenderecoId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalenderecoId.get()).toUri();
				return ResponseEntity.created(location).body("O endereço foi alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do endereço");
			}
		}
		throw new AccessDeniedException("Acesso negado");
	}

	private boolean verificaEnderecoExistente(EnderecoId id) throws Exception {
		if (!enderecoService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}