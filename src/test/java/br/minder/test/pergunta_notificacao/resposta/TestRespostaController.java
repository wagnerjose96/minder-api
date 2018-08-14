package br.minder.test.pergunta_notificacao.resposta;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
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
import br.minder.pergunta_notificacao.resposta.Resposta;
import br.minder.pergunta_notificacao.resposta.RespostaId;
import br.minder.pergunta_notificacao.resposta.RespostaRepository;
import br.minder.pergunta_notificacao.resposta.comandos.CriarResposta;
import br.minder.pergunta_notificacao.resposta.comandos.EditarResposta;
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

	@Autowired
	private GeneroRepository repoGenero;

	@Autowired
	private SangueRepository repoSangue;

	@Autowired
	private UsuarioService serviceUsuario;

	@Autowired
	private UsuarioRepository repoUsuario;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		perguntaService.salvar(criarPergunta("Como vc está se sentindo hj??"));
		perguntaService.salvar(criarPergunta("Como vc está se sentindo hj??"));
		List<Pergunta> pergunta = repoPergunta.findAll();
		assertThat(pergunta.get(0), notNullValue());

		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");
		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();
		List<Usuario> usuarios = repoUsuario.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarResposta("Bem", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/api/resposta").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());
		
		jsonString = objectMapper.writeValueAsString(criarResposta("Bem", pergunta.get(1).getIdPergunta()));

		this.mockMvc
				.perform(post("/api/resposta").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarResposta("Mal", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/api/resposta").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarResposta("Triste", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/api/resposta").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarResposta("Alegre", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/api/resposta").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarResposta("Erro", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/api/resposta").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A resposta não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		this.mockMvc
				.perform(post("/api/resposta").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		jsonString = objectMapper.writeValueAsString(criarRespostaErro1("Bem", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/api/resposta").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A resposta não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(criarRespostaErro2("Bem", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/api/resposta").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A resposta não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());

	}

	@Test
	public void testEditar() throws Exception {
		perguntaService.salvar(criarPergunta("Como vc está se sentindo hj??"));
		List<Pergunta> pergunta = repoPergunta.findAll();
		assertThat(pergunta.get(0), notNullValue());

		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");
		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();
		List<Usuario> usuarios = repoUsuario.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarResposta("Bem", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/api/resposta").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Resposta> respostas = repo.findAll();
		assertThat(respostas.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarResposta(respostas.get(0)));

		this.mockMvc
				.perform(put("/api/resposta").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi alterada com sucesso"))).andExpect(status().isOk());

		this.mockMvc
				.perform(put("/api/resposta").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		jsonString = objectMapper.writeValueAsString(editarRespostaErro1(respostas.get(0)));

		this.mockMvc
				.perform(put("/api/resposta").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração da resposta")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarRespostaErro2(respostas.get(0)));

		this.mockMvc
				.perform(put("/api/resposta").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A resposta a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarRespostaErro3(respostas.get(0)));

		this.mockMvc
				.perform(put("/api/resposta").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A resposta a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testBuscarTodos() throws Exception {
		perguntaService.salvar(criarPergunta("Como vc está se sentindo hj??"));
		List<Pergunta> pergunta = repoPergunta.findAll();
		assertThat(pergunta.get(0), notNullValue());

		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		this.mockMvc.perform(get("/api/resposta").header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhuma resposta cadastrada no banco de dados")))
				.andExpect(status().isNotFound());

		String jsonString = objectMapper.writeValueAsString(criarResposta("Bem", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/api/resposta").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarResposta("Ruim", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/api/resposta").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Resposta> respostas = repo.findAll();
		assertThat(respostas.get(0), notNullValue());
		assertThat(respostas.get(1), notNullValue());

		this.mockMvc.perform(get("/api/resposta").header("token", logarAdm("admin", "1234")))
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

		this.mockMvc
				.perform(get("/api/resposta/" + new RespostaId().toString()).header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.error", equalTo("A resposta procurada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		String jsonString = objectMapper.writeValueAsString(criarResposta("Bem", pergunta.get(0).getIdPergunta()));

		this.mockMvc
				.perform(post("/api/resposta").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A resposta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Resposta> respostas = repo.findAll();
		assertThat(respostas.get(0), notNullValue());

		this.mockMvc.perform(get("/api/resposta/" + respostas.get(0).getIdResposta().toString()))
				.andExpect(jsonPath("$", notNullValue())).andExpect(jsonPath("$.descricao", equalTo("Bem")))
				.andExpect(status().isOk());
	}

	private CriarResposta criarResposta(String descricao, PerguntaId perguntaId) {
		CriarResposta resposta = new CriarResposta();
		resposta.setIdPergunta(perguntaId);
		resposta.setDescricao(descricao);
		return resposta;
	}

	private CriarResposta criarRespostaErro1(String descricao, PerguntaId perguntaId) {
		CriarResposta resposta = new CriarResposta();
		resposta.setDescricao(descricao);
		return resposta;
	}

	private CriarResposta criarRespostaErro2(String descricao, PerguntaId perguntaId) {
		CriarResposta resposta = new CriarResposta();
		resposta.setIdPergunta(perguntaId);
		return resposta;
	}

	private EditarResposta editarResposta(Resposta resposta) {
		EditarResposta respostaAtualizada = new EditarResposta();
		respostaAtualizada.setIdResposta(resposta.getIdResposta());
		respostaAtualizada.setDescricao("Teste Put");
		return respostaAtualizada;
	}

	private EditarResposta editarRespostaErro1(Resposta resposta) {
		EditarResposta respostaAtualizada = new EditarResposta();
		respostaAtualizada.setIdResposta(resposta.getIdResposta());
		return respostaAtualizada;
	}

	private EditarResposta editarRespostaErro2(Resposta resposta) {
		EditarResposta respostaAtualizada = new EditarResposta();
		respostaAtualizada.setIdResposta(new RespostaId());
		respostaAtualizada.setDescricao("Teste Put");
		return respostaAtualizada;
	}

	private EditarResposta editarRespostaErro3(Resposta resposta) {
		EditarResposta respostaAtualizada = new EditarResposta();
		respostaAtualizada.setIdResposta(new RespostaId());
		return respostaAtualizada;
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

	private SangueId criarSangue(String tipo) {
		SangueId id = repoSangue.save(new Sangue(new CriarSangue(tipo))).getIdSangue();
		return id;
	}

	private GeneroId criarGenero(String tipo) {
		GeneroId id = repoGenero.save(new Genero(new CriarGenero(tipo))).getIdGenero();
		return id;
	}

	private LogarUsuario logar(String nomeUsuario, String senha) {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return corpoLogin;
	}

}