package br.minder.test.emergencia;

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
import br.minder.emergencia.Emergencia;
import br.minder.emergencia.EmergenciaRepository;
import br.minder.emergencia.comandos.CriarEmergencia;
import br.minder.emergencia.comandos.EditarEmergencia;
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.genero.Genero;
import br.minder.genero.GeneroId;
import br.minder.genero.GeneroRepository;
import br.minder.genero.comandos.CriarGenero;
import br.minder.login.LoginController;
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

@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
public class TestEmergenciaController {

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
	private LoginController login;

	@Autowired
	private UsuarioService serviceUsuario;

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private EmergenciaRepository repoEmergencia;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarEmergencia(1));

		this.mockMvc
				.perform(post("/emergencias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A emergência foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		this.mockMvc
				.perform(post("/emergencias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "erroToken").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

	}

	@Test
	public void testEditar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();
		serviceUsuario.salvar(criarUsuario("lathuanny@hotmail.com", "lathuanny", idGenero, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarEmergencia(1));

		this.mockMvc
				.perform(post("/emergencias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A emergência foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Emergencia> emergencias = repoEmergencia.findAll();
		assertThat(emergencias.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarEmergencia(emergencias.get(0)));

		this.mockMvc
				.perform(put("/emergencias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A emergência foi alterada com sucesso"))).andExpect(status().isOk());

		this.mockMvc
				.perform(put("/emergencias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("lathuanny", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A emergência a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(put("/emergencias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "erroToken").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());
	}

	@Test
	public void testBurcarTodos() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		this.mockMvc.perform(get("/emergencias").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhuma emergência cadastrada no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc.perform(get("/emergencias").header("token", logar("wagnerju", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		String jsonString = objectMapper.writeValueAsString(criarEmergencia(1));

		this.mockMvc
				.perform(post("/emergencias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A emergência foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Emergencia> emergencias = repoEmergencia.findAll();
		assertThat(emergencias.get(0), notNullValue());

		this.mockMvc.perform(get("/emergencias").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.problemasCardiacos", equalTo("Arritmia"))).andExpect(status().isOk());
	}

	private CriarEmergencia criarEmergencia(int doador) {
		CriarEmergencia emergencia = new CriarEmergencia();
		emergencia.setAtaqueConvulsivos(0);
		emergencia.setDoadorDeOrgaos(doador);
		emergencia.setProblemasCardiacos("Arritmia");
		return emergencia;
	}

	private EditarEmergencia editarEmergencia(Emergencia emergencia) {
		EditarEmergencia emergenciaEditada = new EditarEmergencia();
		emergenciaEditada.setId(emergencia.getIdEmergencia());
		emergenciaEditada.setAtaqueConvulsivos(emergencia.getAtaqueConvulsivos());
		emergenciaEditada.setDoadorDeOrgaos(emergencia.getDoadorDeOrgaos());
		emergenciaEditada.setProblemasCardiacos("Sopro no coração");
		return emergenciaEditada;
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

	private String logar(String nomeUsuario, String senha) {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginUsuario(corpoLogin).getBody();
	}
}