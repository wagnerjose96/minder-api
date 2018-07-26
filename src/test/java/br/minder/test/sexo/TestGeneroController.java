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
import br.minder.genero.Genero;
import br.minder.genero.GeneroId;
import br.minder.genero.GeneroRepository;
import br.minder.genero.comandos.CriarGenero;
import br.minder.genero.comandos.EditarGenero;
import br.minder.login.LoginController;
import br.minder.login.comandos.LogarUsuario;
import br.minder.usuario_adm.UsuarioAdm;
import br.minder.usuario_adm.UsuarioAdmRepository;
import br.minder.usuario_adm.UsuarioAdmService;
import br.minder.usuario_adm.comandos.CriarUsuarioAdm;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
public class TestGeneroController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private GeneroRepository repo;

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

		String jsonString = objectMapper.writeValueAsString(criarGenero("Masculino"));

		this.mockMvc
				.perform(post("/generos").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O genêro foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarGeneroErro());

		this.mockMvc
				.perform(post("/generos").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O genêro não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

	}

	@Test
	public void testEditar() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarGenero("Masculino"));

		this.mockMvc
				.perform(post("/generos").header("token", logarAdm("admin", "1234") + "erroToken")
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(post("/generos").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O genêro foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Genero> sexos = repo.findAll();
		assertThat(sexos.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarGenero(sexos.get(0)));

		this.mockMvc
				.perform(put("/generos").header("token", logarAdm("admin", "1234") + "erroToken")
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(put("/generos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O genêro foi alterado com sucesso"))).andExpect(status().isOk());

		jsonString = objectMapper.writeValueAsString(editarGeneroErroId(sexos.get(0)));

		this.mockMvc
				.perform(put("/generos").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O genêro a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarGeneroErro(sexos.get(0)));

		this.mockMvc
				.perform(put("/generos").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do genêro")))
				.andExpect(status().isInternalServerError());

	}

	@Test
	public void testBuscarTodos() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		this.mockMvc.perform(get("/generos"))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhum genêro cadastrado no banco de dados")))
				.andExpect(status().isNotFound());

		String jsonString = objectMapper.writeValueAsString(criarGenero("Masculino"));

		this.mockMvc
				.perform(post("/generos").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O genêro foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarGenero("Feminino"));

		this.mockMvc
				.perform(post("/generos").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O genêro foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Genero> sexos = repo.findAll();
		assertThat(sexos.get(0), notNullValue());
		assertThat(sexos.get(1), notNullValue());

		this.mockMvc.perform(get("/generos")).andExpect(jsonPath("$[0].genero", equalTo("Masculino")))
				.andExpect(jsonPath("$[1].genero", equalTo("Feminino"))).andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarGenero("Masculino"));

		this.mockMvc
				.perform(post("/generos").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O genêro foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Genero> sexos = repo.findAll();
		assertThat(sexos.get(0), notNullValue());

		this.mockMvc.perform(get("/generos/" + sexos.get(0).getIdGenero().toString()))
				.andExpect(jsonPath("$.genero", equalTo("Masculino"))).andExpect(status().isOk());

		this.mockMvc.perform(get("/generos/" + new GeneroId().toString()))
				.andExpect(jsonPath("$.error", equalTo("O genêro procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());

	}

	private CriarUsuarioAdm criarAdm() {
		CriarUsuarioAdm adm = new CriarUsuarioAdm();
		adm.setNome("admin");
		adm.setSenha("1234");
		return adm;
	}

	private String logarAdm(String nomeUsuario, String senha) {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginAdm(corpoLogin).getBody();
	}

	private CriarGenero criarGenero(String tipo) {
		return new CriarGenero(tipo);
	}

	private CriarGenero criarGeneroErro() {
		return new CriarGenero();
	}

	private EditarGenero editarGenero(Genero sexo) {
		EditarGenero sexoAtualizado = new EditarGenero();
		sexoAtualizado.setId(sexo.getIdGenero());
		sexoAtualizado.setGenero("Outros");
		return sexoAtualizado;
	}

	private EditarGenero editarGeneroErroId(Genero sexo) {
		EditarGenero sexoAtualizado = new EditarGenero();
		sexoAtualizado.setId(new GeneroId());
		sexoAtualizado.setGenero("Outros");
		return sexoAtualizado;
	}

	private EditarGenero editarGeneroErro(Genero sexo) {
		EditarGenero sexoAtualizado = new EditarGenero();
		sexoAtualizado.setId(sexo.getIdGenero());
		return sexoAtualizado;
	}

}
