package br.hela.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.hela.login.comandos.LogarAdm;
import br.hela.login.comandos.LogarUsuario;
import br.hela.security.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Basic Login Controller")
@RestController
@RequestMapping
@CrossOrigin
public class LoginController {
	private static final String LOGINRECUSADO = "Login não realizado! Favor conferir os dados digitados";

	@Autowired
	private LoginService service;
	
	@ApiOperation("Efetue o login de um usuário")
	@PostMapping("/login")
	public ResponseEntity<String> loginUsuario(@RequestBody LogarUsuario comando) {
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
	
	@ApiOperation("Efetue o login de um administrador")
	@PostMapping("/loginAdm")
	public ResponseEntity<String> loginAdm(@RequestBody LogarAdm comando) {
		if (service.consultarUsuario(comando)) {
			String token = JWTUtil.create(comando.getUsername());
			return ResponseEntity.ok().body(token);
		}
		throw new NullPointerException(LOGINRECUSADO);
	}
}
