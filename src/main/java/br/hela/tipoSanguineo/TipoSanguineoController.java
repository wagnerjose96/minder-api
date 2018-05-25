package br.hela.tipoSanguineo;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.hela.tipoSanguineo.comandos.CriarTipoSanguineo;
import br.hela.tipoSanguineo.comandos.EditarTipoSanguineo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Tipo Sanguíneo Controller")
@Controller
@RequestMapping("/sangues")
public class TipoSanguineoController {
	@Autowired
	TipoSanguineoService service;
	
	@ApiOperation(value = "Altere um tipo sanguíneo")
	@PutMapping
	public ResponseEntity<String> putTipoSanguineo(@RequestBody EditarTipoSanguineo comando)
			throws NullPointerException, InternalError {

		if (!verificaTipoSangueExistente(comando.getTipoSanguineoId())) {
			throw new NullPointerException("O tipo sanguíneo a ser alterado não existe no banco de dados");
		}
		Optional<TipoSanguineoId> optionalTipoSanguineoId = service.alterar(comando);
		if (optionalTipoSanguineoId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalTipoSanguineoId.get()).toUri();
			return ResponseEntity.created(location).body("Tipo sanguíneo alterado com sucesso");
		} else {
			throw new InternalError("Erro interno durante a alteração do tipo sanguíneo");
		}
	}
	
	@ApiOperation(value = "Busque todos os tipos sanguíneos")
	@GetMapping
	public ResponseEntity<Optional<List<TipoSanguineo>>> getTipoSanguineo(){
		Optional<List<TipoSanguineo>> tipoSangue = service.encontrarTudo();
		return ResponseEntity.ok(tipoSangue);
	}
	
	@ApiOperation(value = "Busque um tipo sanguíneo pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<TipoSanguineo> getTipoSanguineoPorId(@PathVariable TipoSanguineoId id) throws NullPointerException {
		Optional<TipoSanguineo> tipoSangue = service.encontrarPorId(id);
		if(verificaTipoSangueExistente(id)) {
			return ResponseEntity.ok(tipoSangue.get());
		}
		throw new NullPointerException("O tipo sanguíneo procurado não existente no banco de dados"); 
	}
	
	@ApiOperation(value = "Cadastre um novo tipo sanguíneo")
	@PostMapping
	public ResponseEntity<String> postTelefone(@RequestBody CriarTipoSanguineo comandos) throws Exception {
		Optional<TipoSanguineoId> telefone = service.salvar(comandos);
		if(telefone.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(telefone.get()).toUri();
			return ResponseEntity.created(location).body("Tipo sanguíneo cadastrado com sucesso");
		} else {
			throw new SQLException("Erro interno durante a criação do tipo sanguíneo");
		}
	}
	
	private boolean verificaTipoSangueExistente(TipoSanguineoId id) {
		if(!service.encontrarPorId(id).isPresent()) {
			return false;
		}
		return true;
	}

}
