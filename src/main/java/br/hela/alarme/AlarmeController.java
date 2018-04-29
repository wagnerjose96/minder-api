package br.hela.alarme;

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
import br.hela.alarme.comandos.BuscarAlarme;
import br.hela.alarme.comandos.CriarAlarme;
import br.hela.alarme.comandos.EditarAlarme;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Alarme Controller")
@RestController
@RequestMapping("/alarmes")
public class AlarmeController {
	@Autowired
	private AlarmeService alarmeService;

	@ApiOperation(value = "Busque todos os alarmes")
	@GetMapping
	public ResponseEntity<List<BuscarAlarme>> getAlarmes() throws Exception {
		Optional<List<BuscarAlarme>> optionalAlarmes = alarmeService.encontrar();
		return ResponseEntity.ok(optionalAlarmes.get());
	}

	@ApiOperation(value = "Busque o alarme pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarAlarme> getAlarmePorId(@PathVariable AlarmeId id) 
			throws NullPointerException, Exception {

		Optional<BuscarAlarme> optionalAlarme = alarmeService.encontrar(id);
		if (verificaAlarmeExistente(id)) {
			return ResponseEntity.ok(optionalAlarme.get());
		}
		throw new NullPointerException("O alarme procurado não existe no banco de dados");
	}

	@ApiOperation(value = "Cadastre um novo alarme")
	@PostMapping
	public ResponseEntity<String> postAlarme(@RequestBody CriarAlarme comando) throws Exception {

		Optional<AlarmeId> optionalAlarmeId = alarmeService.salvar(comando);
		if (optionalAlarmeId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalAlarmeId.get()).toUri();
			return ResponseEntity.created(location).body("Alarme cadastrado com sucesso");
		}
		throw new Exception("O alarme não foi salvo devido a um erro interno");
	}

	@ApiOperation(value = "Altere um alarme")
	@PutMapping
	public ResponseEntity<String> putAlarme(@RequestBody EditarAlarme comando)
			throws SQLException, NullPointerException, Exception {

		if (!verificaAlarmeExistente(comando.getId())) {
			throw new NullPointerException("O alarme a ser alterado não existe no banco de dados");
		}

		Optional<AlarmeId> optionalAlarmeId = alarmeService.alterar(comando);
		if (optionalAlarmeId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalAlarmeId.get()).toUri();
			return ResponseEntity.created(location).body("Alarme alterado com sucesso");
		} else {
			throw new SQLException("Erro interno durante a alteração do alarme");
		}

	}

	private boolean verificaAlarmeExistente(AlarmeId id) throws Exception{
		if (!alarmeService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}