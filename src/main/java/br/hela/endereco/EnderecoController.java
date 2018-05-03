package br.hela.endereco;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.hela.endereco.comandos.CriarEndereco;
import br.hela.endereco.comandos.EditarEndereco;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Endereco Controller")
@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
	
	@Autowired
	private EnderecoService enderecoService;

	@ApiOperation(value = "Busque todos os enderecos")
	@GetMapping
	public ResponseEntity<Optional<List<Endereco>>> getenderecos() throws Exception {
		Optional<List<Endereco>> optionalenderecos = enderecoService.encontrar();
		return ResponseEntity.ok(optionalenderecos);
	}

	@ApiOperation(value = "Busque o endereco pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<Endereco> getenderecoPorId(@PathVariable EnderecoId id) 
			throws NullPointerException, Exception {

		Optional<Endereco> optionalendereco = enderecoService.encontrar(id);
		if (verificaEnderecoExistente(id)) {
			return ResponseEntity.ok(optionalendereco.get());
		}
		throw new NullPointerException("O endereco procurado não existe no banco de dados");
	}

	@ApiOperation(value = "Cadastre um novo endereco")
	@PostMapping
	public ResponseEntity<String> postendereco(@RequestBody CriarEndereco comando) throws Exception {

		Optional<EnderecoId> optionalenderecoId = enderecoService.salvar(comando);
		if (optionalenderecoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalenderecoId.get()).toUri();
			return ResponseEntity.created(location).body("endereco cadastrado com sucesso");
		}
		throw new Exception("O endereco não foi salvo devido a um erro interno");
	}

	@ApiOperation(value = "Altere um endereco")
	@PutMapping
	public ResponseEntity<String> putendereco(@RequestBody EditarEndereco comando)
			throws SQLException, NullPointerException, Exception {

		if (!verificaEnderecoExistente(comando.getId())) {
			throw new NullPointerException("O endereco a ser alterado não existe no banco de dados");
		}

		Optional<EnderecoId> optionalenderecoId = enderecoService.alterar(comando);
		if (optionalenderecoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalenderecoId.get()).toUri();
			return ResponseEntity.created(location).body("endereco alterado com sucesso");
		} else {
			throw new SQLException("Erro interno durante a alteração do endereco");
		}

	}

	private boolean verificaEnderecoExistente(EnderecoId id) throws Exception{
		if (!enderecoService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}