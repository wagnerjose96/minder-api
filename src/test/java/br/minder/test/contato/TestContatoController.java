package br.minder.test.contato;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import br.minder.contato.Contato;
import br.minder.contato.ContatoId;
import br.minder.contato.ContatoRepository;
import br.minder.contato.comandos.CriarContato;
import br.minder.contato.comandos.EditarContato;
import br.minder.emergencia.Emergencia;
import br.minder.emergencia.EmergenciaRepository;
import br.minder.emergencia.EmergenciaService;
import br.minder.emergencia.comandos.CriarEmergencia;
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.login.LoginController;
import br.minder.login.comandos.LogarUsuario;
import br.minder.sangue.Sangue;
import br.minder.sangue.SangueId;
import br.minder.sangue.SangueRepository;
import br.minder.sangue.comandos.CriarSangue;
import br.minder.sexo.Sexo;
import br.minder.sexo.SexoId;
import br.minder.sexo.SexoRepository;
import br.minder.sexo.comandos.CriarSexo;
import br.minder.telefone.TelefoneId;
import br.minder.telefone.comandos.CriarTelefone;
import br.minder.telefone.comandos.EditarTelefone;
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
public class TestContatoController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private SangueRepository repoSangue;

	@Autowired
	private SexoRepository repoSexo;

	@Autowired
	private LoginController login;

	@Autowired
	private UsuarioService serviceUsuario;

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private EmergenciaRepository repoEmergencia;

	@Autowired
	private EmergenciaService serviceEmergencia;

	@Autowired
	private ContatoRepository repoContato;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		serviceEmergencia.salvar(criarEmergencia(), usuarios.get(0).getId());

		List<Emergencia> emergencias = repoEmergencia.findAll();
		assertThat(emergencias.get(0), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarContato("Larissa Thuanny"));

		this.mockMvc
				.perform(post("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "TokenError").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(post("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O contato foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		String error = objectMapper.writeValueAsString(new CriarContato());

		this.mockMvc
				.perform(post("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("O contato não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		objectMapper.writeValueAsString(criarContatoError1("Larissa Thuanny"));

		this.mockMvc
				.perform(post("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("O contato não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		objectMapper.writeValueAsString(criarContatoError2("Larissa Thuanny"));

		this.mockMvc
				.perform(post("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("O contato não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

	}

	@Test
	public void testEditar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		serviceEmergencia.salvar(criarEmergencia(), usuarios.get(0).getId());

		List<Emergencia> emergencias = repoEmergencia.findAll();
		assertThat(emergencias.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarContato("Larissa Thuanny"));

		this.mockMvc
				.perform(post("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O contato foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Contato> contatos = repoContato.findAll();
		assertThat(contatos.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarContato(contatos.get(0)));

		this.mockMvc
				.perform(put("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "TokenError").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(put("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O contato foi alterado com sucesso"))).andExpect(status().isOk());

		String error = objectMapper.writeValueAsString(editarContatoError(contatos.get(0).getId()));

		this.mockMvc
				.perform(put("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do contato")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(editarContatoError1(contatos.get(0)));

		this.mockMvc
				.perform(put("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do contato")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(editarContatoError2(contatos.get(0)));

		this.mockMvc
				.perform(put("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do contato")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(editarContatoError3(contatos.get(0)));

		this.mockMvc
				.perform(put("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("O contato a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		error = objectMapper.writeValueAsString(editarContatoError4(contatos.get(0)));

		this.mockMvc
				.perform(put("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("O contato a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		error = objectMapper.writeValueAsString(editarContatoError5(contatos.get(0)));

		this.mockMvc
				.perform(put("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("O contato a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		error = objectMapper.writeValueAsString(editarContatoError6(contatos.get(0)));

		this.mockMvc
				.perform(put("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("O contato a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		serviceEmergencia.salvar(criarEmergencia(), usuarios.get(0).getId());

		List<Emergencia> emergencias = repoEmergencia.findAll();
		assertThat(emergencias.get(0), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarContato("Larissa Thuanny"));

		this.mockMvc
				.perform(post("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O contato foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Contato> contatos = repoContato.findAll();
		assertThat(contatos.get(0), notNullValue());

		this.mockMvc.perform(get("/contatos/" + contatos.get(0).getId().toString()))
				.andExpect(jsonPath("$.nome", equalTo("Larissa Thuanny"))).andExpect(status().isOk());

		this.mockMvc.perform(get("/contatos/" + new ContatoId().toString()))
				.andExpect(jsonPath("$.error", equalTo("O contato procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testBurcarTodos() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		serviceEmergencia.salvar(criarEmergencia(), usuarios.get(0).getId());

		List<Emergencia> emergencias = repoEmergencia.findAll();
		assertThat(emergencias.get(0), notNullValue());

		this.mockMvc.perform(get("/contatos")).andExpect(status().isNotFound());

		String jsonString = objectMapper.writeValueAsString(criarContato("Larissa Thuanny"));

		this.mockMvc
				.perform(post("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O contato foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarContato("Wagner Junior"));

		this.mockMvc
				.perform(post("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O contato foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Contato> contatos = repoContato.findAll();
		assertThat(contatos.get(0), notNullValue());

		this.mockMvc.perform(get("/contatos").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$[0].nome", equalTo("Larissa Thuanny")))
				.andExpect(jsonPath("$[1].nome", equalTo("Wagner Junior"))).andExpect(status().isOk());

	}

	@Test
	public void testDeletar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		serviceEmergencia.salvar(criarEmergencia(), usuarios.get(0).getId());

		List<Emergencia> emergencias = repoEmergencia.findAll();
		assertThat(emergencias.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarContato("Larissa Thuanny"));

		this.mockMvc
				.perform(post("/contatos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O contato foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Contato> contatos = repoContato.findAll();
		assertThat(contatos.get(0), notNullValue());

		this.mockMvc
				.perform(delete("/contatos/" + contatos.get(0).getId().toString()).header("token",
						logar("wagnerju", "1234")))
				.andExpect(jsonPath("$",
						equalTo("Contato ===> " + contatos.get(0).getId().toString() + ": deletado com sucesso")))
				.andExpect(status().isOk());

		this.mockMvc
				.perform(delete("/contatos/" + contatos.get(0).getId().toString()).header("token",
						logar("wagnerju", "1234") + "ErroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(delete("/contatos/" + new ContatoId().toString()).header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error", equalTo("O contato a deletar não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(delete("/contatos/" + new ContatoId().toString()).header("token",
						logar("wagnerju", "1234") + "ErroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());
	}

	private EditarContato editarContatoError(ContatoId id) {
		EditarContato error = new EditarContato();
		error.setId(id);
		return error;
	}

	private EditarContato editarContatoError1(Contato contato) {
		EditarContato contatoEditado = new EditarContato();
		contatoEditado.setId(contato.getId());
		contatoEditado.setTelefone(editarTelefone(contato.getIdTelefone()));

		return contatoEditado;
	}

	private EditarContato editarContatoError2(Contato contato) {
		EditarContato contatoEditado = new EditarContato();
		contatoEditado.setId(contato.getId());
		contatoEditado.setNome("Wagner Junior");

		return contatoEditado;
	}

	private EditarContato editarContatoError3(Contato contato) {
		EditarContato contatoEditado = new EditarContato();
		contatoEditado.setId(new ContatoId());
		contatoEditado.setNome("Wagner Junior");
		contatoEditado.setTelefone(editarTelefone(contato.getIdTelefone()));

		return contatoEditado;
	}

	private EditarContato editarContatoError4(Contato contato) {
		EditarContato contatoEditado = new EditarContato();
		contatoEditado.setId(new ContatoId());
		contatoEditado.setTelefone(editarTelefone(contato.getIdTelefone()));

		return contatoEditado;
	}

	private EditarContato editarContatoError5(Contato contato) {
		EditarContato contatoEditado = new EditarContato();
		contatoEditado.setId(new ContatoId());
		contatoEditado.setNome("Wagner Junior");

		return contatoEditado;
	}

	private EditarContato editarContatoError6(Contato contato) {
		EditarContato contatoEditado = new EditarContato();
		contatoEditado.setId(new ContatoId());

		return contatoEditado;
	}

	private CriarEmergencia criarEmergencia() {
		CriarEmergencia emergencia = new CriarEmergencia();
		emergencia.setAtaqueConvulsivos(1);
		emergencia.setDoadorDeOrgaos(1);
		emergencia.setProblemasCardiacos("Não");
		return emergencia;
	}

	private CriarContato criarContato(String nome) {
		CriarContato contato = new CriarContato();
		contato.setTelefone(telefone());
		contato.setNome(nome);
		return contato;
	}

	private CriarContato criarContatoError1(String nome) {
		CriarContato contato = new CriarContato();
		contato.setNome(nome);
		return contato;
	}

	private CriarContato criarContatoError2(String nome) {
		CriarContato contato = new CriarContato();
		return contato;
	}

	private CriarTelefone telefone() {
		CriarTelefone telefone = new CriarTelefone();
		telefone.setDdd(44);
		telefone.setNumero(997703828);
		return telefone;
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

	private String logar(String nomeUsuario, String senha) {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginUsuario(corpoLogin).getBody();
	}

	private EditarContato editarContato(Contato contato) {
		EditarContato contatoEditado = new EditarContato();
		contatoEditado.setId(contato.getId());
		contatoEditado.setNome("Wagner Junior");
		contatoEditado.setTelefone(editarTelefone(contato.getIdTelefone()));

		return contatoEditado;
	}

	private EditarTelefone editarTelefone(TelefoneId id) {
		EditarTelefone telefone = new EditarTelefone();
		telefone.setId(id);
		telefone.setDdd(44);
		telefone.setNumero(997703828);
		return telefone;
	}

}
