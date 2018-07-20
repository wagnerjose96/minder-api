package br.minder.test.cirurgia;

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
import br.minder.cirurgia.Cirurgia;
import br.minder.cirurgia.CirurgiaId;
import br.minder.cirurgia.CirurgiaRepository;
import br.minder.cirurgia.comandos.CriarCirurgia;
import br.minder.cirurgia.comandos.EditarCirurgia;
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

public class TestCirurgiaController {

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
	private CirurgiaRepository repoCirurgia;

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
		Set<MedicamentoId> idsMedicamentos = new HashSet<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarCirurgia(idsMedicamentos, "Pedra no rim"));

		this.mockMvc
				.perform(post("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A cirurgia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		this.mockMvc
				.perform(post("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "TokenError").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		jsonString = objectMapper.writeValueAsString(new CriarCirurgia());

		this.mockMvc
				.perform(post("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A cirurgia não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());
		
		jsonString = objectMapper.writeValueAsString(criarCirurgiaError1(idsMedicamentos, "Pedra no rim"));

		this.mockMvc
				.perform(post("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A cirurgia não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());
		
		jsonString = objectMapper.writeValueAsString(criarCirurgiaError2(idsMedicamentos, "Pedra no rim"));

		this.mockMvc
				.perform(post("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A cirurgia não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());
		
		jsonString = objectMapper.writeValueAsString(criarCirurgiaError3(idsMedicamentos, "Pedra no rim"));

		this.mockMvc
				.perform(post("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A cirurgia não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());
		
		jsonString = objectMapper.writeValueAsString(criarCirurgiaError4(idsMedicamentos, "Pedra no rim"));

		this.mockMvc
				.perform(post("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A cirurgia não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());
		
		jsonString = objectMapper.writeValueAsString(criarCirurgiaError5(idsMedicamentos, "Pedra no rim"));

		this.mockMvc
				.perform(post("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A cirurgia não foi salva devido a um erro interno")))
				.andExpect(status().isInternalServerError());
		
		jsonString = objectMapper.writeValueAsString(criarCirurgiaError6(idsMedicamentos, "Pedra no rim"));

		this.mockMvc
				.perform(post("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A cirurgia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());
	}

	@Test
	public void testEditar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		Set<MedicamentoId> idsMedicamentos = new HashSet<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarCirurgia(idsMedicamentos, "Pedra no rim"));

		this.mockMvc
				.perform(post("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A cirurgia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Cirurgia> cirurgias = repoCirurgia.findAll();
		assertThat(cirurgias.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarCirurgia(cirurgias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A cirurgia foi alterada com sucesso"))).andExpect(status().isOk());

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "TokenError").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		String error = objectMapper.writeValueAsString(editarCirurgiaError1(cirurgias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do cirurgia")))
				.andExpect(status().isInternalServerError());

		error = objectMapper.writeValueAsString(editarCirurgiaError2(cirurgias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do cirurgia")))
				.andExpect(status().isInternalServerError());
		
		error = objectMapper.writeValueAsString(editarCirurgiaError3(cirurgias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do cirurgia")))
				.andExpect(status().isInternalServerError());
		
		error = objectMapper.writeValueAsString(editarCirurgiaError4(cirurgias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do cirurgia")))
				.andExpect(status().isInternalServerError());
		
		error = objectMapper.writeValueAsString(editarCirurgiaError5(cirurgias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do cirurgia")))
				.andExpect(status().isInternalServerError());
		
		error = objectMapper.writeValueAsString(editarCirurgiaError6(cirurgias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A cirurgia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());
		
		error = objectMapper.writeValueAsString(editarCirurgiaError7(cirurgias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A cirurgia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());
		
		error = objectMapper.writeValueAsString(editarCirurgiaError8(cirurgias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A cirurgia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());
		
		error = objectMapper.writeValueAsString(editarCirurgiaError9(cirurgias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A cirurgia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());
		
		error = objectMapper.writeValueAsString(editarCirurgiaError10(cirurgias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A cirurgia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());
		
		error = objectMapper.writeValueAsString(editarCirurgiaError11(cirurgias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A cirurgia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());
		
		error = objectMapper.writeValueAsString(editarCirurgiaError12(cirurgias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(error))
				.andExpect(jsonPath("$.error", equalTo("A cirurgia a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		serviceUsuario.salvar(criarUsuario("lathuanny@hotmail.com", "lathuanny", idSexo, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		Set<MedicamentoId> idsMedicamentos = new HashSet<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarCirurgia(idsMedicamentos, "Pedra no rim"));

		this.mockMvc
				.perform(post("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A cirurgia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Cirurgia> cirurgias = repoCirurgia.findAll();
		assertThat(cirurgias.get(0), notNullValue());

		this.mockMvc
				.perform(get("/cirurgias/" + cirurgias.get(0).getIdCirurgia().toString()).header("token",
						logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.tipoCirurgia", equalTo("Pedra no rim"))).andExpect(status().isOk());
		
		this.mockMvc
		.perform(get("/cirurgias/" + cirurgias.get(0).getIdCirurgia().toString()).header("token",
				logar("wagnerju", "1234") + "TokenError"))
		.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());
		
		this.mockMvc
		.perform(get("/cirurgias/" + new CirurgiaId().toString()).header("token",
				logar("wagnerju", "1234")))
		.andExpect(jsonPath("$.error", equalTo("A cirurgia procurada não existe no banco de dados"))).andExpect(status().isNotFound());
	
		this.mockMvc
		.perform(get("/cirurgias/" + new CirurgiaId().toString()).header("token",
				logar("lathuanny", "1234")))
		.andExpect(jsonPath("$.error", equalTo("A cirurgia procurada não existe no banco de dados"))).andExpect(status().isNotFound());
	
	}

	@Test
	public void testBurcarTodos() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		serviceUsuario.salvar(criarUsuario("lathuanny@hotmail.com", "lathuanny", idSexo, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		Set<MedicamentoId> idsMedicamentos = new HashSet<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());
		
		this.mockMvc.perform(get("/cirurgias").header("token", logar("wagnerju", "1234")))
		.andExpect(jsonPath("$.error", equalTo("Não existe nenhuma cirurgia cadastrada no banco de dados")))
		.andExpect(status().isNotFound());

		String jsonString = objectMapper.writeValueAsString(criarCirurgia(idsMedicamentos, "Pedra no rim"));

		this.mockMvc
				.perform(post("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A cirurgia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarCirurgia(idsMedicamentos, "Báriatrica"));

		this.mockMvc
				.perform(post("/cirurgias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A cirurgia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Cirurgia> cirurgias = repoCirurgia.findAll();
		assertThat(cirurgias.get(0), notNullValue());

		this.mockMvc.perform(get("/cirurgias").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$[0].tipoCirurgia", equalTo("Pedra no rim")))
				.andExpect(jsonPath("$[1].tipoCirurgia", equalTo("Báriatrica"))).andExpect(status().isOk());
		
		this.mockMvc.perform(get("/cirurgias").header("token", logar("wagnerju", "1234") + "TokenError"))
		.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());
		
		this.mockMvc.perform(get("/cirurgias").header("token", logar("lathuanny", "1234"))).andExpect(status().isOk());

	}

	private CriarCirurgia criarCirurgia(Set<MedicamentoId> idsMedicamentos, String tipoCirurgia) {
		CriarCirurgia cirurgia = new CriarCirurgia();
		cirurgia.setDataCirurgia(Date.valueOf(LocalDate.of(2018, 07, 10)));
		cirurgia.setClinicaResponsavel("Unimed");
		cirurgia.setIdMedicamentos(idsMedicamentos);
		cirurgia.setMedicoResponsavel("Dr Maikon");
		cirurgia.setTipoCirurgia(tipoCirurgia);
		return cirurgia;
	}
	
	private CriarCirurgia criarCirurgiaError1(Set<MedicamentoId> idsMedicamentos, String tipoCirurgia) {
		CriarCirurgia cirurgia = new CriarCirurgia();
		cirurgia.setClinicaResponsavel("Unimed");
		cirurgia.setIdMedicamentos(idsMedicamentos);
		cirurgia.setMedicoResponsavel("Dr Maikon");
		cirurgia.setTipoCirurgia(tipoCirurgia);
		return cirurgia;
	}
	
	private CriarCirurgia criarCirurgiaError2(Set<MedicamentoId> idsMedicamentos, String tipoCirurgia) {
		CriarCirurgia cirurgia = new CriarCirurgia();
		cirurgia.setDataCirurgia(Date.valueOf(LocalDate.of(2018, 07, 10)));
		cirurgia.setIdMedicamentos(idsMedicamentos);
		cirurgia.setMedicoResponsavel("Dr Maikon");
		cirurgia.setTipoCirurgia(tipoCirurgia);
		return cirurgia;
	}
	
	private CriarCirurgia criarCirurgiaError3(Set<MedicamentoId> idsMedicamentos, String tipoCirurgia) {
		CriarCirurgia cirurgia = new CriarCirurgia();
		cirurgia.setDataCirurgia(Date.valueOf(LocalDate.of(2018, 07, 10)));
		cirurgia.setClinicaResponsavel("Unimed");
		cirurgia.setMedicoResponsavel("Dr Maikon");
		cirurgia.setTipoCirurgia(tipoCirurgia);
		return cirurgia;
	}
	
	private CriarCirurgia criarCirurgiaError4(Set<MedicamentoId> idsMedicamentos, String tipoCirurgia) {
		CriarCirurgia cirurgia = new CriarCirurgia();
		cirurgia.setDataCirurgia(Date.valueOf(LocalDate.of(2018, 07, 10)));
		cirurgia.setClinicaResponsavel("Unimed");
		cirurgia.setIdMedicamentos(idsMedicamentos);
		cirurgia.setTipoCirurgia(tipoCirurgia);
		return cirurgia;
	}
	
	private CriarCirurgia criarCirurgiaError5(Set<MedicamentoId> idsMedicamentos, String tipoCirurgia) {
		CriarCirurgia cirurgia = new CriarCirurgia();
		cirurgia.setDataCirurgia(Date.valueOf(LocalDate.of(2018, 07, 10)));
		cirurgia.setClinicaResponsavel("Unimed");
		cirurgia.setIdMedicamentos(idsMedicamentos);
		cirurgia.setMedicoResponsavel("Dr Maikon");
		return cirurgia;
	}
	
	private CriarCirurgia criarCirurgiaError6(Set<MedicamentoId> idsMedicamentos, String tipoCirurgia) {
		CriarCirurgia cirurgia = new CriarCirurgia();
		cirurgia.setDataCirurgia(Date.valueOf(LocalDate.of(2018, 07, 10)));
		cirurgia.setClinicaResponsavel("Unimed");
		cirurgia.setIdMedicamentos(idsMedicamentos);
		cirurgia.setMedicoResponsavel("Dr Maikon");
		cirurgia.setTipoCirurgia(tipoCirurgia);
		return cirurgia;
	}

	private EditarCirurgia editarCirurgia(Cirurgia cirurgia, Set<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(cirurgia.getIdCirurgia());
		cirurgiaEditada.setDataCirurgia(cirurgia.getDataCirurgia());
		cirurgiaEditada.setClinicaResponsavel(cirurgia.getClinicaResponsavel());
		cirurgiaEditada.setIdMedicamentos(idsMedicamentos);
		cirurgiaEditada.setMedicoResponsavel(cirurgia.getMedicoResponsavel());
		cirurgiaEditada.setTipoCirurgia("Báriatrica");
		return cirurgiaEditada;
	}
	
	private EditarCirurgia editarCirurgiaError1(Cirurgia cirurgia, Set<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(cirurgia.getIdCirurgia());
		cirurgiaEditada.setClinicaResponsavel(cirurgia.getClinicaResponsavel());
		cirurgiaEditada.setIdMedicamentos(idsMedicamentos);
		cirurgiaEditada.setMedicoResponsavel(cirurgia.getMedicoResponsavel());
		cirurgiaEditada.setTipoCirurgia("Báriatrica");
		return cirurgiaEditada;
	}
	
	private EditarCirurgia editarCirurgiaError2(Cirurgia cirurgia, Set<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(cirurgia.getIdCirurgia());
		cirurgiaEditada.setDataCirurgia(cirurgia.getDataCirurgia());
		cirurgiaEditada.setIdMedicamentos(idsMedicamentos);
		cirurgiaEditada.setMedicoResponsavel(cirurgia.getMedicoResponsavel());
		cirurgiaEditada.setTipoCirurgia("Báriatrica");
		return cirurgiaEditada;
	}
	
	private EditarCirurgia editarCirurgiaError3(Cirurgia cirurgia, Set<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(cirurgia.getIdCirurgia());
		cirurgiaEditada.setDataCirurgia(cirurgia.getDataCirurgia());
		cirurgiaEditada.setClinicaResponsavel(cirurgia.getClinicaResponsavel());
		cirurgiaEditada.setMedicoResponsavel(cirurgia.getMedicoResponsavel());
		cirurgiaEditada.setTipoCirurgia("Báriatrica");
		return cirurgiaEditada;
	}
	
	private EditarCirurgia editarCirurgiaError4(Cirurgia cirurgia, Set<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(cirurgia.getIdCirurgia());
		cirurgiaEditada.setDataCirurgia(cirurgia.getDataCirurgia());
		cirurgiaEditada.setClinicaResponsavel(cirurgia.getClinicaResponsavel());
		cirurgiaEditada.setIdMedicamentos(idsMedicamentos);
		cirurgiaEditada.setTipoCirurgia("Báriatrica");
		return cirurgiaEditada;
	}
	
	private EditarCirurgia editarCirurgiaError5(Cirurgia cirurgia, Set<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(cirurgia.getIdCirurgia());
		cirurgiaEditada.setDataCirurgia(cirurgia.getDataCirurgia());
		cirurgiaEditada.setClinicaResponsavel(cirurgia.getClinicaResponsavel());
		cirurgiaEditada.setIdMedicamentos(idsMedicamentos);
		cirurgiaEditada.setMedicoResponsavel(cirurgia.getMedicoResponsavel());
		return cirurgiaEditada;
	}
	
	private EditarCirurgia editarCirurgiaError6(Cirurgia cirurgia, Set<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(new CirurgiaId());
		cirurgiaEditada.setDataCirurgia(cirurgia.getDataCirurgia());
		cirurgiaEditada.setClinicaResponsavel(cirurgia.getClinicaResponsavel());
		cirurgiaEditada.setIdMedicamentos(idsMedicamentos);
		cirurgiaEditada.setMedicoResponsavel(cirurgia.getMedicoResponsavel());
		cirurgiaEditada.setTipoCirurgia("Báriatrica");
		return cirurgiaEditada;
	}
	
	private EditarCirurgia editarCirurgiaError7(Cirurgia cirurgia, Set<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(new CirurgiaId());
		cirurgiaEditada.setClinicaResponsavel(cirurgia.getClinicaResponsavel());
		cirurgiaEditada.setIdMedicamentos(idsMedicamentos);
		cirurgiaEditada.setMedicoResponsavel(cirurgia.getMedicoResponsavel());
		cirurgiaEditada.setTipoCirurgia("Báriatrica");
		return cirurgiaEditada;
	}
	
	private EditarCirurgia editarCirurgiaError8(Cirurgia cirurgia, Set<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(new CirurgiaId());
		cirurgiaEditada.setDataCirurgia(cirurgia.getDataCirurgia());
		cirurgiaEditada.setIdMedicamentos(idsMedicamentos);
		cirurgiaEditada.setMedicoResponsavel(cirurgia.getMedicoResponsavel());
		cirurgiaEditada.setTipoCirurgia("Báriatrica");
		return cirurgiaEditada;
	}
	
	private EditarCirurgia editarCirurgiaError9(Cirurgia cirurgia, Set<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(new CirurgiaId());
		cirurgiaEditada.setDataCirurgia(cirurgia.getDataCirurgia());
		cirurgiaEditada.setClinicaResponsavel(cirurgia.getClinicaResponsavel());
		cirurgiaEditada.setMedicoResponsavel(cirurgia.getMedicoResponsavel());
		cirurgiaEditada.setTipoCirurgia("Báriatrica");
		return cirurgiaEditada;
	}
	
	private EditarCirurgia editarCirurgiaError10(Cirurgia cirurgia, Set<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(new CirurgiaId());
		cirurgiaEditada.setDataCirurgia(cirurgia.getDataCirurgia());
		cirurgiaEditada.setClinicaResponsavel(cirurgia.getClinicaResponsavel());
		cirurgiaEditada.setIdMedicamentos(idsMedicamentos);
		cirurgiaEditada.setTipoCirurgia("Báriatrica");
		return cirurgiaEditada;
	}
	
	private EditarCirurgia editarCirurgiaError11(Cirurgia cirurgia, Set<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(new CirurgiaId());
		cirurgiaEditada.setDataCirurgia(cirurgia.getDataCirurgia());
		cirurgiaEditada.setClinicaResponsavel(cirurgia.getClinicaResponsavel());
		cirurgiaEditada.setIdMedicamentos(idsMedicamentos);
		cirurgiaEditada.setMedicoResponsavel(cirurgia.getMedicoResponsavel());
		return cirurgiaEditada;
	}
	
	private EditarCirurgia editarCirurgiaError12(Cirurgia cirurgia, Set<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(new CirurgiaId());
		return cirurgiaEditada;
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

}
