package br.minder.test.usuario;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.minder.MinderApplication;
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.endereco.comandos.EditarEndereco;
import br.minder.login.LoginController;
import br.minder.login.comandos.LogarUsuario;
import br.minder.sangue.Sangue;
import br.minder.sangue.SangueId;
import br.minder.sangue.SangueRepository;
import br.minder.sangue.comandos.CriarSangue;
import br.minder.sexo.Sexo;
import br.minder.sexo.SexoId;
import br.minder.sexo.SexoRepository;
import br.minder.sexo.comandos.CriarSexo;
import br.minder.telefone.comandos.CriarTelefone;
import br.minder.telefone.comandos.EditarTelefone;
import br.minder.usuario.Usuario;
import br.minder.usuario.UsuarioId;
import br.minder.usuario.UsuarioRepository;
import br.minder.usuario.comandos.CriarUsuario;
import br.minder.usuario.comandos.EditarUsuario;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
public class TestUsuarioController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private SangueRepository repoSangue;

	@Autowired
	private SexoRepository repoSexo;

	@Autowired
	private LoginController login;

	@Autowired
	private UsuarioRepository repo;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		SexoId idSexo = criarSexo("Masculino");
		SangueId idSangue = criarSangue("A+");

		String jsonString = objectMapper
				.writeValueAsString(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue));

		this.mockMvc
				.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O usuário foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper
				.writeValueAsString(criarUsuarioErro("wagner@hotmail.com", "wagnerju", idSexo, idSangue));

		this.mockMvc
				.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O usuário não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testEditar() throws Exception {
		SexoId idSexo = criarSexo("Masculino");
		SangueId idSangue = criarSangue("A+");

		String jsonString = objectMapper
				.writeValueAsString(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue));

		this.mockMvc
				.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O usuário foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarUsuario(usuarios.get(0)));

		this.mockMvc
				.perform(put("/usuarios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "erroToken").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(put("/usuarios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O usuário foi alterado com sucesso"))).andExpect(status().isOk());

		jsonString = objectMapper.writeValueAsString(editarUsuarioErroId(usuarios.get(0)));

		this.mockMvc
				.perform(put("/usuarios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O usuário a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarUsuarioErro1(usuarios.get(0)));

		this.mockMvc
				.perform(put("/usuarios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do usuário")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testBuscarTodos() throws Exception {
		SexoId idSexo = criarSexo("Masculino");
		SangueId idSangue = criarSangue("A+");

		String jsonString = objectMapper
				.writeValueAsString(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue));

		this.mockMvc
				.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O usuário foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper
				.writeValueAsString(criarUsuario("lathuanny@hotmail.com", "lathuanny", idSexo, idSangue));

		this.mockMvc
				.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O usuário foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());
		assertThat(usuarios.get(1), notNullValue());

		String token = logar("lathuanny", "1234");

		this.mockMvc
				.perform(delete("/usuarios").header("token",
						logar("lathuanny", "1234")))
				.andExpect(jsonPath("$",
						equalTo("Usuário ===> " + usuarios.get(1).getId().toString() + ": deletado com sucesso")))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/usuarios").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.username", equalTo("wagnerju"))).andExpect(status().isOk());

		this.mockMvc.perform(get("/usuarios").header("token", logar("wagnerju", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc.perform(get("/usuarios").header("token", token))
				.andExpect(jsonPath("$.error", equalTo("O usuário procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testDeletar() throws Exception {
		SexoId idSexo = criarSexo("Masculino");
		SangueId idSangue = criarSangue("A+");

		String jsonString = objectMapper
				.writeValueAsString(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue));

		this.mockMvc
				.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O usuário foi cadastrado com sucesso")))
				.andExpect(status().isCreated());
		
		jsonString = objectMapper
				.writeValueAsString(criarUsuario("lathuanny@hotmail.com", "lathuanny", idSexo, idSangue));

		this.mockMvc
				.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O usuário foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());
		
		String token = logar("lathuanny", "1234");

		this.mockMvc
				.perform(delete("/usuarios").header("token",
						logar("lathuanny", "1234")))
				.andExpect(jsonPath("$",
						equalTo("Usuário ===> " + usuarios.get(1).getId().toString() + ": deletado com sucesso")))
				.andExpect(status().isOk());

		this.mockMvc
				.perform(delete("/usuarios").header("token",
						logar("wagnerju", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(delete("/usuarios").header("token", token))
				.andExpect(jsonPath("$.error", equalTo("O usuário a deletar não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(delete("/usuarios").header("token",
						logar("wagnerju", "1234")))
				.andExpect(jsonPath("$",
						equalTo("Usuário ===> " + usuarios.get(0).getId().toString() + ": deletado com sucesso")))
				.andExpect(status().isOk());
	}

	private CriarUsuario criarUsuario(String email, String username, SexoId idSexo, SangueId idSangue) {
		CriarEndereco endereco = new CriarEndereco();
		endereco.setBairro("Zona 6");
		endereco.setCidade("Maringá");
		endereco.setEstado("Paraná");
		endereco.setNumero("1390");
		endereco.setRua("Castro Alves");

		CriarTelefone telefone = new CriarTelefone();
		telefone.setDdd(44);
		telefone.setNumero(999038860);

		CriarUsuario usuario = new CriarUsuario();
		usuario.setNome("Wagner José");
		usuario.setEmail(email);
		usuario.setDataNascimento(Date.valueOf(LocalDate.of(1997, 03, 17)));
		usuario.setEndereco(endereco);
		usuario.setIdSangue(idSangue);
		usuario.setIdSexo(idSexo);
		usuario.setSenha("1234");
		usuario.setTelefone(telefone);
		usuario.setUsername(username);

		return usuario;
	}

	private CriarUsuario criarUsuarioErro(String email, String username, SexoId idSexo, SangueId idSangue) {
		CriarEndereco endereco = new CriarEndereco();
		endereco.setBairro("Zona 6");
		endereco.setCidade("Maringá");
		endereco.setEstado("Paraná");
		endereco.setNumero("1390");
		endereco.setRua("Castro Alves");

		CriarTelefone telefone = new CriarTelefone();
		telefone.setDdd(44);
		telefone.setNumero(999038860);

		CriarUsuario usuario = new CriarUsuario();
		usuario.setEmail(email);
		usuario.setDataNascimento(Date.valueOf(LocalDate.of(1997, 03, 17)));
		usuario.setEndereco(endereco);
		usuario.setIdSangue(idSangue);
		usuario.setIdSexo(idSexo);
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
		endereco.setNumero("1390");
		endereco.setRua("Castro Alves");

		EditarTelefone telefone = new EditarTelefone();
		telefone.setId(usuario.getIdTelefone());
		telefone.setDdd(44);
		telefone.setNumero(999038860);

		EditarUsuario usuarioEditado = new EditarUsuario();
		usuarioEditado.setId(usuario.getId());
		usuarioEditado.setSenha(usuario.getSenha());
		usuarioEditado.setNome("Wagner Junior");
		usuarioEditado.setEndereco(endereco);
		usuarioEditado.setTelefone(telefone);
		usuarioEditado.setImagem(usuario.getImagemUsuario());

		return usuarioEditado;
	}

	private EditarUsuario editarUsuarioErroId(Usuario usuario) {
		EditarEndereco endereco = new EditarEndereco();
		endereco.setId(usuario.getIdEndereco());
		endereco.setBairro("Zona 6");
		endereco.setCidade("Maringá");
		endereco.setEstado("Paraná");
		endereco.setNumero("1390");
		endereco.setRua("Castro Alves");

		EditarTelefone telefone = new EditarTelefone();
		telefone.setId(usuario.getIdTelefone());
		telefone.setDdd(44);
		telefone.setNumero(999038860);

		EditarUsuario usuarioEditado = new EditarUsuario();
		usuarioEditado.setId(new UsuarioId());
		usuarioEditado.setSenha(usuario.getSenha());
		usuarioEditado.setNome("Wagner Junior");
		usuarioEditado.setEndereco(endereco);
		usuarioEditado.setTelefone(telefone);
		usuarioEditado.setImagem(usuario.getImagemUsuario());

		return usuarioEditado;
	}

	private EditarUsuario editarUsuarioErro1(Usuario usuario) {
		EditarEndereco endereco = new EditarEndereco();
		endereco.setId(usuario.getIdEndereco());
		endereco.setBairro("Zona 6");
		endereco.setCidade("Maringá");
		endereco.setEstado("Paraná");
		endereco.setNumero("1390");
		endereco.setRua("Castro Alves");

		EditarTelefone telefone = new EditarTelefone();
		telefone.setId(usuario.getIdTelefone());
		telefone.setDdd(44);
		telefone.setNumero(999038860);

		EditarUsuario usuarioEditado = new EditarUsuario();
		usuarioEditado.setId(usuario.getId());
		usuarioEditado.setSenha(usuario.getSenha());
		usuarioEditado.setEndereco(endereco);
		usuarioEditado.setTelefone(telefone);
		usuarioEditado.setImagem(usuario.getImagemUsuario());

		return usuarioEditado;
	}

	private String logar(String nomeUsuario, String senha) {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginUsuario(corpoLogin).getBody();
	}

	private SangueId criarSangue(String tipo) {
		return repoSangue.save(new Sangue(new CriarSangue(tipo))).getIdSangue();
	}

	private SexoId criarSexo(String tipo) {
		return repoSexo.save(new Sexo(new CriarSexo(tipo))).getIdGenero();
	}

}