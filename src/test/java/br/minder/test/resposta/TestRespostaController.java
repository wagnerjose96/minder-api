package br.minder.test.resposta;

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
import br.minder.pergunta_notificacao.Pergunta;
import br.minder.pergunta_notificacao.PerguntaId;
import br.minder.pergunta_notificacao.PerguntaRepository;
import br.minder.pergunta_notificacao.PerguntaService;
import br.minder.pergunta_notificacao.comandos.CriarPergunta;
import br.minder.pergunta_notificacao.resposta.Resposta;
import br.minder.pergunta_notificacao.resposta.RespostaRepository;
import br.minder.pergunta_notificacao.resposta.comandos.CriarResposta;
import br.minder.pergunta_notificacao.resposta.comandos.EditarResposta;
import br.minder.usuario_adm.UsuarioAdm;
import br.minder.usuario_adm.UsuarioAdmRepository;
import br.minder.usuario_adm.UsuarioAdmService;
import br.minder.usuario_adm.comandos.CriarUsuarioAdm;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
public class TestRespostaController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private RespostaRepository repo;

	@Autowired
	private LoginController login;

	@Autowired
	private UsuarioAdmRepository repoAdm;

	@Autowired
	private UsuarioAdmService admService;

	@Autowired
	private PerguntaService perguntaService;

	@Autowired
	private PerguntaRepository repoPergunta;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		perguntaService.salvar(criarPergunta("Como vc está se sentindo hj??"));
		List<Pergunta> pergunta = repoPergunta.findAll();
		assertThat(pergunta.get(0), notNullValue());

		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarResposta("Bem", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/respostas").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());
	}

	@Test
	public void testeEditar() throws Exception {
		perguntaService.salvar(criarPergunta("Como vc está se sentindo hj??"));
		List<Pergunta> pergunta = repoPergunta.findAll();
		assertThat(pergunta.get(0), notNullValue());

		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarResposta("Bem", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/respostas").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Resposta> respostas = repo.findAll();
		assertThat(respostas.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarResposta(respostas.get(0)));

		this.mockMvc
				.perform(put("/respostas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi alterada com sucesso"))).andExpect(status().isOk());
	}

	@Test
	public void testBuscarTodos() throws Exception {
		perguntaService.salvar(criarPergunta("Como vc está se sentindo hj??"));
		List<Pergunta> pergunta = repoPergunta.findAll();
		assertThat(pergunta.get(0), notNullValue());

		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarResposta("Bem", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/respostas").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarResposta("Ruim", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/respostas").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Resposta> respostas = repo.findAll();
		assertThat(respostas.get(0), notNullValue());

		this.mockMvc.perform(get("/respostas").header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$[0].descricao", equalTo("Bem")))
				.andExpect(jsonPath("$[1].descricao", equalTo("Ruim"))).andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		perguntaService.salvar(criarPergunta("Como vc está se sentindo hj??"));
		List<Pergunta> pergunta = repoPergunta.findAll();
		assertThat(pergunta.get(0), notNullValue());

		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarResposta("Bem", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/respostas").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Resposta> respostas = repo.findAll();
		assertThat(respostas.get(0), notNullValue());

		this.mockMvc.perform(get("/respostas/" + respostas.get(0).getIdResposta().toString()))
				.andExpect(jsonPath("$", notNullValue())).andExpect(jsonPath("$.descricao", equalTo("Bem")))
				.andExpect(status().isOk());
	}

	private CriarResposta criarResposta(String descricao, PerguntaId perguntaId) {
		CriarResposta resposta = new CriarResposta();
		resposta.setIdPergunta(perguntaId);
		resposta.setDescricao(descricao);
		return resposta;
	}

	private EditarResposta editarResposta(Resposta resposta) {
		EditarResposta respostaAtualizada = new EditarResposta();
		respostaAtualizada.setIdResposta(resposta.getIdResposta());
		respostaAtualizada.setDescricao("Teste Put");
		return respostaAtualizada;
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

	private CriarPergunta criarPergunta(String descricao) {
		CriarPergunta pergunta = new CriarPergunta();
		pergunta.setDescricao(descricao);
		return pergunta;
	}

}