package br.minder.esqueci_senha;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.minder.esqueci_senha.comandos.EditarSenha;
import br.minder.esqueci_senha.comandos.EsqueciSenha;
import br.minder.security.Autentica;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Senha Controller")
@RestController
@RequestMapping("/api/senha")
@CrossOrigin
public class SenhaController {
	@Autowired
	private SenhaService service;

	@Autowired
	private Autentica autentica;

	@ApiOperation(value = "Crie uma senha aleatória para um usuário")
	@PutMapping
	public ResponseEntity<String> putUSenha(@RequestBody EsqueciSenha comando)
			throws MessagingException, UnsupportedEncodingException, NoSuchAlgorithmException {
		if (service.gerarSenhaAleatoria(comando))
			return ResponseEntity.ok("Nova senha criada com sucesso, verifique a caixa de entrada do seu email");
		else
			throw new NullPointerException("Usuário não encontrado");
	}

	@ApiOperation(value = "Edite a senha de um usuário")
	@PostMapping("/alterar")
	public ResponseEntity<String> putSenha(@RequestBody EditarSenha comando, @RequestHeader String token)
			throws AccessDeniedException, NoSuchAlgorithmException, SQLException {
		if (autentica.autenticaRequisicao(token)) {
			if (service.alterarSenha(comando, autentica.idUser(token)))
				return ResponseEntity.ok("Senha alterada com sucesso");
			else
				throw new SQLException("Senha não foi alterada devido a um erro interno");
		}
		throw new AccessDeniedException("Acesso negado");
	}

}