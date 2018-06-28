package br.hela.pergunta_notificacao.pergunta_resposta_usuario;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.hela.pergunta_notificacao.pergunta_resposta_usuario.comandos.BuscarPerguntaRespostaUsuario;
import br.hela.pergunta_notificacao.pergunta_resposta_usuario.comandos.CriarPerguntaRespostaUsuario;
import br.hela.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Respostas de Notificação Controller")
@RestController
@RequestMapping("/perguntaNotificacao")
@CrossOrigin
public class PerguntaRespostaUsuarioController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private PerguntaRespostaUsuarioService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation("Busque todas as perguntas de notificações respondidas")
	@GetMapping
	public ResponseEntity<List<BuscarPerguntaRespostaUsuario>> getPerguntaResposta(@RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<List<BuscarPerguntaRespostaUsuario>> optionalPergunta = service.encontrar(autentica.idUser(token));
			if (optionalPergunta.isPresent()) {
				return ResponseEntity.ok(optionalPergunta.get());
			}
			return ResponseEntity.notFound().build();
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Busque uma pergunta de notificação respondida pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarPerguntaRespostaUsuario> getPerguntaPorId(@PathVariable PerguntaRespostaUsuarioId id,
			@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarPerguntaRespostaUsuario> optionalPergunta = service.encontrar(id, autentica.idUser(token));
			if (optionalPergunta.isPresent()) {
				return ResponseEntity.ok(optionalPergunta.get());
			}
			throw new NullPointerException("A pergunta procurada não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Responda uma pergunta de notificação")
	@PostMapping
	public ResponseEntity<String> postPergunta(@RequestBody CriarPerguntaRespostaUsuario comando,
			@RequestHeader String token) throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<PerguntaRespostaUsuarioId> optionalPerguntaId = service.salvar(comando, autentica.idUser(token));
			if (optionalPerguntaId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalPerguntaId.get()).toUri();
				return ResponseEntity.created(location).body("A pergunta foi cadastrada com sucesso");
			}
			throw new SQLException("A pergunta não foi salva devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

}
