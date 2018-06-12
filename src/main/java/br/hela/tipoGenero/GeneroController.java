package br.hela.tipoGenero;

import java.net.URI;
import java.nio.file.AccessDeniedException;
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

import br.hela.security.AutenticaAdm;
import br.hela.tipoGenero.comandos.CriarGenero;
import br.hela.tipoGenero.comandos.EditarGenero;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/generos")
public class GeneroController {
	@Autowired
	private GeneroService service;

	@Autowired
	private AutenticaAdm autentica;

	@ApiOperation(value = "Busque todos os gêneros")
	@GetMapping
	public ResponseEntity<List<Genero>> getGenero(@RequestHeader String token) throws AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<List<Genero>> optionalGeneros = service.encontrarTudo();
			return ResponseEntity.ok(optionalGeneros.get());
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Busque um gênero pelo id")
	@GetMapping("/{id}")
	public ResponseEntity<Genero> getGeneroPorId(@PathVariable GeneroId id, @RequestHeader String token)
			throws NullPointerException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (verificaGeneroExistente(id)) {
				Optional<Genero> optionalGenero = service.encontrarPorId(id);
				return ResponseEntity.ok(optionalGenero.get());
			}
			throw new NullPointerException("O gênero procurado não existe no banco de dados");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Cadastre um novo gênero")
	@PostMapping
	public ResponseEntity<String> postGenero(@RequestBody CriarGenero comando, @RequestHeader String token)
			throws Exception, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			Optional<GeneroId> optionalGeneroId = service.salvar(comando);
			if (optionalGeneroId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalGeneroId.get()).toUri();
				return ResponseEntity.created(location).body("O gênero foi cadastrado com sucesso");
			}
			throw new Exception("O gênero não foi salvo devido a um erro interno");
		}
		throw new AccessDeniedException("Acesso negado");
	}

	@ApiOperation(value = "Altere um gênero")
	@PutMapping
	public ResponseEntity<String> putGenero(@RequestBody EditarGenero comando, @RequestHeader String token)
			throws NullPointerException, InternalError, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (!verificaGeneroExistente(comando.getId())) {
				throw new NullPointerException("O gênero a ser alterado não existe no banco de dados");
			}
			Optional<GeneroId> optionalGeneroId = service.alterar(comando);
			if (optionalGeneroId.isPresent()) {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
						.buildAndExpand(optionalGeneroId.get()).toUri();
				return ResponseEntity.created(location).body("Gênero alterado com sucesso");
			} else {
				throw new InternalError("Erro interno durante a alteração do gênero");
			}
		}
		throw new AccessDeniedException("Acesso negado");
	}

	private boolean verificaGeneroExistente(GeneroId id) {
		if (!service.encontrarPorId(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

}
