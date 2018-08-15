package br.minder.test.doenca;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import br.minder.doenca.Doenca;
import br.minder.doenca.DoencaId;
import br.minder.doenca.DoencaRepository;
import br.minder.doenca.comandos.CriarDoenca;
import br.minder.doenca.comandos.EditarDoenca;
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.genero.Genero;
import br.minder.genero.GeneroId;
import br.minder.genero.GeneroRepository;
import br.minder.genero.comandos.CriarGenero;
import br.minder.login.LoginController;
import br.minder.login.comandos.LogarUsuario;
import br.minder.medicamento.MedicamentoId;
import br.minder.medicamento.MedicamentoService;
import br.minder.medicamento.comandos.CriarMedicamento;
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

public class TestDoencaController {

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
	private MedicamentoService serviceMedicamento;

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private DoencaRepository repoDoenca;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		Set<MedicamentoId> idsMedicamentos = new HashSet<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarDoenca(idsMedicamentos, "Miopia"));

		this.mockMvc
				.perform(post("/api/doenca").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A doença foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		this.mockMvc
				.perform(post("/api/doenca").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "TokenError").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		String error = objectMapper.writeValueAsString(new CriarDoenca());

		this.mockMvc
				.perform(post("/api/doenca").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A doença não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testEditar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		Set<MedicamentoId> idsMedicamentos = new HashSet<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarDoenca(idsMedicamentos, "Miopia"));

		this.mockMvc
				.perform(post("/api/doenca").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A doença foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Doenca> doencas = repoDoenca.findAll();
		assertThat(doencas.get(0), notNullValue());

		String error = objectMapper.writeValueAsString(editarDoencaError(doencas.get(0).getIdDoenca()));

		this.mockMvc
				.perform(put("/api/doenca").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração da doença")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(editarDoencaError());

		this.mockMvc
				.perform(put("/api/doenca").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A doença a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarDoenca(doencas.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/api/doenca").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A doença foi alterada com sucesso"))).andExpect(status().isOk());

		this.mockMvc
				.perform(put("/api/doenca").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "TokenError").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

	}

	@Test
	public void testBuscarPorId() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		Set<MedicamentoId> idsMedicamentos = new HashSet<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarDoenca(idsMedicamentos, "Miopia"));

		this.mockMvc
				.perform(post("/api/doenca").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A doença foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Doenca> doencas = repoDoenca.findAll();
		assertThat(doencas.get(0), notNullValue());

		this.mockMvc
				.perform(get("/api/doenca/" + doencas.get(0).getIdDoenca().toString()).header("token",
						logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.nomeDoenca", equalTo("Miopia"))).andExpect(status().isOk());

		this.mockMvc.perform(get("/api/doenca/" + new DoencaId().toString()).header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error", equalTo("A doença procurada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(get("/api/doenca/" + doencas.get(0).getIdDoenca().toString()).header("token",
						logar("wagnerju", "1234") + "TokenError"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());
	}

	@Test
	public void testBurcarTodos() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		Set<MedicamentoId> idsMedicamentos = new HashSet<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());
		
		this.mockMvc.perform(get("/api/doenca").header("token", logar("wagnerju", "1234")))
		.andExpect(jsonPath("$.error", equalTo("Não existe nenhuma doença cadastrada no banco de dados")))
		.andExpect(status().isNotFound());

		String jsonString = objectMapper.writeValueAsString(criarDoenca(idsMedicamentos, "Miopia"));

		this.mockMvc
				.perform(post("/api/doenca").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A doença foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarDoenca(idsMedicamentos, "Asma"));

		this.mockMvc
				.perform(post("/api/doenca").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A doença foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Doenca> doencas = repoDoenca.findAll();
		assertThat(doencas.get(0), notNullValue());

		this.mockMvc.perform(get("/api/doenca").header("token", logar("wagnerju", "1234"))).andExpect(status().isOk());

		this.mockMvc.perform(get("/api/doenca").header("token", logar("wagnerju", "1234") + "TokenError"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());
		
		this.mockMvc.perform(
				get("/api/doenca").param("searchTerm", "Asma").header("token", logar("wagnerju", "1234")))
				.andExpect(status().isOk());
	}

	private CriarDoenca criarDoenca(Set<MedicamentoId> idsMedicamentos, String nomeDoenca) {
		CriarDoenca doenca = new CriarDoenca();
		doenca.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		doenca.setIdMedicamentos(idsMedicamentos);
		doenca.setNomeDoenca(nomeDoenca);
		return doenca;
	}

	private EditarDoenca editarDoencaError() {
		EditarDoenca error = new EditarDoenca();
		error.setIdDoenca(new DoencaId());
		return error;
	}

	private EditarDoenca editarDoencaError(DoencaId idDoenca) {
		EditarDoenca error = new EditarDoenca();
		error.setIdDoenca(idDoenca);
		return error;
	}

	private EditarDoenca editarDoenca(Doenca doenca, Set<MedicamentoId> idsMedicamentos) {
		EditarDoenca doencaEditada = new EditarDoenca();
		doencaEditada.setIdDoenca(doenca.getIdDoenca());
		doencaEditada.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 20)));
		doencaEditada.setIdMedicamentos(idsMedicamentos);
		doencaEditada.setNomeDoenca("Asma");
		return doencaEditada;
	}

	private CriarMedicamento criarMedicamento(String nome, String composicao) {
		CriarMedicamento medicamento = new CriarMedicamento();
		medicamento.setComposicao(composicao);
		medicamento.setNomeMedicamento(nome);
		return medicamento;
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

	private String logar(String nomeUsuario, String senha) throws NoSuchAlgorithmException {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginUsuario(corpoLogin).getBody();
	}

}
