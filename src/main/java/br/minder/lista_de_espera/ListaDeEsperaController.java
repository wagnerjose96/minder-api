package br.minder.lista_de_espera;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/api/listaDeEspera")
@CrossOrigin
public class ListaDeEsperaController {
	@Autowired
	private ListaDeEsperaService service;

	@PostMapping
	public boolean postEmail(@RequestBody String email) throws UnsupportedEncodingException, MessagingException {
		return service.sendEmail(email);
	}

}