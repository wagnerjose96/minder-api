package br.hela.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.hela.login.comandos.LogarUsuario;
import br.hela.security.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic Login Controller")
@RestController
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private LoginService service;
	
	@ApiOperation(value = "Efetue o login de um usuário pelo nome de usuário")
	@PostMapping
	public ResponseEntity<String> loginPorNomeDeUsuario(@RequestBody LogarUsuario comando) 
			throws NullPointerException {
		String username = comando.getIdentificador();
		if(username.indexOf("@") > 0 && username.indexOf(".com") > -1 && username.indexOf("@.com") == -1){ 
			if (service.consultarEmail(comando)) {
				String token = JWTUtil.create(comando.getIdentificador());				
				return ResponseEntity.ok().body(token);
			}
			throw new NullPointerException("Login não realizado! Favor conferir os dados digitados");
		} else {
			if (service.consultarUsuario(comando)) {
				String token = JWTUtil.create(comando.getIdentificador());
				return ResponseEntity.ok().body(token);
			}
			throw new NullPointerException("Login não realizado! Favor conferir os dados digitados");
		}
	}
}
