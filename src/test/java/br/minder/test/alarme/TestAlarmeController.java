package br.minder.test.alarme;

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
import br.minder.alarme.Alarme;
import br.minder.alarme.AlarmeRepository;
import br.minder.alarme.comandos.CriarAlarme;
import br.minder.alarme.comandos.EditarAlarme;
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.login.LoginController;
import br.minder.login.comandos.LogarUsuario;
import br.minder.medicamento.MedicamentoId;
import br.minder.medicamento.MedicamentoService;
import br.minder.medicamento.comandos.CriarMedicamento;
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

@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
public class TestAlarmeController {

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
	private MedicamentoService serviceMedicamento;

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private AlarmeRepository repoAlarme;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarAlarme(idMedicamento, "Tomar medicamento"));

		this.mockMvc
				.perform(post("/alarmes").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O alarme foi cadastrado com sucesso")))
				.andExpect(status().isCreated());
	}

	@Test
	public void testEditar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarAlarme(idMedicamento, "Tomar medicamento"));

		this.mockMvc
				.perform(post("/alarmes").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O alarme foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Alarme> alarmes = repoAlarme.findAll();
		assertThat(alarmes.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarAlarme(alarmes.get(0)));

		this.mockMvc
				.perform(put("/alarmes").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O alarme foi alterado com sucesso"))).andExpect(status().isOk());

	}

	@Test
	public void testBuscarPorId() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarAlarme(idMedicamento, "Tomar medicamento"));

		this.mockMvc
				.perform(post("/alarmes").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O alarme foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Alarme> alarmes = repoAlarme.findAll();
		assertThat(alarmes.get(0), notNullValue());

		this.mockMvc
				.perform(
						get("/alarmes/" + alarmes.get(0).getId().toString()).header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.descricao", equalTo("Tomar medicamento"))).andExpect(status().isOk());
	}

	@Test
	public void testBurcarTodos() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarAlarme(idMedicamento, "Tomar medicamento"));

		this.mockMvc
				.perform(post("/alarmes").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O alarme foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarAlarme(idMedicamento, "Aplicar medicamento"));

		this.mockMvc
				.perform(post("/alarmes").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O alarme foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Alarme> alarmes = repoAlarme.findAll();
		assertThat(alarmes.get(0), notNullValue());

		this.mockMvc.perform(get("/alarmes").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$[0].descricao", equalTo("Tomar medicamento")))
				.andExpect(jsonPath("$[1].descricao", equalTo("Aplicar medicamento"))).andExpect(status().isOk());
	}

	@Test
	public void testDeletar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarAlarme(idMedicamento, "Tomar medicamento"));

		this.mockMvc
				.perform(post("/alarmes").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O alarme foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Alarme> alarmes = repoAlarme.findAll();
		assertThat(alarmes.get(0), notNullValue());

		this.mockMvc
				.perform(delete("/alarmes/" + alarmes.get(0).getId().toString()).header("token",
						logar("wagnerju", "1234")))
				.andExpect(jsonPath("$",
						equalTo("Alarme ===> " + alarmes.get(0).getId().toString() + ": deletado com sucesso")))
				.andExpect(status().isOk());
	}

	private CriarAlarme criarAlarme(MedicamentoId idMedicamento, String descrição) {
		CriarAlarme alarme = new CriarAlarme();
		alarme.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarme.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarme.setDescricao(descrição);
		alarme.setIdMedicamento(idMedicamento);
		alarme.setPeriodicidade(8);
		alarme.setQuantidade("1");
		return alarme;
	}

	private CriarMedicamento criarMedicamento(String nome, String composicao) {
		CriarMedicamento medicamento = new CriarMedicamento();
		medicamento.setComposicao(composicao);
		medicamento.setNomeMedicamento(nome);
		return medicamento;
	}

	private CriarUsuario criarUsuario(String email, String username, SexoId idSexo, SangueId idSangue) {
		CriarEndereco endereco = new CriarEndereco();
		endereco.setBairro("Zona 6");
		endereco.setCidade("Maringá");
		endereco.setEstado("Paraná");
		endereco.setNumero(1390);
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

	private EditarAlarme editarAlarme(Alarme alarme) {
		EditarAlarme alarmeEditado = new EditarAlarme();
		alarmeEditado.setId(alarme.getId());
		alarmeEditado.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarmeEditado.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarmeEditado.setDescricao("Tomar medicamento Editado !!!");
		alarmeEditado.setIdMedicamento(alarme.getIdMedicamento());
		alarmeEditado.setPeriodicidade(8);
		alarmeEditado.setQuantidade("1");
		return alarmeEditado;
	}

}