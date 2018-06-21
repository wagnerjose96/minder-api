package br.hela.contato;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.hela.contato.comandos.BuscarContato;
import br.hela.contato.comandos.CriarContato;
import br.hela.contato.comandos.EditarContato;
import br.hela.security.AutenticaRequisicao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Contato Controller")
@RestController
@RequestMapping("/contatos")
@CrossOrigin
public class ContatoController {
	private static final String ACESSONEGADO = "Acesso negado";

	@Autowired
	private ContatoService contatoService;

	@Autowired
	private AutenticaRequisicao autentica;

	@ApiOperation("Busque todos os contatos")
	@GetMapping
	public ResponseEntity<List<BuscarContato>> getContatos() throws Exception {
		Optional<List<BuscarContato>> optionalContatos = contatoService.encontrar();
		return ResponseEntity.ok(optionalContatos.get());
	}

	@ApiOperation("Busque o contato pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarContato> getContatoPorId(@PathVariable ContatoId id) throws Exception {
		Optional<BuscarContato> optionalContato = contatoService.encontrar(id);
		if (verificaContatoExistente(id)) {
			return ResponseEntity.ok(optionalContato.get());
		}
		throw new NullPointerException("O contato procurado não existe no banco de dados");
	}

	@ApiOperation("Cadastre um novo contato")
	@PostMapping
	public ResponseEntity<String> postContato(@RequestBody CriarContato comando, @RequestHeader String token)
			throws Exception {
		if (autentica.autenticaRequisicao(token)) {
			Optional<ContatoId> optionalContatoId = contatoService.salvar(comando, autentica.idUser(token));
			if (optionalContatoId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalContatoId.get()).toUri();
				return ResponseEntity.created(location).body("O contato foi cadastrado com sucesso");
			}
			throw new SQLException("O contato não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Altere um contato")
	@PutMapping
	public ResponseEntity<String> putContato(@RequestBody EditarContato comando, @RequestHeader String token)
			throws Exception {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaContatoExistente(comando.getId())) {
				throw new NullPointerException("O contato a ser alterado não existe no banco de dados");
			}

			Optional<ContatoId> optionalContatoId = contatoService.alterar(comando);
			if (optionalContatoId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalContatoId.get()).toUri();
				return ResponseEntity.created(location).body("O contato foi alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do contato");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Delete um contato pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarContato(@PathVariable ContatoId id, @RequestHeader String token)
			throws Exception {
		if (autentica.autenticaRequisicao(token)) {

			if (verificaContatoExistente(id)) {
				Optional<String> optionalContato = contatoService.deletar(id, autentica.idUser(token));
				return ResponseEntity.ok(optionalContato);
			}
			throw new NullPointerException("O contato a deletar não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	private boolean verificaContatoExistente(ContatoId id) throws Exception {
		if (!contatoService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}