package br.minder.test.sangue;

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
import br.minder.login.comandos.LogarUsuario;
import br.minder.sangue.Sangue;
import br.minder.sangue.SangueId;
import br.minder.sangue.SangueRepository;
import br.minder.sangue.comandos.CriarSangue;
import br.minder.sangue.comandos.EditarSangue;
import br.minder.usuario_adm.UsuarioAdm;
import br.minder.usuario_adm.UsuarioAdmRepository;
import br.minder.usuario_adm.UsuarioAdmService;
import br.minder.usuario_adm.comandos.CriarUsuarioAdm;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
public class TestSangueController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private SangueRepository repo;

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

		String jsonString = objectMapper.writeValueAsString(criarSangue("AB"));

		this.mockMvc
				.perform(post("/api/sangue").header("token", logarAdm("admin", "1234") + "erroToken")
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(post("/api/sangue").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O tipo sanguíneo foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarSangueErro());

		this.mockMvc
				.perform(post("/api/sangue").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O tipo sanguíneo não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testEditar() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarSangue("AB"));

		this.mockMvc
				.perform(post("/api/sangue").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O tipo sanguíneo foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Sangue> sangues = repo.findAll();
		assertThat(sangues.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarSangue(sangues.get(0)));

		this.mockMvc
				.perform(put("/api/sangue").header("token", logarAdm("admin", "1234") + "erroToken")
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(put("/api/sangue").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O tipo sanguíneo foi alterado com sucesso")))
				.andExpect(status().isOk());

		jsonString = objectMapper.writeValueAsString(editarSangueErroId(sangues.get(0)));

		this.mockMvc
				.perform(put("/api/sangue").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O tipo sanguíneo a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarSangueErro1(sangues.get(0)));

		this.mockMvc
				.perform(put("/api/sangue").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(
						jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do tipo sanguíneo")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testBuscarTodos() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		this.mockMvc.perform(get("/api/sangue"))
				.andExpect(
						jsonPath("$.error", equalTo("Não existe nenhum tipo sanguíneo cadastrado no banco de dados")))
				.andExpect(status().isNotFound());

		String jsonString = objectMapper.writeValueAsString(criarSangue("AB"));

		this.mockMvc
				.perform(post("/api/sangue").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O tipo sanguíneo foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarSangue("A+"));

		this.mockMvc
				.perform(post("/api/sangue").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O tipo sanguíneo foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Sangue> sangues = repo.findAll();
		assertThat(sangues.get(0), notNullValue());
		assertThat(sangues.get(1), notNullValue());

		this.mockMvc.perform(get("/api/sangue")).andExpect(jsonPath("$[0].tipoSanguineo", equalTo("AB")))
				.andExpect(jsonPath("$[1].tipoSanguineo", equalTo("A+"))).andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarSangue("AB"));

		this.mockMvc
				.perform(post("/api/sangue").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O tipo sanguíneo foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Sangue> sangues = repo.findAll();
		assertThat(sangues.get(0), notNullValue());

		this.mockMvc.perform(get("/api/sangue/" + sangues.get(0).getIdSangue().toString()))
				.andExpect(jsonPath("$.tipoSanguineo", equalTo("AB"))).andExpect(status().isOk());

		this.mockMvc.perform(get("/api/sangue/" + new SangueId().toString()))
				.andExpect(jsonPath("$.error", equalTo("O tipo sanguíneo procurado não existe no banco de dados")))
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

	private CriarSangue criarSangue(String tipo) {
		return new CriarSangue(tipo);
	}

	private CriarSangue criarSangueErro() {
		return new CriarSangue();
	}

	private EditarSangue editarSangue(Sangue sangue) {
		EditarSangue sangueAtualizado = new EditarSangue();
		sangueAtualizado.setIdSangue(sangue.getIdSangue());
		sangueAtualizado.setTipoSanguineo("O+");
		return sangueAtualizado;
	}

	private EditarSangue editarSangueErroId(Sangue sangue) {
		EditarSangue sangueAtualizado = new EditarSangue();
		sangueAtualizado.setIdSangue(new SangueId());
		sangueAtualizado.setTipoSanguineo("O+");
		return sangueAtualizado;
	}

	private EditarSangue editarSangueErro1(Sangue sangue) {
		EditarSangue sangueAtualizado = new EditarSangue();
		sangueAtualizado.setIdSangue(sangue.getIdSangue());
		return sangueAtualizado;
	}

}
