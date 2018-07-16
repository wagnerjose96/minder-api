package br.hela.test.cirurgia;

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
import java.util.ArrayList;
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

import br.hela.Escoladeti2018Application;
import br.hela.cirurgia.Cirurgia;
import br.hela.cirurgia.CirurgiaRepository;
import br.hela.cirurgia.comandos.CriarCirurgia;
import br.hela.cirurgia.comandos.EditarCirurgia;
import br.hela.endereco.comandos.CriarEndereco;
import br.hela.login.LoginController;
import br.hela.login.comandos.LogarUsuario;
import br.hela.medicamento.MedicamentoId;
import br.hela.medicamento.MedicamentoService;
import br.hela.medicamento.comandos.CriarMedicamento;
import br.hela.sangue.Sangue;
import br.hela.sangue.SangueId;
import br.hela.sangue.SangueRepository;
import br.hela.sangue.comandos.CriarSangue;
import br.hela.sexo.Sexo;
import br.hela.sexo.SexoId;
import br.hela.sexo.SexoRepository;
import br.hela.sexo.comandos.CriarSexo;
import br.hela.telefone.comandos.CriarTelefone;
import br.hela.usuario.Usuario;
import br.hela.usuario.UsuarioRepository;
import br.hela.usuario.UsuarioService;
import br.hela.usuario.comandos.CriarUsuario;

@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { Escoladeti2018Application.class }, webEnvironment = WebEnvironment.MOCK)

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
		List<MedicamentoId> idsMedicamentos = new ArrayList<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarCirurgia(idsMedicamentos, "Pedra no rim"));

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
		List<MedicamentoId> idsMedicamentos = new ArrayList<MedicamentoId>();
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

	}

	@Test
	public void testBuscarPorId() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		List<MedicamentoId> idsMedicamentos = new ArrayList<MedicamentoId>();
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
	}

	@Test
	public void testBurcarTodos() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		List<MedicamentoId> idsMedicamentos = new ArrayList<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

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

	}

	private CriarCirurgia criarCirurgia(List<MedicamentoId> idsMedicamentos, String tipoCirurgia) {
		CriarCirurgia cirurgia = new CriarCirurgia();
		cirurgia.setDataCirurgia(Date.valueOf(LocalDate.of(2018, 07, 10)));
		cirurgia.setClinicaResponsavel("Unimed");
		cirurgia.setIdMedicamentos(idsMedicamentos);
		cirurgia.setMedicoResponsavel("Dr Maikon");
		cirurgia.setTipoCirurgia(tipoCirurgia);
		return cirurgia;
	}

	private EditarCirurgia editarCirurgia(Cirurgia cirurgia, List<MedicamentoId> idsMedicamentos) {
		EditarCirurgia cirurgiaEditada = new EditarCirurgia();
		cirurgiaEditada.setIdCirurgia(cirurgia.getIdCirurgia());
		cirurgiaEditada.setDataCirurgia(cirurgia.getDataCirurgia());
		cirurgiaEditada.setClinicaResponsavel(cirurgia.getClinicaResponsavel());
		cirurgiaEditada.setIdMedicamentos(idsMedicamentos);
		cirurgiaEditada.setMedicoResponsavel(cirurgia.getMedicoResponsavel());
		cirurgiaEditada.setTipoCirurgia("Báriatrica");
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

}
