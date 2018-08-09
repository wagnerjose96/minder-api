package br.minder.pergunta_notificacao.resposta;

import java.net.URI;
import java.nio.file.AccessDeniedException;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.minder.pergunta_notificacao.resposta.comandos.BuscarResposta;
import br.minder.pergunta_notificacao.resposta.comandos.CriarResposta;
import br.minder.pergunta_notificacao.resposta.comandos.EditarResposta;
import br.minder.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Respostas Controller")
@RestController
@RequestMapping("/respostas")
@CrossOrigin
public class RespostaController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private RespostaService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque todas as respostas")
	@GetMapping
	public ResponseEntity<List<BuscarResposta>> getRespostas() {
		Optional<List<BuscarResposta>> optionalResposta = service.encontrar();
		if (optionalResposta.isPresent()) {
			return ResponseEntity.ok(optionalResposta.get());
		}
		throw new NullPointerException("Não existe nenhuma resposta cadastrada no banco de dados");
	}

	@ApiOperation("Busque uma resposta pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarResposta> getRespostaPorId(@PathVariable RespostaId id) {
		Optional<BuscarResposta> optionalResposta = service.encontrar(id);
		if (optionalResposta.isPresent()) {
			return ResponseEntity.ok(optionalResposta.get());
		}
		throw new NullPointerException("A resposta procurada não existe no banco de dados");
	}

	@ApiOperation("Cadastre uma nova resposta")
	@PostMapping
	public ResponseEntity<String> postResposta(@RequestBody CriarResposta comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			Optional<RespostaId> optionalRespostaId = service.salvar(comando);
			if (optionalRespostaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalRespostaId.get()).toUri();
				return ResponseEntity.created(location).body("A resposta foi cadastrada com sucesso");
			}
			throw new SQLException("A resposta não foi salva devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Altere uma resposta")
	@PutMapping
	public ResponseEntity<String> putResposta(@RequestBody EditarResposta comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			if (!service.encontrar(comando.getIdResposta()).isPresent()) {
				throw new NullPointerException("A resposta a ser alterada não existe no banco de dados");
			}
			Optional<RespostaId> optionalRespostaId = service.alterar(comando);
			if (optionalRespostaId.isPresent()) {
				return ResponseEntity.ok().body("A resposta foi alterada com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração da resposta");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}
}