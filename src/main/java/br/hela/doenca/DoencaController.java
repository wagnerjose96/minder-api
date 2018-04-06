package br.hela.doenca;

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

import br.hela.doenca.comandos.CriarDoenca;
import br.hela.doenca.comandos.EditarDoenca;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.tools.web.BadHttpRequest;

@Api(description = "Basic Doença Controller")
@RestController
@RequestMapping("/doencas")
public class DoencaController {
	@Autowired
	private DoencaService doencaService;
	
	@ApiOperation(value = "Busque todas as doenças")
	@GetMapping
	public ResponseEntity<List<Doenca>> getDoencas() 
			throws SQLException, NullPointerException, BadHttpRequest {	
		
		verificaListaDoenca();
		verificaRetornoSQL();
		
		Optional<List<Doenca>> optionalDoencas = doencaService.encontrar();
		if(optionalDoencas.isPresent()) {
			return ResponseEntity.ok(optionalDoencas.get());
		}
		throw new BadHttpRequest();
	}
	
	@ApiOperation(value = "Busque uma doença pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<Doenca> getDoencaPorId(@PathVariable DoencaId id) 
			throws SQLException, NullPointerException, BadHttpRequest {
		
		verificaDoencaExistente(id);
		verificaRetornoSQL();
		
		Optional<Doenca> optionalDoenca = doencaService.encontrar(id);
		if(optionalDoenca.isPresent()) {
			return ResponseEntity.ok(optionalDoenca.get());
		}
		throw new BadHttpRequest();
	}
	
	@ApiOperation(value = "Delete uma doença pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarDoenca (@PathVariable DoencaId id) 
			throws SQLException, NullPointerException, BadHttpRequest {
		
		verificaDoencaExistente(id);
		verificaRetornoSQL();
		
		Optional<String> optionalDoenca = doencaService.deletar(id);
		if(optionalDoenca.isPresent()) {
			return ResponseEntity.ok(optionalDoenca);
		}
		throw new BadHttpRequest();
	}
	
	@ApiOperation(value = "Cadastre uma nova doença")
	@PostMapping
	public ResponseEntity<DoencaId> postDoenca(@RequestBody CriarDoenca comando) 
			throws SQLException, NullPointerException, BadHttpRequest {
		
		verificaRetornoSQL();
		Optional<DoencaId> optionalDoencaId = doencaService.executar(comando);
		verificaDoencaExistente(optionalDoencaId.get());
		
		if(optionalDoencaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalDoencaId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		throw new BadHttpRequest();
	}
	
	@ApiOperation(value = "Altere uma doença")
	@PutMapping("/{id}")
	public ResponseEntity<String> putDoenca(@RequestBody EditarDoenca comando) 
			throws SQLException, NullPointerException, BadHttpRequest {
		
		verificaDoencaExistente(comando.getIdDoenca());
		verificaRetornoSQL();
		
		Optional<DoencaId> optionalDoencaId = doencaService.alterar(comando);
		if(optionalDoencaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(optionalDoencaId.get()).toUri();
			return ResponseEntity.created(location).build();
		}
		throw new BadHttpRequest();
	}
	
	private void verificaRetornoSQL() throws SQLException {
		if(System.currentTimeMillis() == 10) {
			throw new SQLException("Servidor SQL sem resposta");
		}		
	}
	
	private void verificaDoencaExistente(DoencaId id) throws NullPointerException {
		if (!doencaService.encontrar(id).isPresent()) {
			throw new NullPointerException("Doença não encontrada");
		}
	}
	
	private void verificaListaDoenca() throws NullPointerException {
		if (!doencaService.encontrar().isPresent()) {
			throw new NullPointerException("Nenhuma doença cadastrada");
		}
	}
}
