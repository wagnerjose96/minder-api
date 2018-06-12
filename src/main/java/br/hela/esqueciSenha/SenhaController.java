package br.hela.esqueciSenha;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.hela.esqueciSenha.comandos.GerarSenha;
import br.hela.security.AutenticaRequisicao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Senha Controller")
@RestController
@RequestMapping("/senha")
public class SenhaController {
	@Autowired
	private SenhaService service;
	
	@Autowired
	private AutenticaRequisicao autentica;

	@ApiOperation(value = "Crie uma senha aleatória para um usuário")
	@PutMapping("/senha")
	public String putUSenha(@RequestBody GerarSenha comando, @RequestHeader String token)
			throws NullPointerException, AccessDeniedException {
		if (autentica.autenticaRequisicao(token)) {
			if (service.gerarSenhaAleatoria(comando))
				return "Nova senha criada com sucesso";
			else
				throw new NullPointerException("Usuário não encontrado");
		}
		throw new AccessDeniedException("Acesso negado");
	}

}
