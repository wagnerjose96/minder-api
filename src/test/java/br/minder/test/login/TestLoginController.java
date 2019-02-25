package br.minder.test.login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
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
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.genero.Genero;
import br.minder.genero.GeneroId;
import br.minder.genero.GeneroRepository;
import br.minder.genero.comandos.CriarGenero;
import br.minder.login.comandos.GerarToken;
import br.minder.login.comandos.LogarUsuario;
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
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
@ActiveProfiles("application-test")
public class TestLoginController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private SangueRepository repoSangue;

	@Autowired
	private GeneroRepository repoGenero;

	@Autowired
	private UsuarioService serviceUsuario;

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private UsuarioAdmRepository repoAdm;

	@Autowired
	private UsuarioAdmService admService;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testGerarToken() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(gerarToken("wagnerju"));

		this.mockMvc.perform(post("/api/token").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)).andExpect(jsonPath("$", notNullValue())).andExpect(status().isOk());

		jsonString = objectMapper.writeValueAsString(gerarToken("wagner@hotmail.com"));

		this.mockMvc.perform(post("/api/token").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)).andExpect(jsonPath("$", notNullValue())).andExpect(status().isOk());

		jsonString = objectMapper.writeValueAsString(gerarToken("lathuanny"));

		this.mockMvc
				.perform(post("/api/token").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Usuário inválido! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(gerarToken("lathuanny"));

		this.mockMvc
				.perform(post("/api/token").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Usuário inválido! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(gerarToken("Wagner@hotmail.com"));

		this.mockMvc
				.perform(post("/api/token").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Usuário inválido! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(gerarToken("lathuanny@hotmail.com"));

		this.mockMvc
				.perform(post("/api/token").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Usuário inválido! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(gerarToken("lathuanny@"));

		this.mockMvc
				.perform(post("/api/token").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Usuário inválido! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(gerarToken("lathuannyhotmail.com"));

		this.mockMvc
				.perform(post("/api/token").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Usuário inválido! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(gerarToken("lathuanny@hotmail"));

		this.mockMvc
				.perform(post("/api/token").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Usuário inválido! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(gerarToken("@.com"));

		this.mockMvc
				.perform(post("/api/token").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Usuário inválido! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(gerarToken("lathuanny@.com"));

		this.mockMvc
				.perform(post("/api/token").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Usuário inválido! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		serviceUsuario.deletar(usuarios.get(0).getId()).get();
		usuarios = repo.findAll();
		assertThat(usuarios.get(0).getAtivo(), equalTo(0));
		assertThat(usuarios.get(0).getNomeUsuario(), equalTo("wagnerju"));

		jsonString = objectMapper.writeValueAsString(gerarToken("wagnerju"));

		this.mockMvc
				.perform(post("/api/token").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Usuário inválido! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(gerarToken("wagner@hotmail.com"));

		this.mockMvc
				.perform(post("/api/token").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Usuário inválido! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testLoginUsuario() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(logar("wagnerju", "1234"));

		this.mockMvc.perform(post("/api/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)).andExpect(jsonPath("$", notNullValue())).andExpect(status().isOk());

		jsonString = objectMapper.writeValueAsString(logar("wagner@hotmail.com", "1234"));

		this.mockMvc.perform(post("/api/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)).andExpect(jsonPath("$", notNullValue())).andExpect(status().isOk());

		jsonString = objectMapper.writeValueAsString(logar("lathuanny", "1234"));

		this.mockMvc
				.perform(post("/api/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(logar("lathuanny", "1235"));

		this.mockMvc
				.perform(post("/api/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(logar("wagner@hotmail.com", "12345"));

		this.mockMvc
				.perform(post("/api/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(logar("lathuanny@hotmail.com", "1234"));

		this.mockMvc
				.perform(post("/api/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(logar("lathuanny@", "1234"));

		this.mockMvc
				.perform(post("/api/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(logar("lathuannyhotmail.com", "1234"));

		this.mockMvc
				.perform(post("/api/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(logar("lathuanny@hotmail", "1234"));

		this.mockMvc
				.perform(post("/api/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(logar("@.com", "1234"));

		this.mockMvc
				.perform(post("/api/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(logar("lathuanny@.com", "1234"));

		this.mockMvc
				.perform(post("/api/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		serviceUsuario.deletar(usuarios.get(0).getId()).get();
		usuarios = repo.findAll();
		assertThat(usuarios.get(0).getAtivo(), equalTo(0));
		assertThat(usuarios.get(0).getNomeUsuario(), equalTo("wagnerju"));

		jsonString = objectMapper.writeValueAsString(logar("wagnerju", "1234"));

		this.mockMvc
				.perform(post("/api/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(logar("wagner@hotmail.com", "1234"));

		this.mockMvc
				.perform(post("/api/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testLoginUsuarioAdm() throws Exception {
		admService.salvar(criarAdm());

		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(logarAdm("admin", "1234"));

		this.mockMvc.perform(post("/api/loginAdm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(jsonString)).andExpect(jsonPath("$", notNullValue()))
				.andExpect(status().isOk());

		jsonString = objectMapper.writeValueAsString(logarAdm("adm", "1234"));

		this.mockMvc
				.perform(post("/api/loginAdm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(logarAdm("adm", "12345"));

		this.mockMvc
				.perform(post("/api/loginAdm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(logarAdm("admin", "12345"));

		this.mockMvc
				.perform(post("/api/loginAdm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Login não realizado! Favor conferir os dados digitados")))
				.andExpect(status().isNotFound());
	}
	
	private GerarToken gerarToken(String identificador) {
		GerarToken corpoToken = new GerarToken();
		corpoToken.setIdentificador(identificador);
		return corpoToken;
	}

	private LogarUsuario logar(String nomeUsuario, String senha) {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return corpoLogin;
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

	private CriarUsuarioAdm criarAdm() {
		CriarUsuarioAdm adm = new CriarUsuarioAdm();
		adm.setNome("admin");
		adm.setSenha("1234");
		return adm;
	}

	private LogarUsuario logarAdm(String nomeUsuario, String senha) {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return corpoLogin;
	}
}