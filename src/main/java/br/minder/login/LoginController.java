package br.minder.login;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.minder.login.comandos.GerarToken;
import br.minder.login.comandos.LogarUsuario;
import br.minder.security.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Login Controller")
@RestController
@RequestMapping
@CrossOrigin
public class LoginController {
	private static final String LOGINRECUSADO = "Login não realizado! Favor conferir os dados digitados";
	private static final String ERROTOKEN = "Usuário inválido! Favor conferir os dados digitados";

	@Autowired
	private LoginService service;

	@ApiOperation("Efetue o login de um usuário")
	@PostMapping("/api/login")
	public ResponseEntity<String> loginUsuario(@RequestBody LogarUsuario comando) throws NoSuchAlgorithmException {
		String username = comando.getIdentificador();
		if (username.indexOf('@') > -1 && username.indexOf(".com") > -1 && username.indexOf("@.com") == -1) {
			if (service.consultarEmail(comando)) {
				String token = JWTUtil.create(comando.getIdentificador());
				return ResponseEntity.ok().body(token);
			}
			throw new NullPointerException(LOGINRECUSADO);
		} else {
			if (service.consultarUsuario(comando)) {
				String token = JWTUtil.create(comando.getIdentificador());
				return ResponseEntity.ok().body(token);
			}
			throw new NullPointerException(LOGINRECUSADO);
		}
	}

	@ApiOperation("Gerar código de acesso de um usuário")
	@PostMapping("/api/token")
	public ResponseEntity<String> gerarToken(@RequestBody GerarToken comando) {
		String username = comando.getIdentificador();
		if (username.indexOf('@') > -1 && username.indexOf(".com") > -1 && username.indexOf("@.com") == -1) {
			if (service.consultarEmail(comando.getIdentificador())) {
				String token = JWTUtil.create(comando.getIdentificador());
				return ResponseEntity.ok().body(token);
			}
			throw new NullPointerException(ERROTOKEN);
		} else {
			if (service.consultarUsuario(comando.getIdentificador())) {
				String token = JWTUtil.create(comando.getIdentificador());
				return ResponseEntity.ok().body(token);
			}
			throw new NullPointerException(ERROTOKEN);
		}
	}

	@ApiOperation("Efetue o login de um administrador")
	@PostMapping("/api/loginAdm")
	public ResponseEntity<String> loginAdm(@RequestBody LogarUsuario comando) throws NoSuchAlgorithmException {
		if (service.consultarAdm(comando)) {
			String token = JWTUtil.create(comando.getIdentificador());
			return ResponseEntity.ok().body(token);
		}
		throw new NullPointerException(LOGINRECUSADO);
	}
}
