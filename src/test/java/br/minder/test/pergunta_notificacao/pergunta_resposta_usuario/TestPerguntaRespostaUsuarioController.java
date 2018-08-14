package br.minder.test.pergunta_notificacao.pergunta_resposta_usuario;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import br.minder.genero.Genero;
import br.minder.genero.GeneroId;
import br.minder.genero.GeneroRepository;
import br.minder.genero.comandos.CriarGenero;
import br.minder.login.LoginController;
import br.minder.login.comandos.LogarUsuario;
import br.minder.pergunta_notificacao.Pergunta;
import br.minder.pergunta_notificacao.PerguntaId;
import br.minder.pergunta_notificacao.PerguntaRepository;
import br.minder.pergunta_notificacao.PerguntaService;
import br.minder.pergunta_notificacao.comandos.CriarPergunta;
import br.minder.pergunta_notificacao.pergunta_resposta_usuario.PerguntaRespostaUsuario;
import br.minder.pergunta_notificacao.pergunta_resposta_usuario.PerguntaRespostaUsuarioId;
import br.minder.pergunta_notificacao.pergunta_resposta_usuario.PerguntaRespostaUsuarioRepository;
import br.minder.pergunta_notificacao.pergunta_resposta_usuario.comandos.CriarPerguntaRespostaUsuario;
import br.minder.pergunta_notificacao.resposta.Resposta;
import br.minder.pergunta_notificacao.resposta.RespostaId;
import br.minder.pergunta_notificacao.resposta.RespostaRepository;
import br.minder.pergunta_notificacao.resposta.RespostaService;
import br.minder.pergunta_notificacao.resposta.comandos.CriarResposta;
import br.minder.sangue.Sangue;
import br.minder.sangue.SangueId;
import br.minder.sangue.SangueRepository;
import br.minder.sangue.comandos.CriarSangue;
import br.minder.telefone.comandos.CriarTelefone;
import br.minder.usuario.Usuario;
import br.minder.usuario.UsuarioRepository;
import br.minder.usuario.UsuarioService;
import br.minder.usuario.comandos.CriarUsuario;
import br.minder.usuario_adm.UsuarioAdm;
import br.minder.usuario_adm.UsuarioAdmRepository;
import br.minder.usuario_adm.UsuarioAdmService;
import br.minder.usuario_adm.comandos.CriarUsuarioAdm;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
public class TestPerguntaRespostaUsuarioController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private PerguntaRespostaUsuarioRepository repo;

	@Autowired
	private LoginController login;

	@Autowired
	private PerguntaService perguntaService;

	@Autowired
	private RespostaService respostaService;

	@Autowired
	private RespostaRepository repoResposta;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository repoUsuario;

	@Autowired
	private PerguntaRepository repoPergunta;

	@Autowired
	private SangueRepository repoSangue;

	@Autowired
	private GeneroRepository repoGenero;

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
		GeneroId idGenero = criarGenero("Masculino");
		SangueId idSangue = criarSangue("A+");
		usuarioService.salvar((criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)));
		List<Usuario> usuario = repoUsuario.findAll();
		assertThat(usuario.get(0), notNullValue());

		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		perguntaService.salvar(criarPergunta("Como vc está se sentindo hj??"));
		List<Pergunta> pergunta = repoPergunta.findAll();
		assertThat(pergunta.get(0), notNullValue());

		respostaService.salvar(criarResposta("Bem", pergunta.get(0).getIdPergunta()));
		List<Resposta> resposta = repoResposta.findAll();
		assertThat(resposta.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(
				criarPerguntaRespostaUsuario(pergunta.get(0).getIdPergunta(), resposta.get(0).getIdResposta()));

		this.mockMvc
				.perform(post("/api/perguntaNotificacao").header("token", logar("wagnerju", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A  pergunta de notificação foi respondida com sucesso")))
				.andExpect(status().isCreated());

		this.mockMvc.perform(post("/api/perguntaNotificacao").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		jsonString = objectMapper.writeValueAsString(
				criarPerguntaRespostaUsuarioErro1(pergunta.get(0).getIdPergunta(), resposta.get(0).getIdResposta()));

		this.mockMvc
				.perform(post("/api/perguntaNotificacao").header("token", logar("wagnerju", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error",
						equalTo("A pergunta de notificação não foi respondida devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(
				criarPerguntaRespostaUsuarioErro2(pergunta.get(0).getIdPergunta(), resposta.get(0).getIdResposta()));

		this.mockMvc
				.perform(post("/api/perguntaNotificacao").header("token", logar("wagnerju", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error",
						equalTo("A pergunta de notificação não foi respondida devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(
				criarPerguntaRespostaUsuarioErro3(pergunta.get(0).getIdPergunta(), resposta.get(0).getIdResposta()));

		this.mockMvc
				.perform(post("/api/perguntaNotificacao").header("token", logar("wagnerju", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error",
						equalTo("A pergunta de notificação não foi respondida devido a um erro interno")))
				.andExpect(status().isInternalServerError());

	}

	@Test
	public void testBuscarTodos() throws Exception {
		GeneroId idGenero = criarGenero("Masculino");
		SangueId idSangue = criarSangue("A+");
		usuarioService.salvar((criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)));
		List<Usuario> usuario = repoUsuario.findAll();
		assertThat(usuario.get(0), notNullValue());

		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		this.mockMvc.perform(get("/api/perguntaNotificacao").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error",
						equalTo("Não existe nenhuma pergunta de notificação respondida no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(get("/api/perguntaNotificacao").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		perguntaService.salvar(criarPergunta("Como vc está se sentindo hj??"));
		List<Pergunta> pergunta = repoPergunta.findAll();
		assertThat(pergunta.get(0), notNullValue());

		respostaService.salvar(criarResposta("Bem", pergunta.get(0).getIdPergunta()));
		List<Resposta> resposta = repoResposta.findAll();
		assertThat(resposta.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(
				criarPerguntaRespostaUsuario(pergunta.get(0).getIdPergunta(), resposta.get(0).getIdResposta()));

		this.mockMvc
				.perform(post("/api/perguntaNotificacao").header("token", logar("wagnerju", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A  pergunta de notificação foi respondida com sucesso")))
				.andExpect(status().isCreated());

		perguntaService.salvar(criarPergunta("Como foi seu dia??"));
		pergunta = repoPergunta.findAll();
		assertThat(pergunta.get(1), notNullValue());

		respostaService.salvar(criarResposta("Ruim", pergunta.get(0).getIdPergunta()));
		resposta = repoResposta.findAll();
		assertThat(resposta.get(1), notNullValue());

		jsonString = objectMapper.writeValueAsString(
				criarPerguntaRespostaUsuario(pergunta.get(1).getIdPergunta(), resposta.get(1).getIdResposta()));

		this.mockMvc
				.perform(post("/api/perguntaNotificacao").header("token", logar("wagnerju", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A  pergunta de notificação foi respondida com sucesso")))
				.andExpect(status().isCreated());

		List<PerguntaRespostaUsuario> respostasUser = repo.findAll();
		assertThat(respostasUser.get(0), notNullValue());

		this.mockMvc.perform(get("/api/perguntaNotificacao").header("token", logar("wagnerju", "1234")))
				.andExpect(
						jsonPath("$[0].pergunta.idPergunta.value", equalTo(pergunta.get(0).getIdPergunta().toString())))
				.andExpect(
						jsonPath("$[1].pergunta.idPergunta.value", equalTo(pergunta.get(1).getIdPergunta().toString())))
				.andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		GeneroId idGenero = criarGenero("Masculino");
		SangueId idSangue = criarSangue("A+");
		usuarioService.salvar((criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)));
		List<Usuario> usuario = repoUsuario.findAll();
		assertThat(usuario.get(0), notNullValue());

		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		this.mockMvc
				.perform(get("/api/perguntaNotificacao/" + new PerguntaRespostaUsuarioId().toString()).header("token",
						logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error",
						equalTo("A pergunta de notificação procurada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(get("/api/perguntaNotificacao/" + new PerguntaRespostaUsuarioId().toString())
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		perguntaService.salvar(criarPergunta("Como vc está se sentindo hj??"));
		List<Pergunta> pergunta = repoPergunta.findAll();
		assertThat(pergunta.get(0), notNullValue());

		respostaService.salvar(criarResposta("Bem", pergunta.get(0).getIdPergunta()));
		List<Resposta> resposta = repoResposta.findAll();
		assertThat(resposta.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(
				criarPerguntaRespostaUsuario(pergunta.get(0).getIdPergunta(), resposta.get(0).getIdResposta()));

		this.mockMvc
				.perform(post("/api/perguntaNotificacao").header("token", logar("wagnerju", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A  pergunta de notificação foi respondida com sucesso")))
				.andExpect(status().isCreated());

		List<PerguntaRespostaUsuario> respostasUser = repo.findAll();
		assertThat(respostasUser.get(0), notNullValue());

		this.mockMvc
				.perform(get("/api/perguntaNotificacao/" + respostasUser.get(0).getId().toString()).header("token",
						logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.pergunta.idPergunta.value", equalTo(pergunta.get(0).getIdPergunta().toString())))
				.andExpect(status().isOk());
	}

	private CriarPerguntaRespostaUsuario criarPerguntaRespostaUsuario(PerguntaId idPergunta, RespostaId idResposta) {
		CriarPerguntaRespostaUsuario respostaUser = new CriarPerguntaRespostaUsuario();
		respostaUser.setIdPergunta(idPergunta);
		respostaUser.setIdResposta(idResposta);
		return respostaUser;
	}

	private CriarPerguntaRespostaUsuario criarPerguntaRespostaUsuarioErro1(PerguntaId idPergunta,
			RespostaId idResposta) {
		CriarPerguntaRespostaUsuario respostaUser = new CriarPerguntaRespostaUsuario();
		respostaUser.setIdResposta(idResposta);
		return respostaUser;
	}

	private CriarPerguntaRespostaUsuario criarPerguntaRespostaUsuarioErro2(PerguntaId idPergunta,
			RespostaId idResposta) {
		CriarPerguntaRespostaUsuario respostaUser = new CriarPerguntaRespostaUsuario();
		respostaUser.setIdPergunta(idPergunta);
		return respostaUser;
	}

	private CriarPerguntaRespostaUsuario criarPerguntaRespostaUsuarioErro3(PerguntaId idPergunta,
			RespostaId idResposta) {
		CriarPerguntaRespostaUsuario respostaUser = new CriarPerguntaRespostaUsuario();
		return respostaUser;
	}

	private CriarResposta criarResposta(String descricao, PerguntaId perguntaId) {
		CriarResposta resposta = new CriarResposta();
		resposta.setIdPergunta(perguntaId);
		resposta.setDescricao(descricao);
		return resposta;
	}

	private CriarPergunta criarPergunta(String descricao) {
		CriarPergunta pergunta = new CriarPergunta();
		pergunta.setDescricao(descricao);
		return pergunta;
	}

	private CriarUsuario criarUsuario(String email, String username, GeneroId idGenero, SangueId idSangue) {
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
		usuario.setIdGenero(idGenero);
		usuario.setSenha("1234");
		usuario.setTelefone(telefone);
		usuario.setUsername(username);

		return usuario;
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

	private GeneroId criarGenero(String tipo) {
		return repoGenero.save(new Genero(new CriarGenero(tipo))).getIdGenero();
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

}