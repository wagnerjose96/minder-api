package br.hela.endereco;

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
import br.hela.endereco.comandos.CriarEndereco;
import br.hela.endereco.comandos.EditarEndereco;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Endereço Controller")
@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
	
	@Autowired
	private EnderecoService enderecoService;

	@ApiOperation(value = "Busque todos os endereços")
	@GetMapping
	public ResponseEntity<Optional<List<Endereco>>> getenderecos() throws Exception {
		Optional<List<Endereco>> optionalenderecos = enderecoService.encontrar();
		return ResponseEntity.ok(optionalenderecos);
	}

	@ApiOperation(value = "Busque o endereço pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<Endereco> getenderecoPorId(@PathVariable EnderecoId id) 
			throws NullPointerException, Exception {

		Optional<Endereco> optionalendereco = enderecoService.encontrar(id);
		if (verificaEnderecoExistente(id)) {
			return ResponseEntity.ok(optionalendereco.get());
		}
		throw new NullPointerException("O endereço procurado não existe no banco de dados");
	}

	@ApiOperation(value = "Cadastre um novo endereço")
	@PostMapping
	public ResponseEntity<String> postendereco(@RequestBody CriarEndereco comando) throws Exception {

		Optional<EnderecoId> optionalenderecoId = enderecoService.salvar(comando);
		if (optionalenderecoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalenderecoId.get()).toUri();
			return ResponseEntity.created(location).body("Endereço cadastrado com sucesso");
		}
		throw new Exception("O endereço não foi salvo devido a um erro interno");
	}

	@ApiOperation(value = "Altere um endereço")
	@PutMapping
	public ResponseEntity<String> putendereco(@RequestBody EditarEndereco comando)
			throws SQLException, NullPointerException, Exception {

		if (!verificaEnderecoExistente(comando.getId())) {
			throw new NullPointerException("O endereço a ser alterado não existe no banco de dados");
		}

		Optional<EnderecoId> optionalenderecoId = enderecoService.alterar(comando);
		if (optionalenderecoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalenderecoId.get()).toUri();
			return ResponseEntity.created(location).body("Endereço alterado com sucesso");
		} else {
			throw new SQLException("Erro interno durante a alteração do endereço");
		}
	}
	
	@ApiOperation(value = "Delete um endereço pelo id")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarSexo(@PathVariable EnderecoId id) throws Exception {

		if (verificaEnderecoExistente(id)) {
			Optional<String> optionalEndereco = enderecoService.deletar(id);
			return ResponseEntity.ok(optionalEndereco);
		}
		throw new NullPointerException("O endereço a deletar não existe no banco de dados");
	}

	private boolean verificaEnderecoExistente(EnderecoId id) throws Exception{
		if (!enderecoService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}