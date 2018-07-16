package br.hela.test.pergunta;

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
import br.hela.Escoladeti2018Application;
import br.hela.login.LoginController;
import br.hela.login.comandos.LogarAdm;
import br.hela.pergunta_notificacao.Pergunta;
import br.hela.pergunta_notificacao.PerguntaRepository;
import br.hela.pergunta_notificacao.comandos.CriarPergunta;
import br.hela.pergunta_notificacao.comandos.EditarPergunta;
import br.hela.usuario_adm.UsuarioAdm;
import br.hela.usuario_adm.UsuarioAdmRepository;
import br.hela.usuario_adm.UsuarioAdmService;
import br.hela.usuario_adm.comandos.CriarUsuarioAdm;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { Escoladeti2018Application.class }, webEnvironment = WebEnvironment.MOCK)
public class TestPerguntaController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private PerguntaRepository repo;

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

		String jsonString = objectMapper.writeValueAsString(criarPergunta("Como vc está se sentindo hj??"));

		this.mockMvc
				.perform(post("/perguntas").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A pergunta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());
	}

	@Test
	public void testeEditar() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarPergunta("Como vc está se sentindo hj??"));

		this.mockMvc
				.perform(post("/perguntas").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A pergunta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Pergunta> perguntas = repo.findAll();
		assertThat(perguntas.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarPergunta(perguntas.get(0)));

		this.mockMvc
				.perform(put("/perguntas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A pergunta foi alterada com sucesso"))).andExpect(status().isOk());
	}

	@Test
	public void testBuscarTodos() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarPergunta("Como vc está se sentindo hj??"));

		this.mockMvc
				.perform(post("/perguntas").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A pergunta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarPergunta("Como foi seu dia??"));

		this.mockMvc
				.perform(post("/perguntas").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A pergunta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Pergunta> perguntas = repo.findAll();
		assertThat(perguntas.get(0), notNullValue());

		this.mockMvc.perform(get("/perguntas").header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$[0].descricao", equalTo("Como vc está se sentindo hj??")))
				.andExpect(jsonPath("$[1].descricao", equalTo("Como foi seu dia??"))).andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarPergunta("Como vc está se sentindo hj??"));

		this.mockMvc
				.perform(post("/perguntas").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A pergunta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Pergunta> perguntas = repo.findAll();
		assertThat(perguntas.get(0), notNullValue());

		this.mockMvc.perform(get("/perguntas/" + perguntas.get(0).getIdPergunta().toString()))
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.descricao", equalTo("Como vc está se sentindo hj??")))
				.andExpect(status().isOk());
	}

	private CriarPergunta criarPergunta(String descricao) {
		CriarPergunta pergunta = new CriarPergunta();
		pergunta.setDescricao(descricao);
		return pergunta;
	}

	private EditarPergunta editarPergunta(Pergunta pergunta) {
		EditarPergunta perguntaAtualizada = new EditarPergunta();
		perguntaAtualizada.setIdPergunta(pergunta.getIdPergunta());
		perguntaAtualizada.setDescricao("Teste Put");
		return perguntaAtualizada;
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