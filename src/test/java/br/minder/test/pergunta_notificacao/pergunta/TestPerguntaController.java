package br.minder.test.pergunta_notificacao.pergunta;

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
import br.minder.login.LoginController;
import br.minder.login.comandos.LogarUsuario;
import br.minder.pergunta_notificacao.Pergunta;
import br.minder.pergunta_notificacao.PerguntaId;
import br.minder.pergunta_notificacao.PerguntaRepository;
import br.minder.pergunta_notificacao.comandos.CriarPergunta;
import br.minder.pergunta_notificacao.comandos.EditarPergunta;
import br.minder.sangue.Sangue;
import br.minder.sangue.SangueId;
import br.minder.sangue.SangueRepository;
import br.minder.sangue.comandos.CriarSangue;
import br.minder.sexo.Sexo;
import br.minder.sexo.SexoId;
import br.minder.sexo.SexoRepository;
import br.minder.sexo.comandos.CriarSexo;
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

	@Autowired
	private SexoRepository repoSexo;

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
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");
		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		List<Usuario> usuarios = repoUsuario.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarPergunta("Como vc está se sentindo hj??"));

		this.mockMvc
				.perform(post("/perguntas").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A pergunta foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarPerguntaErro("Como vc está se sentindo hj??"));

		this.mockMvc
				.perform(post("/perguntas").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A pergunta não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		this.mockMvc
				.perform(post("/perguntas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());
	}

	@Test
	public void testEditar() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");
		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		List<Usuario> usuarios = repoUsuario.findAll();
		assertThat(usuarios.get(0), notNullValue());

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

		this.mockMvc
				.perform(put("/perguntas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		jsonString = objectMapper.writeValueAsString(editarPerguntaErro1(perguntas.get(0)));

		this.mockMvc
				.perform(put("/perguntas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração da pergunta")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarPerguntaErro2(perguntas.get(0)));

		this.mockMvc
				.perform(put("/perguntas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A pergunta a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarPerguntaErro3(perguntas.get(0)));

		this.mockMvc
				.perform(put("/perguntas").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A pergunta a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testBuscarTodos() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		this.mockMvc.perform(get("/perguntas").header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhuma pergunta cadastrada no banco de dados")))
				.andExpect(status().isNotFound());

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
		assertThat(perguntas.get(1), notNullValue());

		this.mockMvc.perform(get("/perguntas").header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$[0].descricao", equalTo("Como vc está se sentindo hj??")))
				.andExpect(jsonPath("$[1].descricao", equalTo("Como foi seu dia??"))).andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		this.mockMvc
				.perform(get("/perguntas/" + new PerguntaId().toString()).header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.error", equalTo("A pergunta procurada não existe no banco de dados")))
				.andExpect(status().isNotFound());

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

	private CriarPergunta criarPerguntaErro(String descricao) {
		CriarPergunta pergunta = new CriarPergunta();
		return pergunta;
	}

	private EditarPergunta editarPergunta(Pergunta pergunta) {
		EditarPergunta perguntaAtualizada = new EditarPergunta();
		perguntaAtualizada.setIdPergunta(pergunta.getIdPergunta());
		perguntaAtualizada.setDescricao("Teste Put");
		return perguntaAtualizada;
	}

	private EditarPergunta editarPerguntaErro1(Pergunta pergunta) {
		EditarPergunta perguntaAtualizada = new EditarPergunta();
		perguntaAtualizada.setIdPergunta(pergunta.getIdPergunta());
		return perguntaAtualizada;
	}

	private EditarPergunta editarPerguntaErro2(Pergunta pergunta) {
		EditarPergunta perguntaAtualizada = new EditarPergunta();
		perguntaAtualizada.setIdPergunta(new PerguntaId());
		perguntaAtualizada.setDescricao("Teste Put");
		return perguntaAtualizada;
	}

	private EditarPergunta editarPerguntaErro3(Pergunta pergunta) {
		EditarPergunta perguntaAtualizada = new EditarPergunta();
		perguntaAtualizada.setIdPergunta(new PerguntaId());
		return perguntaAtualizada;
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

	private SangueId criarSangue(String tipo) {
		SangueId id = repoSangue.save(new Sangue(new CriarSangue(tipo))).getIdSangue();
		return id;
	}

	private SexoId criarSexo(String tipo) {
		SexoId id = repoSexo.save(new Sexo(new CriarSexo(tipo))).getIdGenero();
		return id;
	}

	private LogarUsuario logar(String nomeUsuario, String senha) {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return corpoLogin;
	}

}