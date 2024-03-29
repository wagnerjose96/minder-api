package br.minder.test.convenio;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
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
import br.minder.MinderApplication;
import br.minder.convenio.Convenio;
import br.minder.convenio.ConvenioId;
import br.minder.convenio.ConvenioRepository;
import br.minder.convenio.comandos.CriarConvenio;
import br.minder.convenio.comandos.EditarConvenio;
import br.minder.login.LoginController;
import br.minder.login.comandos.LogarUsuario;
import br.minder.usuario_adm.UsuarioAdm;
import br.minder.usuario_adm.UsuarioAdmRepository;
import br.minder.usuario_adm.UsuarioAdmService;
import br.minder.usuario_adm.comandos.CriarUsuarioAdm;

@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
@ActiveProfiles("application-test")
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
				.perform(post("/api/convenio").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		this.mockMvc
				.perform(post("/api/convenio").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234") + "TokenEror").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		String error = objectMapper.writeValueAsString(new CriarConvenio());

		this.mockMvc
				.perform(post("/api/convenio").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("O convênio não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testEditar() throws Exception {
		admService.salvar(criarAdm());

		List<UsuarioAdm> usuarios = repoAdm.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarConvenio("Unimed"));

		this.mockMvc
				.perform(post("/api/convenio").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Convenio> convenios = repoConvenio.findAll();
		assertThat(convenios.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarConvenio(convenios.get(0)));

		this.mockMvc
				.perform(put("/api/convenio").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi alterado com sucesso"))).andExpect(status().isOk());

		this.mockMvc
				.perform(put("/api/convenio").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234") + "TokenError").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		String error = objectMapper.writeValueAsString(editarConvenioError());

		this.mockMvc
				.perform(put("/api/convenio").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("O convênio a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		error = objectMapper.writeValueAsString(editarConvenioError(convenios.get(0).getId()));

		this.mockMvc
				.perform(put("/api/convenio").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do convênio")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		admService.salvar(criarAdm());

		List<UsuarioAdm> usuarios = repoAdm.findAll();
		assertThat(usuarios.get(0), notNullValue());

		this.mockMvc.perform(get("/api/convenio/" + new ConvenioId().toString()))
				.andExpect(jsonPath("$.error", equalTo("O convênio procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		String jsonString = objectMapper.writeValueAsString(criarConvenio("Unimed"));

		this.mockMvc
				.perform(post("/api/convenio").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Convenio> convenios = repoConvenio.findAll();
		assertThat(convenios.get(0), notNullValue());

		this.mockMvc.perform(get("/api/convenio/" + convenios.get(0).getId().toString()))
				.andExpect(jsonPath("$.nome", equalTo("Unimed"))).andExpect(status().isOk());
	}

	@Test
	public void testBurcarTodos() throws Exception {
		admService.salvar(criarAdm());

		List<UsuarioAdm> usuarios = repoAdm.findAll();
		assertThat(usuarios.get(0), notNullValue());

		this.mockMvc.perform(get("/api/convenio"))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhum convênio cadastrado no banco de dados")))
				.andExpect(status().isNotFound());

		String jsonString = objectMapper.writeValueAsString(criarConvenio("Unimed"));

		this.mockMvc
				.perform(post("/api/convenio").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarConvenio("Santa Casa"));

		this.mockMvc
				.perform(post("/api/convenio").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Convenio> convenios = repoConvenio.findAll();
		assertThat(convenios.get(0), notNullValue());

		this.mockMvc.perform(get("/api/convenio")).andExpect(status().isOk());
		
		this.mockMvc.perform(get("/api/convenio").param("searchTerm", "Santa Casa")).andExpect(status().isOk());

	}

	@Test
	public void testDeletar() throws Exception {
		admService.salvar(criarAdm());

		List<UsuarioAdm> usuarios = repoAdm.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarConvenio("Unimed"));

		this.mockMvc
				.perform(post("/api/convenio").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O convênio foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Convenio> convenios = repoConvenio.findAll();
		assertThat(convenios.get(0), notNullValue());

		this.mockMvc
				.perform(delete("/api/convenio/" + convenios.get(0).getId().toString()).header("token",
						logarAdm("admin", "1234")))
				.andExpect(jsonPath("$",
						equalTo("Convênio ===> " + convenios.get(0).getId().toString() + ": deletado com sucesso")))
				.andExpect(status().isOk());

		this.mockMvc
				.perform(delete("/api/convenio/" + new ConvenioId().toString()).header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.error", equalTo("O convênio a deletar não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(delete("/api/convenio/" + convenios.get(0).getId().toString()).header("token",
						logarAdm("admin", "1234") + "TokenError"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());
	}

	private CriarConvenio criarConvenio(String nome) {
		CriarConvenio convenio = new CriarConvenio();
		convenio.setNome(nome);
		return convenio;
	}

	private EditarConvenio editarConvenioError() {
		EditarConvenio error = new EditarConvenio();
		error.setId(new ConvenioId());
		return error;
	}

	private EditarConvenio editarConvenioError(ConvenioId id) {
		EditarConvenio error = new EditarConvenio();
		error.setId(id);
		return error;
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

	private String logarAdm(String nomeUsuario, String senha) throws NoSuchAlgorithmException {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginAdm(corpoLogin).getBody();
	}

}
