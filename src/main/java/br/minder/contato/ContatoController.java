package br.minder.contato;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.minder.contato.comandos.BuscarContato;
import br.minder.contato.comandos.CriarContato;
import br.minder.contato.comandos.EditarContato;
import br.minder.security.Autentica;
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
	private Autentica autentica;

	@Autowired
	private ContatoRepository repo;

	@ApiOperation("Busque todos os contatos")
	@GetMapping
	public Page<Contato> getContatos(Pageable p,
			@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
			@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!repo.findAll(autentica.idUser(token).toString()).isEmpty()) {
				if (searchTerm.isEmpty()) {
					return repo.findAll(p, autentica.idUser(token).toString());
				}
				return repo.findAll(p, "%" + searchTerm + "%", autentica.idUser(token).toString());
			}
		} else {
			throw new NullPointerException("Não existe nenhum contato cadastrado no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);

	}

	@ApiOperation("Busque um contato pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarContato> getContatoPorId(@PathVariable ContatoId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarContato> optionalContato = contatoService.encontrar(id, autentica.idUser(token));
			if (optionalContato.isPresent()) {
				return ResponseEntity.ok(optionalContato.get());
			}
			throw new NullPointerException("O contato procurado não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Cadastre um novo contato")
	@PostMapping
	public ResponseEntity<String> postContato(@RequestBody CriarContato comando, @RequestHeader String token)
			throws SQLException, AccessDeniedException {
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
			throws AccessDeniedException, SQLException {
		if (autentica.autenticaRequisicao(token)) {
			if (!contatoService.encontrar(comando.getId(), autentica.idUser(token)).isPresent()) {
				throw new NullPointerException("O contato a ser alterado não existe no banco de dados");
			}

			Optional<ContatoId> optionalContatoId = contatoService.alterar(comando);
			if (optionalContatoId.isPresent()) {
				return ResponseEntity.ok().body("O contato foi alterado com sucesso");
			} else {
				throw new SQLException("Ocorreu um erro interno durante a alteração do contato");
			}
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

	@ApiOperation("Delete um contato pelo ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletarContato(@PathVariable ContatoId id, @RequestHeader String token)
			throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<String> optionalContato = contatoService.deletar(id, autentica.idUser(token));
			if (optionalContato.isPresent())
				return ResponseEntity.ok(optionalContato.get());
			throw new NullPointerException("O contato a deletar não existe no banco de dados");
		}
		throw new AccessDeniedException(ACESSONEGADO);
	}

}