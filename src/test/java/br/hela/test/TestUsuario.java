<<<<<<< HEAD
/*package br.hela.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
	public void testPost() {
		CriarUsuario user = criarNovoUsuario();
		String url = "http://localhost:9090/usuarios";
		ResponseEntity<String> response = template.postForEntity(url, user, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		service.deletarTodos();
	}

	@Test
	public void testGetAll() {
		CriarUsuario user = criarNovoUsuario();
		String url = "http://localhost:9090/usuarios";
		ResponseEntity<String> response = template.postForEntity(url, user, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		ResponseEntity<String> responseGetUser = template.getForEntity(url, String.class);
		assertEquals(HttpStatus.OK, responseGetUser.getStatusCode());
		service.deletarTodos();
	}

	@Test
	public void testGetOne() {
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

	@Test
	public void testPut() {
		CriarUsuario user = criarNovoUsuario();
		String url = "http://localhost:9090/usuarios";
		ResponseEntity<String> response = template.postForEntity(url, user, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		Usuario userSave = service.encontrar().get().get(0);
		EditarUsuario userAlter = alterarUsuario(userSave);
		template.put(url, userAlter);
		List<Usuario> userList = service.encontrar().get();
		assertEquals(1, userList.size());
		assertFalse(userList.contains(userSave));
		service.deletarTodos();
	}

	@Test
	public void testDelete() {
		CriarUsuario user = criarNovoUsuario();
		String url = "http://localhost:9090/usuarios";
		ResponseEntity<String> response = template.postForEntity(url, user, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		Usuario userSave = service.encontrar().get().get(0);
		url = "http://localhost:9090/usuarios/" + userSave.getId().toString();
		template.delete(url);
		List<Usuario> userList = service.encontrar().get();
		assertEquals(0, service.encontrar().get().size());
		assertFalse(userList.contains(userSave));
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

	private EditarUsuario alterarUsuario(Usuario comando) {
		EditarUsuario user = new EditarUsuario();
		user.setId(comando.getId());
		user.setData_nascimento(comando.getData_nascimento());
		user.setEndereco("endereco alterado");
		user.setImagem_usuario("imagem_usuario alterado");
		user.setNome_completo(comando.getNome_completo());
		user.setSenha(comando.getSenha());
		user.setTelefone(55667788);
		user.setTipo_sangue(comando.getTipo_sangue());
		user.setSexo(comando.getSexo());
		return user;
	}
}
=======
/*//package br.hela.test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//
//import java.util.Date;
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.client.RestTemplate;
//
//import br.hela.usuario.Usuario;
//import br.hela.usuario.UsuarioService;
//import br.hela.usuario.comandos.CriarUsuario;
//import br.hela.usuario.comandos.EditarUsuario;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class TestUsuario {
//
//	@Autowired
//	private UsuarioService service;
//
//	private RestTemplate template = new RestTemplate();
//
//	@Test
//	public void testPost() {
//		CriarUsuario user = criarNovoUsuario();
//		String url = "http://localhost:9090/usuarios";
//		ResponseEntity<String> response = template.postForEntity(url, user, String.class);
//		assertEquals(HttpStatus.CREATED, response.getStatusCode());
//		service.deletarTodos();
//	}
//
//	@Test
//	public void testGetAll() {
//		CriarUsuario user = criarNovoUsuario();
//		String url = "http://localhost:9090/usuarios";
//		ResponseEntity<String> response = template.postForEntity(url, user, String.class);
//		assertEquals(HttpStatus.CREATED, response.getStatusCode());
//		ResponseEntity<String> responseGetUser = template.getForEntity(url, String.class);
//		assertEquals(HttpStatus.OK, responseGetUser.getStatusCode());
//		service.deletarTodos();
//	}
//
//	@Test
//	public void testGetOne() {
//		CriarUsuario user = criarNovoUsuario();
//		String url = "http://localhost:9090/usuarios";
//		ResponseEntity<String> response = template.postForEntity(url, user, String.class);
//		assertEquals(HttpStatus.CREATED, response.getStatusCode());
//		String id = service.encontrar().get().get(0).getId().toString();
//		url = "http://localhost:9090/usuarios/" + id;
//		ResponseEntity<String> responseGetOne = template.getForEntity(url, String.class);
//		assertEquals(HttpStatus.OK, responseGetOne.getStatusCode());
//		service.deletarTodos();
//	}
//
//	@Test
//	public void testPut() {
//		CriarUsuario user = criarNovoUsuario();
//		String url = "http://localhost:9090/usuarios";
//		ResponseEntity<String> response = template.postForEntity(url, user, String.class);
//		assertEquals(HttpStatus.CREATED, response.getStatusCode());
//		Usuario userSave = service.encontrar().get().get(0);
//		EditarUsuario userAlter = alterarUsuario(userSave);
//		template.put(url, userAlter);
//		List<Usuario> userList = service.encontrar().get();
//		assertEquals(1, userList.size());
//		assertFalse(userList.contains(userSave));
//		service.deletarTodos();
//	}
//
//	@Test
//	public void testDelete() {
//		CriarUsuario user = criarNovoUsuario();
//		String url = "http://localhost:9090/usuarios";
//		ResponseEntity<String> response = template.postForEntity(url, user, String.class);
//		assertEquals(HttpStatus.CREATED, response.getStatusCode());
//		Usuario userSave = service.encontrar().get().get(0);
//		url = "http://localhost:9090/usuarios/" + userSave.getId().toString();
//		template.delete(url);
//		List<Usuario> userList = service.encontrar().get();
//		assertEquals(0, service.encontrar().get().size());
//		assertFalse(userList.contains(userSave));
//	}
//
//	private CriarUsuario criarNovoUsuario() {
//		CriarUsuario user = new CriarUsuario();
//		user.setData_nascimento(new Date());
//		user.setEmail("email1");
//		user.setEndereco("endereco");
//		user.setImagem_usuario("imagem_usuario");
//		user.setNome_completo("nome_completo");
//		user.setSenha("senha");
//		user.setNome_usuario("nome_usuario1");
//		user.setTelefone(11223344);
//		user.setTipo_sangue("tipo_sangue");
//		user.setSexo("sexo");
//		return user;
//	}
//
//	private EditarUsuario alterarUsuario(Usuario comando) {
//		EditarUsuario user = new EditarUsuario();
//		user.setId(comando.getId());
//		user.setData_nascimento(comando.getData_nascimento());
//		user.setEndereco("endereco alterado");
//		user.setImagem_usuario("imagem_usuario alterado");
//		user.setNome_completo(comando.getNome_completo());
//		user.setSenha(comando.getSenha());
//		user.setTelefone(55667788);
//		user.setTipo_sangue(comando.getTipo_sangue());
//		user.setSexo(comando.getSexo());
//		return user;
//	}
//}
>>>>>>> 913e5d20409c6b0add3d888369297742a09a5314
*/