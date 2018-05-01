package br.hela.contatoEmergencia;

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

import br.hela.contatoEmergencia.comandos.BuscarContatoEmergencia;
import br.hela.contatoEmergencia.comandos.CriarContatoEmergencia;
import br.hela.contatoEmergencia.comandos.EditarContatoEmergencia;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Contato De Emergência Controller")
@RestController
@RequestMapping("/contatos")
public class ContatoEmergenciaController {

	@Autowired
	private ContatoEmergenciaService contatoEmergenciaService;

	@ApiOperation(value = "Busque todas os contatos de emergência")
	@GetMapping
	public ResponseEntity<List<ContatoEmergencia>> getContatoEmergencias() throws Exception {
		Optional<List<ContatoEmergencia>> optionalContatoEmergencias = contatoEmergenciaService.encontrar();
		return ResponseEntity.ok(optionalContatoEmergencias.get());
	}

	@ApiOperation(value = "Busque um contato de emergência pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarContatoEmergencia> getContatoEmergenciaPorId(@PathVariable ContatoEmergenciaId id) throws NullPointerException, Exception {

		Optional<BuscarContatoEmergencia> optionalContatoEmergencia = contatoEmergenciaService.encontrar(id);
		if (verificaContatoEmergenciaExistente(id)) {
			return ResponseEntity.ok(optionalContatoEmergencia.get());
		}
		throw new NullPointerException("O contato emergência procurado não existe no banco de dados");
	}

	@ApiOperation(value = "Cadastre um novo contato emergência")
	@PostMapping
	public ResponseEntity<String> postContatoEmergencia(@RequestBody CriarContatoEmergencia comando) throws Exception {

		Optional<ContatoEmergenciaId> optionalContatoEmergenciaId = contatoEmergenciaService.executar(comando);
		if (optionalContatoEmergenciaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalContatoEmergenciaId.get()).toUri();
			return ResponseEntity.created(location).body("Contato de emergência criado com sucesso");
		}
		throw new Exception("O contato de emergência não foi salvo devido a um erro interno");
	}

	@ApiOperation(value = "Altere um contato de emergência")
	@PutMapping
	public ResponseEntity<String> putContatoEmergencia(@RequestBody EditarContatoEmergencia comando) throws SQLException, NullPointerException, Exception {

		if (!verificaContatoEmergenciaExistente(comando.getIdContatoEmergencia())) {
			throw new NullPointerException("O contato de emergência a ser alterado não existe no banco de dados");
		}
		Optional<ContatoEmergenciaId> optionalContatoEmergenciaId = contatoEmergenciaService.alterar(comando);
		if (optionalContatoEmergenciaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalContatoEmergenciaId.get()).toUri();
			return ResponseEntity.created(location).body("Contato de emergência alterado com sucesso");
		}

		else {
			throw new SQLException("Erro interno durante a alteração do contato de emergência");
		}

	}

	private boolean verificaContatoEmergenciaExistente(ContatoEmergenciaId id) throws Exception {
		if (!contatoEmergenciaService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}