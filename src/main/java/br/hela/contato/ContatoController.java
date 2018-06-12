package br.hela.contato;

import java.net.URI;
import java.nio.file.AccessDeniedException;
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

@Api(description = "Basic Contato Controller")
@RestController
@RequestMapping("/contatos")
public class ContatoController {

	@Autowired
	private ContatoService contatoService;
	
	@Autowired
	private AutenticaRequisicao autentica;

	@ApiOperation(value = "Busque todos os contatos")
	@GetMapping
	public ResponseEntity<List<BuscarContato>> getContatos(@RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<List<BuscarContato>> optionalContatos = contatoService.encontrar();
			return ResponseEntity.ok(optionalContatos.get());
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Busque o contato pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<BuscarContato> getContatoPorId(@PathVariable ContatoId id, @RequestHeader String token)
			throws NullPointerException, Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<BuscarContato> optionalContato = contatoService.encontrar(id);
			if (verificaContatoExistente(id)) {
				return ResponseEntity.ok(optionalContato.get());
			}
			throw new NullPointerException("O contato procurado não existe no banco de dados");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Cadastre um novo contato")
	@PostMapping
	public ResponseEntity<String> postContato(@RequestBody CriarContato comando, @RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<ContatoId> optionalContatoId = contatoService.salvar(comando);
			if (optionalContatoId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalContatoId.get()).toUri();
				return ResponseEntity.created(location).body("Contato cadastrado com sucesso");
			}
			throw new Exception("O contato não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Altere um contato")
	@PutMapping
	public ResponseEntity<String> putContato(@RequestBody EditarContato comando, @RequestHeader String token)
			throws SQLException, NullPointerException, Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaContatoExistente(comando.getId())) {
				throw new NullPointerException("O contato a ser alterado não existe no banco de dados");
			}

			Optional<ContatoId> optionalContatoId = contatoService.alterar(comando);
			if (optionalContatoId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalContatoId.get()).toUri();
				return ResponseEntity.created(location).body("Contato alterado com sucesso");
			} else {
				throw new SQLException("Erro interno durante a alteração do contato");
			}
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Delete um contato pelo id")
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<String>> deletarContato(@PathVariable ContatoId id, @RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {

			if (verificaContatoExistente(id)) {
				Optional<String> optionalContato = contatoService.deletar(id);
				return ResponseEntity.ok(optionalContato);
			}
			throw new NullPointerException("O contato a deletar não existe no banco de dados");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	private boolean verificaContatoExistente(ContatoId id) throws Exception {
		if (!contatoService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}