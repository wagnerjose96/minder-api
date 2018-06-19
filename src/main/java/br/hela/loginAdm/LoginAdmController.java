package br.hela.loginAdm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.hela.loginAdm.comandos.LogarAdm;
import br.hela.security.JWTUtil;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/loginAdm")
public class LoginAdmController {

	@Autowired
	private LoginAdmService service;

	@ApiOperation("Efetue o login de um administrador")
	@PostMapping
	public ResponseEntity<String> loginPorNomeDeUsuario(@RequestBody LogarAdm comando) throws NullPointerException {
		if (service.consultarUsuario(comando)) {
			String token = JWTUtil.create(comando.getUsername());
			return ResponseEntity.ok().body(token);
		}
		throw new NullPointerException("Login não realizado! Favor conferir os dados digitados");
	}
}
