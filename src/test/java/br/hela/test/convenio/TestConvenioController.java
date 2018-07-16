package br.hela.test.convenio;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;

import br.hela.Escoladeti2018Application;
import br.hela.convenio.Convenio;
import br.hela.convenio.ConvenioRepository;
import br.hela.convenio.comandos.CriarConvenio;
import br.hela.convenio.comandos.EditarConvenio;
import br.hela.login.LoginController;
import br.hela.login.comandos.LogarAdm;
import br.hela.usuario_adm.UsuarioAdm;
import br.hela.usuario_adm.UsuarioAdmRepository;
import br.hela.usuario_adm.UsuarioAdmService;
import br.hela.usuario_adm.comandos.CriarUsuarioAdm;

@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { Escoladeti2018Application.class }, webEnvironment = WebEnvironment.MOCK)

public class TestConvenioController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private LoginController login;

	@Autowired
	private ConvenioRepository repoConvenio;

	@Autowired
	private UsuarioAdmService admService;

	@Autowired
	private UsuarioAdmRepository repoAdm;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		admService.salvar(criarAdm());

		List<UsuarioAdm> usuarios = repoAdm.findAll();
		assertThat(usuarios.get(0), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarConvenio("Unimed"));

		this.mockMvc
				.perform(post("/convenios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi cadastrado com sucesso")))
				.andExpect(status().isCreated());
	}

	@Test
	public void testEditar() throws Exception {
		admService.salvar(criarAdm());

		List<UsuarioAdm> usuarios = repoAdm.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarConvenio("Unimed"));

		this.mockMvc
				.perform(post("/convenios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Convenio> convenios = repoConvenio.findAll();
		assertThat(convenios.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarConvenio(convenios.get(0)));

		this.mockMvc
				.perform(put("/convenios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi alterado com sucesso"))).andExpect(status().isOk());

	}

	@Test
	public void testBuscarPorId() throws Exception {
		admService.salvar(criarAdm());

		List<UsuarioAdm> usuarios = repoAdm.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarConvenio("Unimed"));

		this.mockMvc
				.perform(post("/convenios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Convenio> convenios = repoConvenio.findAll();
		assertThat(convenios.get(0), notNullValue());

		this.mockMvc
				.perform(get("/convenios/" + convenios.get(0).getId().toString()).header("token",
						logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.nome", equalTo("Unimed"))).andExpect(status().isOk());
	}

	@Test
	public void testBurcarTodos() throws Exception {
		admService.salvar(criarAdm());

		List<UsuarioAdm> usuarios = repoAdm.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarConvenio("Unimed"));

		this.mockMvc
				.perform(post("/convenios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarConvenio("Santa Casa"));

		this.mockMvc
				.perform(post("/convenios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Convenio> convenios = repoConvenio.findAll();
		assertThat(convenios.get(0), notNullValue());

		this.mockMvc.perform(get("/convenios").header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$[0].nome", equalTo("Unimed")))
				.andExpect(jsonPath("$[1].nome", equalTo("Santa Casa"))).andExpect(status().isOk());

	}

	@Test
	public void testDeletar() throws Exception {
		admService.salvar(criarAdm());

		List<UsuarioAdm> usuarios = repoAdm.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarConvenio("Unimed"));

		this.mockMvc
				.perform(post("/convenios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Convenio> convenios = repoConvenio.findAll();
		assertThat(convenios.get(0), notNullValue());

		this.mockMvc
				.perform(delete("/convenios/" + convenios.get(0).getId().toString()).header("token",
						logarAdm("admin", "1234")))
				.andExpect(jsonPath("$",
						equalTo("Convênio ===> " + convenios.get(0).getId().toString() + ": deletado com sucesso")))
				.andExpect(status().isOk());
	}

	private CriarConvenio criarConvenio(String nome) {
		CriarConvenio convenio = new CriarConvenio();
		convenio.setNome(nome);
		return convenio;
	}

	private EditarConvenio editarConvenio(Convenio convenio) {
		EditarConvenio convenioEditado = new EditarConvenio();
		convenioEditado.setId(convenio.getId());
		convenioEditado.setNome("Santa Casa");
		convenioEditado.setAtivo(convenio.getAtivo());
		return convenioEditado;
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

}
