package br.hela.esqueciSenha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.hela.esqueciSenha.comandos.GerarSenha;
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
	public String putUSenha(@RequestBody GerarSenha comando) {
		String senhaGerada = service.gerarSenhaAleatoria(comando);
		if (senhaGerada != null)
			return "Nova senha -> " + senhaGerada + ": criada com sucesso";
		else
			throw new NullPointerException("Usuário não encontrado");
	}

}
