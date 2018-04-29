package br.hela.telefone;

import java.net.URI;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.hela.telefone.comandos.CriarTelefone;
import br.hela.telefone.comandos.EditarTelefone;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Telefone Controller")
@Controller
@RequestMapping("/telefones")
public class TelefoneController {
	@Autowired
	TelefoneService service;
	
	@ApiOperation(value = "Busque todos os telefones")
	@GetMapping
	public ResponseEntity<Optional<List<Telefone>>> getTelefone(){
		Optional<List<Telefone>> telefones = service.encontrar();
		return ResponseEntity.ok(telefones);
	}
	
	@ApiOperation(value = "Busque um telefone pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<Telefone> getTelefonePorId(@PathVariable TelefoneId id) throws NullPointerException {
		Optional<Telefone> telefone = service.encontrar(id);
		if(verificaTelefoneExistente(id)) {
			return ResponseEntity.ok(telefone.get());
		}
		throw new NullPointerException("O telefone procurado não existente no banco de dados"); 
	}

	@ApiOperation(value = "Delete um telefone pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarTelefone(@PathVariable TelefoneId id) throws NullPointerException {
		if(verificaTelefoneExistente(id)) {
			Optional<String> telefone = service.deletar(id);
			return ResponseEntity.ok(telefone);
		}
		throw new NullPointerException("O telefone a deletar não existe no banco de dados");
	}
	
	@ApiOperation(value = "Cadastre um novo telefone")
	@PostMapping
	public ResponseEntity<String> postTelefone(@RequestBody CriarTelefone comandos) throws Exception {
		Optional<TelefoneId> telefone = service.salvar(comandos);
		if(telefone.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(telefone.get()).toUri();
			return ResponseEntity.created(location).body("Telefone cadastrado com sucesso");
		} else {
			throw new SQLException("Erro interno durante a criação do telefone");
		}
	}
	
	@ApiOperation(value = "Altere um telefone")
	@PutMapping
	public ResponseEntity<String> putTelfeone(@RequestBody EditarTelefone comandos) throws NullPointerException, Exception {
		if (!verificaTelefoneExistente(comandos.getTelefoneId())) {
			throw new NullPointerException("O telefone a ser alterada não existe no banco de dados");
		}
		Optional<TelefoneId> telefone = service.alterar(comandos);
		if(telefone.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(telefone.get()).toUri();
			return ResponseEntity.created(location).body("Telefone alterado com sucesso");
		} else {
			throw new SQLException("Erro interno durante a alteração do telefone");
		}
	}
	
	private boolean verificaTelefoneExistente(TelefoneId id) {
		if(!service.encontrar(id).isPresent()) {
			return false;
		}
		return true;
	}
	
}
