package br.minder.test.alergia;

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

import br.minder.MinderApplication;
import br.minder.alergia.Alergia;
import br.minder.alergia.AlergiaRepository;
import br.minder.alergia.comandos.CriarAlergia;
import br.minder.alergia.comandos.EditarAlergia;
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

public class TestAlergiaController {

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
	private AlergiaRepository repoAlergia;

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
		MedicamentoId idMedicamento1 = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		List<MedicamentoId> idsMedicamentos = new ArrayList<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);
		idsMedicamentos.add(idMedicamento);
		idsMedicamentos.add(idMedicamento1);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		final String jsonString = objectMapper.writeValueAsString(criarAlergia(idsMedicamentos, "Alergia de pele"));

		this.mockMvc
				.perform(post("/alergias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A alergia foi cadastrada com sucesso")))
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

		String jsonString = objectMapper.writeValueAsString(criarAlergia(idsMedicamentos, "Alergia de pele"));

		this.mockMvc
				.perform(post("/alergias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A alergia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Alergia> alergias = repoAlergia.findAll();
		assertThat(alergias.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarAlergia(alergias.get(0), idsMedicamentos));

		this.mockMvc
				.perform(put("/alergias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A alergia foi alterada com sucesso"))).andExpect(status().isOk());

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

		final String jsonString = objectMapper.writeValueAsString(criarAlergia(idsMedicamentos, "Alergia de pele"));

		this.mockMvc
				.perform(post("/alergias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A alergia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Alergia> alergias = repoAlergia.findAll();
		assertThat(alergias.get(0), notNullValue());

		this.mockMvc
				.perform(get("/alergias/" + alergias.get(0).getIdAlergia().toString()).header("token",
						logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.tipoAlergia", equalTo("Alergia de pele"))).andExpect(status().isOk());
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

		String jsonString = objectMapper.writeValueAsString(criarAlergia(idsMedicamentos, "Alergia de pele"));

		this.mockMvc
				.perform(post("/alergias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A alergia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarAlergia(idsMedicamentos, "Alergia nas pernas"));

		this.mockMvc
				.perform(post("/alergias").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A alergia foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Alergia> alergias = repoAlergia.findAll();
		assertThat(alergias.get(0), notNullValue());

		this.mockMvc.perform(get("/alergias").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$[0].tipoAlergia", equalTo("Alergia de pele")))
				.andExpect(jsonPath("$[1].tipoAlergia", equalTo("Alergia nas pernas"))).andExpect(status().isOk());

	}

	private CriarAlergia criarAlergia(List<MedicamentoId> idsMedicamentos, String tipoAlergia) {
		CriarAlergia alergia = new CriarAlergia();
		alergia.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergia.setEfeitos("Coceira");
		alergia.setIdMedicamentos(idsMedicamentos);
		alergia.setLocalAfetado("Braços");
		alergia.setTipoAlergia(tipoAlergia);
		return alergia;
	}

	private EditarAlergia editarAlergia(Alergia alergia, List<MedicamentoId> idsMedicamentos) {
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
