package br.hela.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import br.hela.usuario.Usuario;
import br.hela.usuario.UsuarioService;
import br.hela.usuario.comandos.CriarUsuario;
import br.hela.usuario.comandos.EditarUsuario;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUsuario {
	
	@Autowired
	private UsuarioService service;
	
	private RestTemplate template = new RestTemplate();

	@Test
	public void testPostUsuario() {
		CriarUsuario user = usuario();
		String url = "http://localhost:9090/usuarios";
		ResponseEntity<String> response = template.postForEntity(url, user, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		service.deletarTodos();
	}
	
	@Test
	public void testGetAllUsuario() {
		CriarUsuario user = usuario();
		String url = "http://localhost:9090/usuarios";
		service.salvar(user);
		ResponseEntity<String> response = template.getForEntity(url, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		service.deletarTodos();
	}
	
	@Test
	public void testGetOneUsuario() {
		CriarUsuario user = usuario();
		service.salvar(user);
		String id = service.encontrar().get().get(0).getId().toString();
		String url = "http://localhost:9090/usuarios/" + id;
		ResponseEntity<String> response = template.getForEntity(url, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		service.deletarTodos();
	}
	
//	@Test
//	public void testPutUsuario() {
//		CriarUsuario user = usuario();
//		service.salvar(user);
//		Usuario userSalvo = service.encontrar().get().get(0);
//		
//		
//		
//		
//		user.setEmail("email2");
//		user.setNome_usuario("nome2");
//		String url = "http://localhost:9090/usuarios";
//		template.put(url, user);
//		List<Usuario> userList = service.encontrar().get();
//		assertTrue(userList.contains(user));
//
//		service.deletarTodos();
//	}
	
	private CriarUsuario usuario() {
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
	
	private EditarUsuario usuarioEditar() {
		EditarUsuario userAlter = new EditarUsuario();
		userAlter.setData_nascimento(new Date());
		userAlter.setEndereco("endereco alterado");
		userAlter.setImagem_usuario("imagem_usuario alterado");
		userAlter.setNome_completo("nome_completo alterado");
		userAlter.setSenha("senha alterado");
		userAlter.setTelefone(11223344);
		userAlter.setTipo_sangue("tipo_sangue alterado");
		userAlter.setSexo("sexo alterado");
		return userAlter;
	}

}
