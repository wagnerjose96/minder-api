package br.minder.test.alarme.alarme_hora;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import br.minder.alarme.AlarmeService;
import br.minder.alarme.alarme_hora.comandos.CriarAlarmeHora;
import br.minder.alarme.comandos.CriarAlarme;
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
import br.minder.usuario.UsuarioId;
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
public class TestAlarmeHoraController {

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
	private UsuarioRepository repoUsuario;

	@Autowired
	private MedicamentoService serviceMedicamento;

	@Autowired
	private AlarmeService serviceAlarme;

	@Autowired
	private AlarmeRepository repo;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		UsuarioId usuarioId = serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue))
				.get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "1mg")).get();

		repoUsuario.findAll();

		AlarmeId idAlarme = serviceAlarme.salvar(criarAlarme(idMedicamento, "Tomar medicamento"), usuarioId).get();

		final String jsonString = objectMapper.writeValueAsString(criarAlarmeHora(idAlarme));

		this.mockMvc.perform(post("/api/alarme/hora").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O horário do alarme foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		this.mockMvc
				.perform(post("/api/alarme/hora").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "erroToken").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		String erro = objectMapper.writeValueAsString(criarAlarmeHoraErro());

		this.mockMvc.perform(post("/api/alarme/hora").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("token", logar("wagnerju", "1234")).content(erro))
				.andExpect(jsonPath("$.error", equalTo("O alarme hora não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		erro = objectMapper.writeValueAsString(criarAlarmeHoraErro1(idAlarme));

		this.mockMvc.perform(post("/api/alarme/hora").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("token", logar("wagnerju", "1234")).content(erro))
				.andExpect(jsonPath("$.error", equalTo("O alarme hora não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		UsuarioId usuarioId = serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue))
				.get();

		serviceUsuario.salvar(criarUsuario("lathuanny@hotmail.com", "lathuanny", idGenero, idSangue));

		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "1mg")).get();

		repoUsuario.findAll();

		AlarmeId idAlarme = serviceAlarme.salvar(criarAlarme(idMedicamento, "Tomar medicamento"), usuarioId).get();

		final String jsonString = objectMapper.writeValueAsString(criarAlarmeHora(idAlarme));

		this.mockMvc.perform(post("/api/alarme/hora").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O horário do alarme foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Alarme> alarmes = repo.findAll();

		assertThat(alarmes.get(0), notNullValue());

		this.mockMvc.perform(
				get("/api/alarme/hora/" + alarmes.get(0).getId().toString()).header("token", logar("wagnerju", "1234")))
				.andExpect(status().isOk());

		this.mockMvc
				.perform(get("/api/alarme/hora/" + alarmes.get(0).getId().toString()).header("token",
						logar("lathuanny", "1234")))
				.andExpect(jsonPath("$.error", equalTo("O alarme hora procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(get("/api/alarme/hora/" + alarmes.get(0).getId().toString()).header("token",
						logar("wagnerju", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc.perform(get("/api/alarme/hora/" + new AlarmeId()).header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error", equalTo("O alarme hora procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testBurcarTodos() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		UsuarioId usuarioId = serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue))
				.get();

		serviceUsuario.salvar(criarUsuario("lathuanny@hotmail.com", "lathuanny", idGenero, idSangue));

		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "1mg")).get();

		repoUsuario.findAll();

		AlarmeId idAlarme = serviceAlarme.salvar(criarAlarme(idMedicamento, "Tomar medicamento"), usuarioId).get();

		final String jsonString = objectMapper.writeValueAsString(criarAlarmeHora(idAlarme));

		this.mockMvc.perform(post("/api/alarme/hora").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O horário do alarme foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Alarme> alarmes = repo.findAll();

		assertThat(alarmes.get(0), notNullValue());

		this.mockMvc.perform(get("/api/alarme/hora").header("token", logar("wagnerju", "1234")))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/api/alarme/hora").header("token", logar("lathuanny", "1234")))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhum alarme cadastrado no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc.perform(get("/api/alarme/hora").header("token", logar("wagnerju", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());
	}

	private CriarAlarmeHora criarAlarmeHora(AlarmeId idAlarme) {
		CriarAlarmeHora alarmeHora = new CriarAlarmeHora();
		alarmeHora.setDataAlarme(Date.valueOf(LocalDate.now()));
		alarmeHora.setIdAlarme(idAlarme);
		return alarmeHora;
	}

	private CriarAlarmeHora criarAlarmeHoraErro() {
		CriarAlarmeHora alarmeHora = new CriarAlarmeHora();
		alarmeHora.setDataAlarme(Date.valueOf(LocalDate.now()));
		return alarmeHora;
	}

	private CriarAlarmeHora criarAlarmeHoraErro1(AlarmeId idAlarme) {
		CriarAlarmeHora alarmeHora = new CriarAlarmeHora();
		alarmeHora.setIdAlarme(idAlarme);
		return alarmeHora;
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
