package br.minder.test.emergencia;

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
import br.minder.alergia.Alergia;
import br.minder.alergia.AlergiaId;
import br.minder.alergia.AlergiaRepository;
import br.minder.alergia.comandos.CriarAlergia;
import br.minder.cirurgia.Cirurgia;
import br.minder.cirurgia.CirurgiaId;
import br.minder.cirurgia.CirurgiaRepository;
import br.minder.cirurgia.comandos.CriarCirurgia;
import br.minder.doenca.Doenca;
import br.minder.doenca.DoencaId;
import br.minder.doenca.DoencaRepository;
import br.minder.doenca.comandos.CriarDoenca;
import br.minder.emergencia.Emergencia;
import br.minder.emergencia.EmergenciaId;
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

	@Autowired
	private MedicamentoService serviceMedicamento;

	@Autowired
	private AlergiaRepository repoAlergia;

	@Autowired
	private CirurgiaRepository repoCirurgia;

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

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarEmergencia(1));

		this.mockMvc.perform(post("/api/emergencia").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A emergência foi cadastrada com sucesso")))
				.andExpect(status().isCreated()).andReturn();

		this.mockMvc
				.perform(post("/api/emergencia").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
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

		this.mockMvc.perform(post("/api/emergencia").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A emergência foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Emergencia> emergencias = repoEmergencia.findAll();
		assertThat(emergencias.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarEmergencia(emergencias.get(0)));

		this.mockMvc.perform(put("/api/emergencia").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A emergência foi alterada com sucesso"))).andExpect(status().isOk());

		this.mockMvc
				.perform(put("/api/emergencia").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).header("token", logar("lathuanny", "1234"))
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("A emergência a ser alterada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(put("/api/emergencia").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "erroToken").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());
	}

	@Test
	public void testBurcar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		this.mockMvc.perform(get("/api/emergencia").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhuma emergência cadastrada no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc.perform(get("/api/emergencia").header("token", logar("wagnerju", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		String jsonString = objectMapper.writeValueAsString(criarEmergencia(1));

		this.mockMvc.perform(post("/api/emergencia").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A emergência foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Emergencia> emergencias = repoEmergencia.findAll();
		assertThat(emergencias.get(0), notNullValue());

		this.mockMvc.perform(get("/api/emergencia").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.problemasCardiacos", equalTo("Arritmia"))).andExpect(status().isOk());
	}

	@Test
	public void testBurcarPdf() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		Set<MedicamentoId> idsMedicamentos = new HashSet<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);

		Alergia alergia = new Alergia(criarAlergia(idsMedicamentos, "Lactose"), usuarios.get(0).getId());
		AlergiaId idAlergia = repoAlergia.save(alergia).getIdAlergia();

		Alergia alergias = repoAlergia.findAll().get(0);
		assertThat(alergias, notNullValue());

		assertThat(alergias.getIdAlergia().getValue().toString(), equalTo(idAlergia.toString()));

		this.mockMvc.perform(get("/api/emergencia/pdf").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhuma emergência cadastrada no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc.perform(get("/api/emergencia/pdf").header("token", logar("wagnerju", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		String jsonString = objectMapper.writeValueAsString(criarEmergencia(1));

		this.mockMvc.perform(post("/api/emergencia").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("A emergência foi cadastrada com sucesso")))
				.andExpect(status().isCreated());

		List<Emergencia> emergencias = repoEmergencia.findAll();
		assertThat(emergencias.get(0), notNullValue());

		this.mockMvc.perform(get("/api/emergencia/pdf").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.problemasCardiacos", equalTo("Arritmia"))).andExpect(status().isOk());

		Cirurgia cirurgia = new Cirurgia(criarCirurgia(idsMedicamentos, "Coração"), usuarios.get(0).getId());
		CirurgiaId idCirurgia = repoCirurgia.save(cirurgia).getIdCirurgia();

		Cirurgia cirurgias = repoCirurgia.findAll().get(0);
		assertThat(cirurgias, notNullValue());

		assertThat(cirurgias.getIdCirurgia().getValue().toString(), equalTo(idCirurgia.toString()));

		this.mockMvc.perform(get("/api/emergencia/pdf").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.problemasCardiacos", equalTo("Arritmia"))).andExpect(status().isOk());

		Doenca doenca = new Doenca(criarDoenca(idsMedicamentos, "Asma"), usuarios.get(0).getId());
		DoencaId idDoenca = repoDoenca.save(doenca).getIdDoenca();

		Doenca doencas = repoDoenca.findAll().get(0);
		assertThat(doencas, notNullValue());

		assertThat(doencas.getIdDoenca().getValue().toString(), equalTo(idDoenca.toString()));

		this.mockMvc.perform(get("/api/emergencia/pdf").header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.problemasCardiacos", equalTo("Arritmia"))).andExpect(status().isOk());
	}

	@Test
	public void testarId() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		Emergencia emerg = new Emergencia(criarEmergencia(1), usuarios.get(0).getId());
		EmergenciaId idEmergencia = repoEmergencia.save(emerg).getIdEmergencia();

		Emergencia emergencia = repoEmergencia.findAll().get(0);
		assertThat(emergencia, notNullValue());

		assertThat(emergencia.getIdEmergencia().getValue().toString(), equalTo(idEmergencia.toString()));

		assertThat(emergencia.getIdUsuario().getValue().toString(), equalTo(usuarios.get(0).getId().toString()));
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

	private String logar(String nomeUsuario, String senha) throws NoSuchAlgorithmException {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginUsuario(corpoLogin).getBody();
	}

	private CriarAlergia criarAlergia(Set<MedicamentoId> idsMedicamentos, String nomeAlergia) {
		CriarAlergia alergia = new CriarAlergia();
		alergia.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergia.setIdMedicamentos(idsMedicamentos);
		alergia.setTipoAlergia(nomeAlergia);
		alergia.setLocalAfetado("pulmao");
		alergia.setEfeitos("dor");
		return alergia;
	}

	private CriarMedicamento criarMedicamento(String nome, String composicao) {
		CriarMedicamento medicamento = new CriarMedicamento();
		medicamento.setComposicao(composicao);
		medicamento.setNomeMedicamento(nome);
		return medicamento;
	}

	private CriarCirurgia criarCirurgia(Set<MedicamentoId> idsMedicamentos, String nomeCirurgia) {
		CriarCirurgia cirurgia = new CriarCirurgia();
		cirurgia.setDataCirurgia(Date.valueOf(LocalDate.of(2018, 07, 10)));
		cirurgia.setIdMedicamentos(idsMedicamentos);
		cirurgia.setTipoCirurgia(nomeCirurgia);
		cirurgia.setClinicaResponsavel("unimed");
		cirurgia.setMedicoResponsavel("Junior");
		return cirurgia;
	}

	private CriarDoenca criarDoenca(Set<MedicamentoId> idsMedicamentos, String nomeDoenca) {
		CriarDoenca doenca = new CriarDoenca();
		doenca.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		doenca.setIdMedicamentos(idsMedicamentos);
		doenca.setNomeDoenca(nomeDoenca);
		return doenca;
	}
}