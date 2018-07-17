package br.minder.test.sexo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import br.minder.login.LoginController;
import br.minder.login.comandos.LogarAdm;
import br.minder.sexo.Sexo;
import br.minder.sexo.SexoRepository;
import br.minder.sexo.comandos.CriarSexo;
import br.minder.sexo.comandos.EditarSexo;
import br.minder.usuario_adm.UsuarioAdm;
import br.minder.usuario_adm.UsuarioAdmRepository;
import br.minder.usuario_adm.UsuarioAdmService;
import br.minder.usuario_adm.comandos.CriarUsuarioAdm;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
public class TestSexoController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private SexoRepository repo;

	@Autowired
	private LoginController login;

	@Autowired
	private UsuarioAdmRepository repoAdm;

	@Autowired
	private UsuarioAdmService admService;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarSexo("Masculino"));

		this.mockMvc
				.perform(post("/generos").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O genêro foi cadastrado com sucesso")))
				.andExpect(status().isCreated());
	}

	@Test
	public void testEditar() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarSexo("Masculino"));

		this.mockMvc
				.perform(post("/generos").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O genêro foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Sexo> sexos = repo.findAll();
		assertThat(sexos.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarSexo(sexos.get(0)));

		this.mockMvc
				.perform(put("/generos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O genêro foi alterado com sucesso"))).andExpect(status().isOk());
	}

	@Test
	public void testBuscarTodos() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarSexo("Masculino"));

		this.mockMvc
				.perform(post("/generos").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O genêro foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarSexo("Feminino"));

		this.mockMvc
				.perform(post("/generos").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O genêro foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Sexo> sexos = repo.findAll();
		assertThat(sexos.get(0), notNullValue());
		assertThat(sexos.get(1), notNullValue());

		this.mockMvc.perform(get("/generos").header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$[0].genero", equalTo("Masculino")))
				.andExpect(jsonPath("$[1].genero", equalTo("Feminino"))).andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarSexo("Masculino"));

		this.mockMvc
				.perform(post("/generos").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O genêro foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Sexo> sexos = repo.findAll();
		assertThat(sexos.get(0), notNullValue());

		this.mockMvc.perform(get("/generos/" + sexos.get(0).getIdGenero().toString()))
				.andExpect(jsonPath("$.genero", equalTo("Masculino"))).andExpect(status().isOk());
	}

	private CriarUsuarioAdm criarAdm() {
		CriarUsuarioAdm adm = new CriarUsuarioAdm();
		adm.setNome("admin");
		adm.setSenha("1234");
		return adm;
	}

	private String logarAdm(String nomeUsuario, String senha) {
		LogarAdm corpoLogin = new LogarAdm();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginAdm(corpoLogin).getBody();
	}

	private CriarSexo criarSexo(String tipo) {
		return new CriarSexo(tipo);
	}

	private EditarSexo editarSexo(Sexo sexo) {
		EditarSexo sexoAtualizado = new EditarSexo();
		sexoAtualizado.setId(sexo.getIdGenero());
		sexoAtualizado.setGenero("Outros");
		return sexoAtualizado;
	}

}
