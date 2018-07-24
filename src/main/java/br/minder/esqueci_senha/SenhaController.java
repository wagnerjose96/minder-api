package br.minder.esqueci_senha;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.minder.esqueci_senha.comandos.GerarSenha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Senha Controller")
@RestController
@RequestMapping("/senha")
@CrossOrigin
public class SenhaController {
	@Autowired
	private SenhaService service;

	@ApiOperation(value = "Crie uma senha aleatória para um usuário")
	@PutMapping
	public ResponseEntity<String> putUSenha(@RequestBody GerarSenha comando) {
		Optional<String> senhaGerada = service.gerarSenhaAleatoria(comando);
		if (senhaGerada.isPresent())
			return ResponseEntity.ok("Nova senha -> " + senhaGerada.get() + ": criada com sucesso");
		else
			throw new NullPointerException("Usuário não encontrado");
	}

}
