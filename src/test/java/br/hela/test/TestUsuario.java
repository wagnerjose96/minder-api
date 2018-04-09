package br.hela.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import br.hela.usuario.UsuarioService;
import br.hela.usuario.comandos.CriarUsuario;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUsuario {

	@Autowired
	private UsuarioService service;

	private RestTemplate template = new RestTemplate();

	@Test
	public void testPostUsuario() {

		CriarUsuario user = criarNovoUsuario();
		String url = "http://localhost:9090/usuarios";
		ResponseEntity<String> response = template.postForEntity(url, user, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		service.deletarTodos();
	}

	@Test
	public void testGetAllUsuario() {
		CriarUsuario user = criarNovoUsuario();
		String url = "http://localhost:9090/usuarios";
		ResponseEntity<String> response = template.postForEntity(url, user, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		ResponseEntity<String> responseGetUser = template.getForEntity(url, String.class);
		assertEquals(HttpStatus.OK, responseGetUser.getStatusCode());
		service.deletarTodos();
	}

	@Test
	public void testGetOneUsuario() {
		CriarUsuario user = criarNovoUsuario();
		String url = "http://localhost:9090/usuarios";
		ResponseEntity<String> response = template.postForEntity(url, user, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		String id = service.encontrar().get().get(0).getId().toString();
		url = "http://localhost:9090/usuarios/" + id;
		ResponseEntity<String> responseGetOne = template.getForEntity(url, String.class);
		assertEquals(HttpStatus.OK, responseGetOne.getStatusCode());
		service.deletarTodos();
	}

	private CriarUsuario criarNovoUsuario() {
		CriarUsuario user = new CriarUsuario();
		user.setData_nascimento(new Date());
		user.setEmail("email1");
		user.setEndereco("endereco");
		user.setImagem_usuario("imagem_usuario");
		user.setNome_completo("nome_completo");
		user.setSenha("senha");
		user.setNome_usuario("nome_usuario1");
		user.setTelefone(11223344);
		user.setTipo_sangue("tipo_sangue");
		user.setSexo("sexo");
		return user;
	}
}
