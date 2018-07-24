package br.minder.telefone;

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
import br.minder.telefone.comandos.BuscarTelefone;
import br.minder.telefone.comandos.CriarTelefone;
import br.minder.telefone.comandos.EditarTelefone;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Telefone Controller")
@RestController
@RequestMapping("/telefones")
@CrossOrigin
public class TelefoneController {
	@Autowired
	private TelefoneService service;

	@ApiOperation("Busque todos os telefones")
	@GetMapping
	public ResponseEntity<List<BuscarTelefone>> getTelefone() {
		Optional<List<BuscarTelefone>> optionalTelefones = service.encontrar();
		if (optionalTelefones.isPresent()) {
			return ResponseEntity.ok(optionalTelefones.get());
		}
		return ResponseEntity.notFound().build();
	}

	@ApiOperation("Busque um telefone pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarTelefone> getTelefonePorId(@PathVariable TelefoneId id) {
		Optional<BuscarTelefone> optionalTelefone = service.encontrar(id);
		if (optionalTelefone.isPresent()) {
			return ResponseEntity.ok(optionalTelefone.get());
		}
		throw new NullPointerException("O telefone procurado não existe no banco de dados");
	}

	@ApiOperation("Cadastre um novo telefone")
	@PostMapping
	public ResponseEntity<String> postTelefone(@RequestBody CriarTelefone comando)
			throws SQLException {
			Optional<TelefoneId> optionalTelefoneId = service.salvar(comando);
			if (optionalTelefoneId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalTelefoneId.get()).toUri();
				return ResponseEntity.created(location).body("O telefone foi cadastrado com sucesso");
			}
			throw new SQLException("O telefone não foi salvo devido a um erro interno");
	}

	@ApiOperation("Altere um telefone")
	@PutMapping
	public ResponseEntity<String> putTelefone(@RequestBody EditarTelefone comando)
			throws SQLException {
			if (!service.encontrar(comando.getId()).isPresent()) {
				throw new NullPointerException("O telefone a ser alterado não existe no banco de dados");
			}
			Optional<TelefoneId> optionalTelefoneId = service.alterar(comando);
			if (optionalTelefoneId.isPresent()) {
				return ResponseEntity.ok().body("O telefone foi alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do telefone");
			}
	}
}