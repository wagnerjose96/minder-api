package br.hela.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.sql.Date;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.hela.Escoladeti2018Application;
import br.hela.endereco.EnderecoId;
import br.hela.endereco.comandos.CriarEndereco;
import br.hela.endereco.comandos.EditarEndereco;
import br.hela.login.LoginController;
import br.hela.login.comandos.LogarUsuario;
import br.hela.sangue.SangueId;
import br.hela.sexo.SexoId;
import br.hela.telefone.TelefoneId;
import br.hela.telefone.comandos.CriarTelefone;
import br.hela.telefone.comandos.EditarTelefone;
import br.hela.usuario.Usuario;
import br.hela.usuario.UsuarioId;
import br.hela.usuario.UsuarioRepository;
import br.hela.usuario.comandos.CriarUsuario;
import br.hela.usuario.comandos.EditarUsuario;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { Escoladeti2018Application.class }, webEnvironment = WebEnvironment.MOCK)

public class TestUsuarioController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private LoginController login;

	@Autowired
	private UsuarioRepository repo;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	private CriarUsuario criarUsuario(String email, String username) {
		CriarEndereco endereco = new CriarEndereco();
		endereco.setBairro("Zona 6");
		endereco.setCidade("Maringá");
		endereco.setEstado("Paraná");
		endereco.setNumero(1390);
		endereco.setRua("Castro Alves");

		CriarTelefone telefone = new CriarTelefone();
		telefone.setDdd(44);
		telefone.setNumero(999038860);

		CriarUsuario usuario = new CriarUsuario();
		usuario.setNome("Wagner José");
		usuario.setEmail(email);
		usuario.setDataNascimento(Date.valueOf(LocalDate.of(1997, 03, 17)));
		usuario.setEndereco(endereco);
		usuario.setIdSangue(new SangueId("23d52f28-043f-40c6-a399-dab9d2495e8b"));
		usuario.setIdSexo(new SexoId("a67df215-bbdf-420c-b5c3-62813302ffcd"));
		usuario.setSenha("1234");
		usuario.setTelefone(telefone);
		usuario.setUsername(username);

		return usuario;
	}

	private EditarUsuario editarUsuario(Usuario usuario) {
		EditarEndereco endereco = new EditarEndereco();
		endereco.setId(usuario.getIdEndereco());
		endereco.setBairro("Zona 6");
		endereco.setCidade("Maringá");
		endereco.setEstado("Paraná");
		endereco.setNumero(1390);
		endereco.setRua("Castro Alves");

		EditarTelefone telefone = new EditarTelefone();
		telefone.setId(usuario.getIdTelefone());
		telefone.setDdd(44);
		telefone.setNumero(999038860);
		
		EditarUsuario usuarioEditado = new EditarUsuario();
		usuarioEditado.setId(usuario.getId());
		usuarioEditado.setSenha(usuario.getSenha());
		usuarioEditado.setNome("Teste Put");
		usuarioEditado.setEndereco(endereco);
		usuarioEditado.setTelefone(telefone);
		usuarioEditado.setImagem(usuario.getImagemUsuario());

		return usuarioEditado;
	}

	@Test
	public void testCadastrar() throws Exception {
		Usuario usuario = new Usuario(criarUsuario("wagner@hotmail.com", "wagnerju"));
		usuario.setIdEndereco(new EnderecoId("1"));
		usuario.setIdTelefone(new TelefoneId("1"));
		repo.save(usuario);

		final String jsonString = objectMapper.writeValueAsString(criarUsuario("wagner@hotmail.com", "wagnerju"));

		this.mockMvc.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(jsonString)).andExpect(status().isCreated());
	}

	@Test
	public void testEditar() throws Exception {
		Usuario usuario = new Usuario(criarUsuario("wagner@hotmail.com", "wagnerju"));
		usuario.setIdEndereco(new EnderecoId("1"));
		usuario.setIdTelefone(new TelefoneId("1"));		
		repo.save(usuario);
		String login = logar("wagnerju", "1234");
		
		EditarUsuario usuarioAtualizado = editarUsuario(usuario);
		final String jsonString = objectMapper.writeValueAsString(usuarioAtualizado);

		this.mockMvc
				.perform(put("/usuarios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", login).content(jsonString)).andExpect(status().isOk());
		assertEquals(usuarioAtualizado.getNome(), "Teste Ṕut");
	}

	private String logar(String nomeUsuario, String senha) {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginUsuario(corpoLogin).toString();
	}

}