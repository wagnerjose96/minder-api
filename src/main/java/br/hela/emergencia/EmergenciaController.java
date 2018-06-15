package br.hela.emergencia;

import java.net.URI;
import java.nio.file.AccessDeniedException;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.hela.emergencia.comandos.BuscarEmergencia;
import br.hela.emergencia.comandos.CriarEmergencia;
import br.hela.emergencia.comandos.EditarEmergencia;
import br.hela.security.AutenticaRequisicao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Emergência Controller")
@RestController
@RequestMapping("/emergencias")
public class EmergenciaController {
	@Autowired
	private EmergenciaService service;

	@Autowired
	private AutenticaRequisicao autentica;

	@ApiOperation("Busque todas as emergencias")
	@GetMapping
	public ResponseEntity<List<BuscarEmergencia>> getEmergencias() throws SQLException, Exception {
		Optional<List<BuscarEmergencia>> optionalEmergencias = service.encontrar();
		return ResponseEntity.ok(optionalEmergencias.get());
	}

	@ApiOperation("Busque um medicamento pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarEmergencia> getEmergenciaPorId(@PathVariable EmergenciaId id)
			throws SQLException, Exception, NullPointerException {
		if (verificaEmergenciaExistente(id)) {
			Optional<BuscarEmergencia> optionalEmergencia = service.encontrar(id);
			return ResponseEntity.ok(optionalEmergencia.get());
		}
		throw new NullPointerException("O medicamento procurado não existe no banco de dados");
	}

	@ApiOperation("Cadastre um novo medicamento")
	@PostMapping
	public ResponseEntity<String> postEmergencia(@RequestBody CriarEmergencia comando, @RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<EmergenciaId> optionalEmergenciaId = service.salvar(comando, autentica.idUser(token));
			if (optionalEmergenciaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalEmergenciaId.get()).toUri();
				return ResponseEntity.created(location).body("O medicamento foi cadastrado com sucesso");
			}
			throw new Exception("O medicamento não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation("Altere um medicamento")
	@PutMapping
	public ResponseEntity<String> putEmergencia(@RequestBody EditarEmergencia comando,
			@RequestHeader String token) throws NullPointerException, Exception, SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaEmergenciaExistente(comando.getId())) {
				throw new NullPointerException("O medicamento a ser alterado não existe no banco de dados");
			}
			Optional<EmergenciaId> optionalEmergenciaId = service.alterar(comando);
			if (optionalEmergenciaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalEmergenciaId.get()).toUri();
				return ResponseEntity.created(location).body("Medicamento alterado com sucesso");
			} else {
				throw new InternalError("Erro interno durante a alteração do medicamento");
			}
		}
		throw new AccessDeniedException("Acesso negado");
	}

	private boolean verificaEmergenciaExistente(EmergenciaId id) {
		if (!service.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}
}