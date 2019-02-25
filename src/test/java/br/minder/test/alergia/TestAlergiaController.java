package br.minder.test.alergia;

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
import br.minder.alergia.Alergia;
import br.minder.alergia.AlergiaId;
import br.minder.alergia.AlergiaRepository;
import br.minder.alergia.comandos.CriarAlergia;
import br.minder.alergia.comandos.EditarAlergia;
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
public class TestAlergiaController {

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
	private AlergiaRepository repoAlergia;

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
		MedicamentoId idMedicamento1 = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		Set<MedicamentoId> idsMedicamentos = new HashSet<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);
		idsMedicamentos.add(idMedicamento);
		idsMedicamentos.add(idMedicamento1);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarAlergia(idsMedicamentos, "Alergia de pele"));
		String error = objectMapper.writeValueAsString(new CriarAlergia());

		this.mockMvc
				.perform(post("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A alergia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		this.mockMvc
				.perform(post("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "TokenErrado").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(post("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(criarAlergiaErro1(idsMedicamentos, "Alergia de pele"));

		this.mockMvc
				.perform(post("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(criarAlergiaErro2(idsMedicamentos, "Alergia de pele"));

		this.mockMvc
				.perform(post("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(criarAlergiaErro3(idsMedicamentos, "Alergia de pele"));

		this.mockMvc
				.perform(post("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(criarAlergiaErro4(idsMedicamentos, "Alergia de pele"));

		this.mockMvc
				.perform(post("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(criarAlergiaErro5(idsMedicamentos, "Alergia de pele"));

		this.mockMvc
				.perform(post("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		Set<MedicamentoId> idsMedicamentos2 = new HashSet<MedicamentoId>();
		idsMedicamentos2.add(new MedicamentoId());

		error = objectMapper.writeValueAsString(criarAlergiaErro6(idsMedicamentos2, "Alergia de pele"));

		this.mockMvc
				.perform(post("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$", equalTo("A alergia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

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

		String jsonString = objectMapper.writeValueAsString(criarAlergia(idsMedicamentos, "Alergia de pele"));

		this.mockMvc
				.perform(post("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A alergia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Alergia> alergias = repoAlergia.findAll();
		assertThat(alergias.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarAlergia(alergias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A alergia foi alterada com sucesso"))).andExpect(status().isOk());

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "TokenErrado").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		String error = objectMapper.writeValueAsString(editarAlergiaError(alergias.get(0).getIdAlergia()));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração da alergia")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(editarAlergiaError(alergias.get(0).getIdAlergia()));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração da alergia")))
				.andExpect(status().isInternalServerError());

		serviceUsuario.salvar(criarUsuario("lathuanny@hotmail.com", "lathuanny", idGenero, idSangue)).get();
		usuarios = repo.findAll();

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("lathuanny", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		error = objectMapper.writeValueAsString(editarAlergiaError1(alergias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração da alergia")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(editarAlergiaError2(alergias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração da alergia")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(editarAlergiaError3(alergias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração da alergia")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(editarAlergiaError4(alergias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração da alergia")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(editarAlergiaError5(alergias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração da alergia")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(editarAlergiaError6());

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		error = objectMapper.writeValueAsString(editarAlergiaError7(alergias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		error = objectMapper.writeValueAsString(editarAlergiaError8(alergias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		error = objectMapper.writeValueAsString(editarAlergiaError9(alergias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		error = objectMapper.writeValueAsString(editarAlergiaError10(alergias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		error = objectMapper.writeValueAsString(editarAlergiaError11(alergias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		idsMedicamentos.add(new MedicamentoId());

		error = objectMapper.writeValueAsString(editarAlergiaError12(alergias.get(0), idsMedicamentos));

		this.mockMvc.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.header("token", logar("wagnerju", "1234")).content(error)).andExpect(status().isOk());

		error = objectMapper.writeValueAsString(editarAlergiaError13(alergias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A alergia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		MedicamentoId idMedicamento1 = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		Set<MedicamentoId> idsMedicamentos = new HashSet<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);
		idsMedicamentos.add(idMedicamento);
		idsMedicamentos.add(idMedicamento1);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarAlergia(idsMedicamentos, "Alergia de pele"));

		this.mockMvc
				.perform(post("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A alergia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Alergia> alergias = repoAlergia.findAll();
		assertThat(alergias.get(0), notNullValue());

		this.mockMvc
				.perform(get("/api/alergia/" + alergias.get(0).getIdAlergia().toString()).header("token",
						logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.tipoAlergia", equalTo("Alergia de pele"))).andExpect(status().isOk());

		this.mockMvc
				.perform(get("/api/alergia/" + alergias.get(0).getIdAlergia().toString()).header("token",
						logar("wagnerju", "1234") + "TokenError"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc.perform(get("/api/alergia/" + new AlergiaId().toString()).header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error", equalTo("A alergia procurada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		serviceUsuario.salvar(criarUsuario("lathuanny@hotmail.com", "lathuanny", idGenero, idSangue)).get();
		usuarios = repo.findAll();

		this.mockMvc
				.perform(get("/api/alergia/" + alergias.get(0).getIdAlergia().toString()).header("token",
						logar("lathuanny", "1234")))
				.andExpect(jsonPath("$.error", equalTo("A alergia procurada não existe no banco de dados")))
				.andExpect(status().isNotFound());
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

		this.mockMvc.perform(get("/api/alergia").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhuma alergia cadastrada no banco de dados")))
				.andExpect(status().isNotFound());

		String jsonString = objectMapper.writeValueAsString(criarAlergia(idsMedicamentos, "Alergia de pele"));

		this.mockMvc
				.perform(post("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A alergia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarAlergia(idsMedicamentos, "Alergia nas pernas"));

		this.mockMvc
				.perform(post("/api/alergia").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A alergia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Alergia> alergias = repoAlergia.findAll();
		assertThat(alergias.get(0), notNullValue());

		this.mockMvc.perform(get("/api/alergia").header("token", logar("wagnerju", "1234"))).andExpect(status().isOk());

		this.mockMvc.perform(
				get("/api/alergia").param("searchTerm", "Alergia nas pernas").header("token", logar("wagnerju", "1234")))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/api/alergia").header("token", logar("wagnerju", "1234") + "TokenError"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());
	}

	private CriarAlergia criarAlergia(Set<MedicamentoId> idsMedicamentos, String tipoAlergia) {
		CriarAlergia alergia = new CriarAlergia();
		alergia.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergia.setEfeitos("Coceira");
		alergia.setIdMedicamentos(idsMedicamentos);
		alergia.setLocalAfetado("Braços");
		alergia.setTipoAlergia(tipoAlergia);
		return alergia;
	}

	private CriarAlergia criarAlergiaErro1(Set<MedicamentoId> idsMedicamentos, String tipoAlergia) {
		CriarAlergia alergia = new CriarAlergia();
		alergia.setEfeitos("Coceira");
		alergia.setIdMedicamentos(idsMedicamentos);
		alergia.setLocalAfetado("Braços");
		alergia.setTipoAlergia(tipoAlergia);
		return alergia;
	}

	private CriarAlergia criarAlergiaErro2(Set<MedicamentoId> idsMedicamentos, String tipoAlergia) {
		CriarAlergia alergia = new CriarAlergia();
		alergia.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergia.setIdMedicamentos(idsMedicamentos);
		alergia.setLocalAfetado("Braços");
		alergia.setTipoAlergia(tipoAlergia);
		return alergia;
	}

	private CriarAlergia criarAlergiaErro3(Set<MedicamentoId> idsMedicamentos, String tipoAlergia) {
		CriarAlergia alergia = new CriarAlergia();
		alergia.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergia.setEfeitos("Coceira");
		alergia.setLocalAfetado("Braços");
		alergia.setTipoAlergia(tipoAlergia);
		return alergia;
	}

	private CriarAlergia criarAlergiaErro4(Set<MedicamentoId> idsMedicamentos, String tipoAlergia) {
		CriarAlergia alergia = new CriarAlergia();
		alergia.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergia.setEfeitos("Coceira");
		alergia.setIdMedicamentos(idsMedicamentos);
		alergia.setTipoAlergia(tipoAlergia);
		return alergia;
	}

	private CriarAlergia criarAlergiaErro5(Set<MedicamentoId> idsMedicamentos, String tipoAlergia) {
		CriarAlergia alergia = new CriarAlergia();
		alergia.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergia.setEfeitos("Coceira");
		alergia.setIdMedicamentos(idsMedicamentos);
		alergia.setLocalAfetado("Braços");
		return alergia;
	}

	private CriarAlergia criarAlergiaErro6(Set<MedicamentoId> idsMedicamentos, String tipoAlergia) {
		CriarAlergia alergia = new CriarAlergia();
		alergia.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergia.setEfeitos("Coceira");
		alergia.setIdMedicamentos(idsMedicamentos);
		alergia.setLocalAfetado("Braços");
		alergia.setTipoAlergia(tipoAlergia);
		return alergia;
	}

	private EditarAlergia editarAlergia(Alergia alergia, Set<MedicamentoId> idsMedicamentos) {
		EditarAlergia alergiaEditada = new EditarAlergia();
		alergiaEditada.setIdAlergia(alergia.getIdAlergia());
		alergiaEditada.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergiaEditada.setEfeitos("Manchas");
		alergiaEditada.setIdMedicamentos(idsMedicamentos);
		alergiaEditada.setLocalAfetado("Pernas");
		alergiaEditada.setTipoAlergia("Alergia nas pernas");
		return alergiaEditada;
	}

	private CriarMedicamento criarMedicamento(String nome, String composicao) {
		CriarMedicamento medicamento = new CriarMedicamento();
		medicamento.setComposicao(composicao);
		medicamento.setNomeMedicamento(nome);
		return medicamento;
	}

	private EditarAlergia editarAlergiaError(AlergiaId id) {
		EditarAlergia erro = new EditarAlergia();
		erro.setIdAlergia(id);
		return erro;
	}

	private EditarAlergia editarAlergiaError1(Alergia alergia, Set<MedicamentoId> idsMedicamentos) {
		EditarAlergia alergiaEditada = new EditarAlergia();
		alergiaEditada.setIdAlergia(alergia.getIdAlergia());
		alergiaEditada.setEfeitos("Manchas");
		alergiaEditada.setIdMedicamentos(idsMedicamentos);
		alergiaEditada.setLocalAfetado("Pernas");
		alergiaEditada.setTipoAlergia("Alergia nas pernas");
		return alergiaEditada;
	}

	private EditarAlergia editarAlergiaError2(Alergia alergia, Set<MedicamentoId> idsMedicamentos) {
		EditarAlergia alergiaEditada = new EditarAlergia();
		alergiaEditada.setIdAlergia(alergia.getIdAlergia());
		alergiaEditada.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergiaEditada.setIdMedicamentos(idsMedicamentos);
		alergiaEditada.setLocalAfetado("Pernas");
		alergiaEditada.setTipoAlergia("Alergia nas pernas");
		return alergiaEditada;
	}

	private EditarAlergia editarAlergiaError3(Alergia alergia, Set<MedicamentoId> idsMedicamentos) {
		EditarAlergia alergiaEditada = new EditarAlergia();
		alergiaEditada.setIdAlergia(alergia.getIdAlergia());
		alergiaEditada.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergiaEditada.setEfeitos("Manchas");
		alergiaEditada.setLocalAfetado("Pernas");
		alergiaEditada.setTipoAlergia("Alergia nas pernas");
		return alergiaEditada;
	}

	private EditarAlergia editarAlergiaError4(Alergia alergia, Set<MedicamentoId> idsMedicamentos) {
		EditarAlergia alergiaEditada = new EditarAlergia();
		alergiaEditada.setIdAlergia(alergia.getIdAlergia());
		alergiaEditada.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergiaEditada.setEfeitos("Manchas");
		alergiaEditada.setIdMedicamentos(idsMedicamentos);
		alergiaEditada.setTipoAlergia("Alergia nas pernas");
		return alergiaEditada;
	}

	private EditarAlergia editarAlergiaError5(Alergia alergia, Set<MedicamentoId> idsMedicamentos) {
		EditarAlergia alergiaEditada = new EditarAlergia();
		alergiaEditada.setIdAlergia(alergia.getIdAlergia());
		alergiaEditada.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergiaEditada.setEfeitos("Manchas");
		alergiaEditada.setIdMedicamentos(idsMedicamentos);
		alergiaEditada.setLocalAfetado("Pernas");
		return alergiaEditada;
	}

	private EditarAlergia editarAlergiaError6() {
		EditarAlergia erro = new EditarAlergia();
		erro.setIdAlergia(new AlergiaId());
		return erro;
	}

	private EditarAlergia editarAlergiaError7(Alergia alergia, Set<MedicamentoId> idsMedicamentos) {
		EditarAlergia alergiaEditada = new EditarAlergia();
		alergiaEditada.setIdAlergia(new AlergiaId());
		alergiaEditada.setTipoAlergia("teste");
		alergiaEditada.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergiaEditada.setEfeitos("Manchas");
		alergiaEditada.setIdMedicamentos(idsMedicamentos);
		alergiaEditada.setLocalAfetado("Pernas");
		return alergiaEditada;
	}

	private EditarAlergia editarAlergiaError8(Alergia alergia, Set<MedicamentoId> idsMedicamentos) {
		EditarAlergia alergiaEditada = new EditarAlergia();
		alergiaEditada.setIdAlergia(new AlergiaId());
		alergiaEditada.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergiaEditada.setEfeitos("Manchas");
		alergiaEditada.setIdMedicamentos(idsMedicamentos);
		alergiaEditada.setLocalAfetado("Pernas");
		return alergiaEditada;
	}

	private EditarAlergia editarAlergiaError9(Alergia alergia, Set<MedicamentoId> idsMedicamentos) {
		EditarAlergia alergiaEditada = new EditarAlergia();
		alergiaEditada.setIdAlergia(new AlergiaId());
		alergiaEditada.setTipoAlergia("teste");
		alergiaEditada.setEfeitos("Manchas");
		alergiaEditada.setIdMedicamentos(idsMedicamentos);
		alergiaEditada.setLocalAfetado("Pernas");
		return alergiaEditada;
	}

	private EditarAlergia editarAlergiaError10(Alergia alergia, Set<MedicamentoId> idsMedicamentos) {
		EditarAlergia alergiaEditada = new EditarAlergia();
		alergiaEditada.setIdAlergia(new AlergiaId());
		alergiaEditada.setTipoAlergia("teste");
		alergiaEditada.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergiaEditada.setIdMedicamentos(idsMedicamentos);
		alergiaEditada.setLocalAfetado("Pernas");
		return alergiaEditada;
	}

	private EditarAlergia editarAlergiaError11(Alergia alergia, Set<MedicamentoId> idsMedicamentos) {
		EditarAlergia alergiaEditada = new EditarAlergia();
		alergiaEditada.setIdAlergia(new AlergiaId());
		alergiaEditada.setTipoAlergia("teste");
		alergiaEditada.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergiaEditada.setEfeitos("Manchas");
		alergiaEditada.setLocalAfetado("Pernas");
		return alergiaEditada;
	}

	private EditarAlergia editarAlergiaError12(Alergia alergia, Set<MedicamentoId> idsMedicamentos) {
		EditarAlergia alergiaEditada = new EditarAlergia();
		alergiaEditada.setIdAlergia(alergia.getIdAlergia());
		alergiaEditada.setTipoAlergia("teste");
		alergiaEditada.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergiaEditada.setEfeitos("Manchas");
		alergiaEditada.setIdMedicamentos(idsMedicamentos);
		alergiaEditada.setLocalAfetado("Pernas");
		return alergiaEditada;
	}

	private EditarAlergia editarAlergiaError13(Alergia alergia, Set<MedicamentoId> idsMedicamentos) {
		EditarAlergia alergiaEditada = new EditarAlergia();
		alergiaEditada.setIdAlergia(new AlergiaId());
		alergiaEditada.setTipoAlergia("teste");
		alergiaEditada.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergiaEditada.setEfeitos("Manchas");
		alergiaEditada.setIdMedicamentos(idsMedicamentos);
		return alergiaEditada;
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
