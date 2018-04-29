package br.hela.contatoEmergencia;

import java.net.URI;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.hela.contatoEmergencia.comandos.CriarContatoEmergencia;
import br.hela.contatoEmergencia.contato_emergencia_telefone.Contato_emergencia_telefone_Service;
import br.hela.telefone.TelefoneId;
import br.hela.telefone.TelefoneService;
import br.hela.telefone.comandos.CriarTelefone;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "Basic ContatoEmergencia Controller")
@RestController
@RequestMapping("/contatos")
public class ContatoEmergenciaController {
	@Autowired
	private ContatoEmergenciaService contatoEmergenciaService;
	@Autowired
	private TelefoneService service;
	@Autowired
	private Contato_emergencia_telefone_Service serv;

	@ApiOperation(value = "Cadastre um novo contato de emergência")
	@PostMapping
	public ResponseEntity<String> postContatoEmergencia(@RequestBody CriarContatoEmergencia comando) throws Exception {
		
		Optional<TelefoneId> idTelefone = service.salvar(comando.getTelefone());
		Optional<ContatoEmergenciaId> optionalContatoEmergenciaId = contatoEmergenciaService.salvar(comando, idTelefone);
		serv.salvar(idTelefone.get(), optionalContatoEmergenciaId.get());
		
		if (optionalContatoEmergenciaId.isPresent()) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(optionalContatoEmergenciaId.get()).toUri();
			return ResponseEntity.created(location).body("Contato de emergência cadastrado com sucesso");
		}
		throw new Exception("O contato de emergência não foi salvo devido a um erro interno");
	}

}