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
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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
import br.minder.alarme.Alarme;
import br.minder.alarme.AlarmeId;
import br.minder.alarme.AlarmeRepository;
import br.minder.alarme.comandos.CriarAlarme;
import br.minder.alarme.comandos.EditarAlarme;
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
@ActiveProfiles("application-test")
public class TestAlarmeController {

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
	private AlarmeRepository repoAlarme;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "1mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarAlarme(idMedicamento, "Tomar medicamento"));
		String erro = objectMapper.writeValueAsString(new CriarAlarme());

		this.mockMvc
				.perform(post("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(erro))
				.andExpect(jsonPath("$.error", equalTo("O alarme não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		this.mockMvc
				.perform(post("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "erroToken").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(post("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagner@hotmail.com", "1234")).content(jsonString))
				.andExpect(status().isCreated());

		erro = objectMapper.writeValueAsString(criarAlarmeErro1(idMedicamento, "Tomar medicamento"));

		this.mockMvc
				.perform(post("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(erro))
				.andExpect(jsonPath("$.error", equalTo("O alarme não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		erro = objectMapper.writeValueAsString(criarAlarmeErro2(idMedicamento, "Tomar medicamento"));

		this.mockMvc
				.perform(post("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(erro))
				.andExpect(jsonPath("$.error", equalTo("O alarme não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		erro = objectMapper.writeValueAsString(criarAlarmeErro3(idMedicamento));

		this.mockMvc
				.perform(post("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(erro))
				.andExpect(jsonPath("$.error", equalTo("O alarme não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		erro = objectMapper.writeValueAsString(criarAlarmeErro4(idMedicamento, "Tomar medicamento"));

		this.mockMvc
				.perform(post("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(erro))
				.andExpect(jsonPath("$.error", equalTo("O alarme não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		erro = objectMapper.writeValueAsString(criarAlarmeErro5(idMedicamento, "Tomar medicamento"));

		this.mockMvc
				.perform(post("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(erro))
				.andExpect(jsonPath("$.error", equalTo("O alarme não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

	}

	@Test
	public void testEditar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "1mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarAlarme(idMedicamento, "Tomar medicamento"));

		this.mockMvc
				.perform(post("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(status().isCreated());

		List<Alarme> alarmes = repoAlarme.findAll();
		assertThat(alarmes.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarAlarme(alarmes.get(0)));

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O alarme foi alterado com sucesso"))).andExpect(status().isOk());

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "erroToken").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		jsonString = objectMapper.writeValueAsString(editarAlarmeErro(alarmes.get(0)));

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O alarme a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarAlarmeErro1(alarmes.get(0)));

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O alarme a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarAlarmeErro2(alarmes.get(0)));

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O alarme a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarAlarmeErro3(alarmes.get(0)));

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O alarme a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarAlarmeErro4(alarmes.get(0)));

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O alarme a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarAlarmeErro5(alarmes.get(0)));

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O alarme a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarAlarmeErro6());

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O alarme a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarAlarmeErro7(alarmes.get(0)));

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do alarme")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarAlarmeErro8(alarmes.get(0)));

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do alarme")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarAlarmeErro9(alarmes.get(0)));

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do alarme")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarAlarmeErro10(alarmes.get(0)));

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do alarme")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarAlarmeErro11(alarmes.get(0)));

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do alarme")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarAlarmeErro12(alarmes.get(0)));

		this.mockMvc
				.perform(put("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do alarme")))
				.andExpect(status().isInternalServerError());

	}

	@Test
	public void testBuscarPorId() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "1mg")).get();
		serviceUsuario.salvar(criarUsuario("lathuanny@hotmail.com", "lathuanny", idGenero, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());
		assertThat(usuarios.get(1), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarAlarme(idMedicamento, "Tomar medicamento"));

		this.mockMvc
				.perform(post("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(status().isCreated());

		List<Alarme> alarmes = repoAlarme.findAll();
		assertThat(alarmes.get(0), notNullValue());

		this.mockMvc
				.perform(get("/api/alarme/" + alarmes.get(0).getId().toString()).header("token",
						logar("wagnerju", "1234")))

				.andExpect(jsonPath("$.descricao", equalTo("Tomar medicamento"))).andExpect(status().isOk());

		this.mockMvc
				.perform(get("/api/alarme/" + alarmes.get(0).getId().toString()).header("token",
						logar("lathuanny", "1234")))
				.andExpect(jsonPath("$.error", equalTo("O alarme procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(get("/api/alarme/" + alarmes.get(0).getId().toString()).header("token",
						logar("wagnerju", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc.perform(get("/api/alarme/" + new AlarmeId()).header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error", equalTo("O alarme procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testBurcarTodos() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "1mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		this.mockMvc.perform(get("/api/alarme").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhum alarme cadastrado no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc.perform(get("/api/alarme").header("token", logar("wagnerju", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		String jsonString = objectMapper.writeValueAsString(criarAlarme(idMedicamento, "Tomar medicamento"));

		this.mockMvc
				.perform(post("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarAlarme(idMedicamento, "Aplicar medicamento"));

		this.mockMvc
				.perform(post("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(status().isCreated());

		List<Alarme> alarmes = repoAlarme.findAll();
		assertThat(alarmes.get(0), notNullValue());
		assertThat(alarmes.get(1), notNullValue());

		this.mockMvc.perform(get("/api/alarme").header("token", logar("wagnerju", "1234"))).andExpect(status().isOk());

		this.mockMvc.perform(get("/api/alarme").param("searchTerm", "Aplicar medicamento").header("token",
				logar("wagnerju", "1234"))).andExpect(status().isOk());
	}

	@Test
	public void testDeletar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "1mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarAlarme(idMedicamento, "Tomar medicamento"));

		this.mockMvc
				.perform(post("/api/alarme").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(status().isCreated());

		List<Alarme> alarmes = repoAlarme.findAll();
		assertThat(alarmes.get(0), notNullValue());

		this.mockMvc
				.perform(delete("/api/alarme/" + alarmes.get(0).getId().toString()).header("token",
						logar("wagnerju", "1234")))
				.andExpect(jsonPath("$",
						equalTo("Alarme ===> " + alarmes.get(0).getId().toString() + ": deletado com sucesso")))
				.andExpect(status().isOk());

		this.mockMvc
				.perform(delete("/api/alarme/" + alarmes.get(0).getId().toString()).header("token",
						logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error", equalTo("O alarme a ser deletado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(delete("/api/alarme/" + new AlarmeId().toString()).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error", equalTo("O alarme a ser deletado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(delete("/api/alarme/" + new AlarmeId().toString()).header("token",
						logar("wagnerju", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

	}

	private CriarAlarme criarAlarme(MedicamentoId idMedicamento, String descrição) {
		CriarAlarme alarme = new CriarAlarme();
		alarme.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarme.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarme.setDescricao(descrição);
		alarme.setIdMedicamento(idMedicamento);
		alarme.setPeriodicidade(8);
		alarme.setQuantidade("1");
		alarme.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		return alarme;
	}

	private CriarAlarme criarAlarmeErro1(MedicamentoId idMedicamento, String descrição) {
		CriarAlarme alarme = new CriarAlarme();
		alarme.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarme.setDescricao(descrição);
		alarme.setIdMedicamento(idMedicamento);
		alarme.setPeriodicidade(8);
		alarme.setQuantidade("1");
		alarme.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		return alarme;
	}

	private CriarAlarme criarAlarmeErro2(MedicamentoId idMedicamento, String descrição) {
		CriarAlarme alarme = new CriarAlarme();
		alarme.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarme.setDescricao(descrição);
		alarme.setIdMedicamento(idMedicamento);
		alarme.setPeriodicidade(8);
		alarme.setQuantidade("1");
		alarme.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		return alarme;
	}

	private CriarAlarme criarAlarmeErro3(MedicamentoId idMedicamento) {
		CriarAlarme alarme = new CriarAlarme();
		alarme.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarme.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarme.setIdMedicamento(idMedicamento);
		alarme.setPeriodicidade(8);
		alarme.setQuantidade("1");
		alarme.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		return alarme;
	}

	private CriarAlarme criarAlarmeErro4(MedicamentoId idMedicamento, String descrição) {
		CriarAlarme alarme = new CriarAlarme();
		alarme.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarme.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarme.setDescricao(descrição);
		alarme.setPeriodicidade(8);
		alarme.setQuantidade("1");
		alarme.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		return alarme;
	}

	private CriarAlarme criarAlarmeErro5(MedicamentoId idMedicamento, String descrição) {
		CriarAlarme alarme = new CriarAlarme();
		alarme.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarme.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarme.setDescricao(descrição);
		alarme.setIdMedicamento(idMedicamento);
		alarme.setPeriodicidade(8);
		alarme.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		return alarme;
	}

	private EditarAlarme editarAlarmeErro(Alarme alarme) {
		EditarAlarme editar = new EditarAlarme();
		editar.setId(new AlarmeId());
		editar.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		editar.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		editar.setDescricao("Erro");
		editar.setIdMedicamento(alarme.getIdMedicamento());
		editar.setPeriodicidade(8);
		editar.setQuantidade("1");
		editar.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		editar.setHoraUltimaDose(Time.valueOf(LocalTime.of(16, 12)));
		return editar;
	}

	private EditarAlarme editarAlarmeErro1(Alarme alarme) {
		EditarAlarme editar = new EditarAlarme();
		editar.setId(new AlarmeId());
		editar.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		editar.setDescricao("Erro");
		editar.setIdMedicamento(alarme.getIdMedicamento());
		editar.setPeriodicidade(8);
		editar.setQuantidade("1");
		editar.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		editar.setHoraUltimaDose(Time.valueOf(LocalTime.of(16, 12)));
		return editar;
	}

	private EditarAlarme editarAlarmeErro2(Alarme alarme) {
		EditarAlarme editar = new EditarAlarme();
		editar.setId(new AlarmeId());
		editar.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		editar.setDescricao("Erro");
		editar.setIdMedicamento(alarme.getIdMedicamento());
		editar.setPeriodicidade(8);
		editar.setQuantidade("1");
		editar.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		editar.setHoraUltimaDose(Time.valueOf(LocalTime.of(16, 12)));
		return editar;
	}

	private EditarAlarme editarAlarmeErro3(Alarme alarme) {
		EditarAlarme editar = new EditarAlarme();
		editar.setId(new AlarmeId());
		editar.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		editar.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		editar.setIdMedicamento(alarme.getIdMedicamento());
		editar.setPeriodicidade(8);
		editar.setQuantidade("1");
		editar.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		editar.setHoraUltimaDose(Time.valueOf(LocalTime.of(16, 12)));
		return editar;
	}

	private EditarAlarme editarAlarmeErro4(Alarme alarme) {
		EditarAlarme editar = new EditarAlarme();
		editar.setId(new AlarmeId());
		editar.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		editar.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		editar.setDescricao("Erro");
		editar.setPeriodicidade(8);
		editar.setQuantidade("1");
		editar.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		editar.setHoraUltimaDose(Time.valueOf(LocalTime.of(16, 12)));
		return editar;
	}

	private EditarAlarme editarAlarmeErro5(Alarme alarme) {
		EditarAlarme editar = new EditarAlarme();
		editar.setId(new AlarmeId());
		editar.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		editar.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		editar.setDescricao("Erro");
		editar.setIdMedicamento(alarme.getIdMedicamento());
		editar.setPeriodicidade(8);
		editar.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		editar.setHoraUltimaDose(Time.valueOf(LocalTime.of(16, 12)));
		return editar;
	}

	private EditarAlarme editarAlarmeErro6() {
		EditarAlarme editar = new EditarAlarme();
		editar.setId(new AlarmeId());
		return editar;
	}

	private EditarAlarme editarAlarmeErro7(Alarme alarme) {
		EditarAlarme alarmeEditado = new EditarAlarme();
		alarmeEditado.setId(alarme.getId());
		alarmeEditado.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarmeEditado.setDescricao("Tomar medicamento Editado !!!");
		alarmeEditado.setIdMedicamento(alarme.getIdMedicamento());
		alarmeEditado.setPeriodicidade(8);
		alarmeEditado.setQuantidade("1");
		alarmeEditado.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		alarmeEditado.setHoraUltimaDose(Time.valueOf(LocalTime.of(16, 12)));
		return alarmeEditado;
	}

	private EditarAlarme editarAlarmeErro8(Alarme alarme) {
		EditarAlarme alarmeEditado = new EditarAlarme();
		alarmeEditado.setId(alarme.getId());
		alarmeEditado.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarmeEditado.setDescricao("Tomar medicamento Editado !!!");
		alarmeEditado.setIdMedicamento(alarme.getIdMedicamento());
		alarmeEditado.setPeriodicidade(8);
		alarmeEditado.setQuantidade("1");
		alarmeEditado.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		alarmeEditado.setHoraUltimaDose(Time.valueOf(LocalTime.of(16, 12)));
		return alarmeEditado;
	}

	private EditarAlarme editarAlarmeErro9(Alarme alarme) {
		EditarAlarme alarmeEditado = new EditarAlarme();
		alarmeEditado.setId(alarme.getId());
		alarmeEditado.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarmeEditado.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarmeEditado.setIdMedicamento(alarme.getIdMedicamento());
		alarmeEditado.setPeriodicidade(8);
		alarmeEditado.setQuantidade("1");
		alarmeEditado.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		alarmeEditado.setHoraUltimaDose(Time.valueOf(LocalTime.of(16, 12)));
		return alarmeEditado;
	}

	private EditarAlarme editarAlarmeErro10(Alarme alarme) {
		EditarAlarme alarmeEditado = new EditarAlarme();
		alarmeEditado.setId(alarme.getId());
		alarmeEditado.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarmeEditado.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarmeEditado.setDescricao("Tomar medicamento Editado !!!");
		alarmeEditado.setPeriodicidade(8);
		alarmeEditado.setQuantidade("1");
		alarmeEditado.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		alarmeEditado.setHoraUltimaDose(Time.valueOf(LocalTime.of(16, 12)));
		return alarmeEditado;
	}

	private EditarAlarme editarAlarmeErro11(Alarme alarme) {
		EditarAlarme alarmeEditado = new EditarAlarme();
		alarmeEditado.setId(alarme.getId());
		alarmeEditado.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarmeEditado.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarmeEditado.setDescricao("Tomar medicamento Editado !!!");
		alarmeEditado.setIdMedicamento(alarme.getIdMedicamento());
		alarmeEditado.setPeriodicidade(8);
		alarmeEditado.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		alarmeEditado.setHoraUltimaDose(Time.valueOf(LocalTime.of(16, 12)));
		return alarmeEditado;
	}

	private EditarAlarme editarAlarmeErro12(Alarme alarme) {
		EditarAlarme alarmeEditado = new EditarAlarme();
		alarmeEditado.setId(alarme.getId());
		return alarmeEditado;
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

	private EditarAlarme editarAlarme(Alarme alarme) {
		EditarAlarme alarmeEditado = new EditarAlarme();
		alarmeEditado.setId(alarme.getId());
		alarmeEditado.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarmeEditado.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarmeEditado.setDescricao("Tomar medicamento Editado !!!");
		alarmeEditado.setIdMedicamento(alarme.getIdMedicamento());
		alarmeEditado.setPeriodicidade(8);
		alarmeEditado.setQuantidade("1");
		alarmeEditado.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		alarmeEditado.setHoraUltimaDose(Time.valueOf(LocalTime.of(16, 12)));
		return alarmeEditado;
	}

}