package br.minder.pergunta_notificacao;

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

import br.minder.pergunta_notificacao.comandos.BuscarPergunta;
import br.minder.pergunta_notificacao.comandos.CriarPergunta;
import br.minder.pergunta_notificacao.comandos.EditarPergunta;
import br.minder.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Perguntas Controller")
@RestController
@RequestMapping("/api/pergunta")
@CrossOrigin
public class PerguntaController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private PerguntaService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque todas as perguntas")
	@GetMapping
	public ResponseEntity<List<BuscarPergunta>> getPerguntas() {
		Optional<List<BuscarPergunta>> optionalPergunta = service.encontrar();
		if (optionalPergunta.isPresent()) {
			return ResponseEntity.ok(optionalPergunta.get());
		}
		throw new NullPointerException("Não existe nenhuma pergunta cadastrada no banco de dados");
	}

	@ApiOperation("Busque uma pergunta pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarPergunta> getPerguntaPorId(@PathVariable PerguntaId id) {
		Optional<BuscarPergunta> optionalPergunta = service.encontrar(id);
		if (optionalPergunta.isPresent()) {
			return ResponseEntity.ok(optionalPergunta.get());
		}
		throw new NullPointerException("A pergunta procurada não existe no banco de dados");
	}

	@ApiOperation("Cadastre uma nova pergunta")
	@PostMapping
	public ResponseEntity<String> postPergunta(@RequestBody CriarPergunta comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			Optional<PerguntaId> optionalPerguntaId = service.salvar(comando);
			if (optionalPerguntaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalPerguntaId.get()).toUri();
				return ResponseEntity.created(location).body("A pergunta foi cadastrada com sucesso");
			}
			throw new SQLException("A pergunta não foi salva devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Altere uma pergunta")
	@PutMapping
	public ResponseEntity<String> putPergunta(@RequestBody EditarPergunta comando, @RequestHeader String token)
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicaoAdm(token)) {
			if (!service.encontrar(comando.getIdPergunta()).isPresent()) {
				throw new NullPointerException("A pergunta a ser alterada não existe no banco de dados");
			}
			Optional<PerguntaId> optionalPerguntaId = service.alterar(comando);
			if (optionalPerguntaId.isPresent()) {
				return ResponseEntity.ok().body("A pergunta foi alterada com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração da pergunta");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

}