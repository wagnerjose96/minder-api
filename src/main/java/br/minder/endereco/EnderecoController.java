package br.minder.endereco;

import java.net.URI;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.minder.endereco.comandos.BuscarEndereco;
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.endereco.comandos.EditarEndereco;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Endereço Controller")
@RestController
@RequestMapping("/enderecos")
@CrossOrigin
public class EnderecoController {
	@Autowired
	private EnderecoService enderecoService;

	@ApiOperation("Busque todos os endereços")
	@GetMapping
	public ResponseEntity<List<BuscarEndereco>> getEnderecos() {
		Optional<List<BuscarEndereco>> optionalEnderecos = enderecoService.encontrar();
		if (optionalEnderecos.isPresent()) {
			return ResponseEntity.ok(optionalEnderecos.get());
		}
		throw new NullPointerException("Não existe nenhum endereço cadastrado no banco de dados");
	}

	@ApiOperation("Busque um endereço pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarEndereco> getEnderecoPorId(@PathVariable EnderecoId id) {
		Optional<BuscarEndereco> optionalEndereco = enderecoService.encontrar(id);
		if (optionalEndereco.isPresent()) {
			return ResponseEntity.ok(optionalEndereco.get());
		}
		throw new NullPointerException("O endereço procurado não existe no banco de dados");
	}

	@ApiOperation("Cadastre um novo endereço")
	@PostMapping
	public ResponseEntity<String> postEndereco(@RequestBody CriarEndereco comando) throws SQLException {
		Optional<EnderecoId> optionalEnderecoId = enderecoService.salvar(comando);
		if (optionalEnderecoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalEnderecoId.get()).toUri();
			return ResponseEntity.created(location).body("O endereço foi cadastrado com sucesso");
		}
		throw new SQLException("O endereço não foi salvo devido a um erro interno");
	}

	@ApiOperation("Altere um endereço")
	@PutMapping
	public ResponseEntity<String> putEndereco(@RequestBody EditarEndereco comando) throws SQLException {
		if (!enderecoService.encontrar(comando.getId()).isPresent()) {
			throw new NullPointerException("O endereço a ser alterado não existe no banco de dados");
		}
		Optional<EnderecoId> optionalEnderecoId = enderecoService.alterar(comando);
		if (optionalEnderecoId.isPresent()) {
			return ResponseEntity.ok().body("O endereço foi alterado com sucesso");
		} else {
			throw new SQLException("Ocorreu um erro interno durante a alteração do endereço");
		}
	}
}