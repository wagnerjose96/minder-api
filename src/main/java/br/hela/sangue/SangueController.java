package br.hela.sangue;

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

import br.hela.sangue.comandos.BuscarSangue;
import br.hela.sangue.comandos.CriarSangue;
import br.hela.sangue.comandos.EditarSangue;
import br.hela.security.AutenticaAdm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Tipo Sanguíneo Controller")
@RestController
@RequestMapping("/sangues")
public class SangueController {
	@Autowired
	private SangueService service;

	@Autowired
	private AutenticaAdm autentica;

	@ApiOperation("Busque todos os tipos sanguíneos")
	@GetMapping
	public ResponseEntity<List<BuscarSangue>> getSangue() throws SQLException, Exception {
		Optional<List<BuscarSangue>> optionalSangues = service.encontrar();
		return ResponseEntity.ok(optionalSangues.get());
	}

	@ApiOperation("Busque um tipo sanguíneo pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarSangue> getSanguePorId(@PathVariable SangueId id)
			throws SQLException, Exception, NullPointerException {
		if (verificaSangueExistente(id)) {
			Optional<BuscarSangue> optionalSangue = service.encontrar(id);
			return ResponseEntity.ok(optionalSangue.get());
		}
		throw new NullPointerException("O tipo sanguíneo procurado não existe no banco de dados");
	}

	@ApiOperation("Cadastre um tipo sanguíneo")
	@PostMapping
	public ResponseEntity<String> postSangue(@RequestBody CriarSangue comando, @RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<SangueId> optionalSangueId = service.salvar(comando);
			if (optionalSangueId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalSangueId.get()).toUri();
				return ResponseEntity.created(location).body("O tipo sanguíneo foi cadastrado com sucesso");
			}
			throw new Exception("O tipo sanguíneo não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation("Altere um tipo sanguíneo")
	@PutMapping
	public ResponseEntity<String> putMedicamentoContinuo(@RequestBody EditarSangue comando,
			@RequestHeader String token) throws NullPointerException, Exception, SQLException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaSangueExistente(comando.getIdSangue())) {
				throw new NullPointerException("O tipo sanguíneo a ser alterado não existe no banco de dados");
			}
			Optional<SangueId> optionalSangueId = service.alterar(comando);
			if (optionalSangueId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalSangueId.get()).toUri();
				return ResponseEntity.created(location).body("Tipo sanguíneo alterado com sucesso");
			} else {
				throw new InternalError("Erro interno durante a alteração do tipo sanguíneo");
			}
		}
		throw new AccessDeniedException("Acesso negado");
	}

	private boolean verificaSangueExistente(SangueId id) {
		if (!service.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}
}