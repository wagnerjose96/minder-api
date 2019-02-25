package br.minder.test.plano_de_saude;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.minder.MinderApplication;
import br.minder.convenio.Convenio;
import br.minder.convenio.ConvenioId;
import br.minder.convenio.ConvenioRepository;
import br.minder.convenio.ConvenioService;
import br.minder.convenio.comandos.CriarConvenio;
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.genero.Genero;
import br.minder.genero.GeneroId;
import br.minder.genero.GeneroRepository;
import br.minder.genero.comandos.CriarGenero;
import br.minder.login.LoginController;
import br.minder.login.comandos.LogarUsuario;
import br.minder.plano_de_saude.PlanoDeSaude;
import br.minder.plano_de_saude.PlanoDeSaudeId;
import br.minder.plano_de_saude.PlanoDeSaudeRepository;
import br.minder.plano_de_saude.comandos.CriarPlanoDeSaude;
import br.minder.plano_de_saude.comandos.EditarPlanoDeSaude;
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
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
@ActiveProfiles("application-test")
public class TestPlanoDeSaudeController {

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
	private UsuarioRepository repoUsuario;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ConvenioRepository repoConvenio;

	@Autowired
	private ConvenioService convenioService;

	@Autowired
	private PlanoDeSaudeRepository repo;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		GeneroId idGenero = criarGenero("Masculino");
		SangueId idSangue = criarSangue("A+");
		usuarioService.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue));
		List<Usuario> usuario = repoUsuario.findAll();
		assertThat(usuario.get(0), notNullValue());

		convenioService.salvar(criarConvenio("Unimed"));
		List<Convenio> convenio = repoConvenio.findAll();
		assertThat(convenio.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarPlano(convenio.get(0).getId()));

		this.mockMvc
				.perform(post("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "TokenError").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(post("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O plano de saúde foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarPlanoErro1(convenio.get(0).getId()));

		this.mockMvc
				.perform(post("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O plano de saúde não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(criarPlanoErro2(convenio.get(0).getId()));

		this.mockMvc
				.perform(post("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O plano de saúde não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(criarPlanoErro3(convenio.get(0).getId()));

		this.mockMvc
				.perform(post("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O plano de saúde não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(criarPlanoErro4(convenio.get(0).getId()));

		this.mockMvc
				.perform(post("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O plano de saúde não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testEditar() throws Exception {
		GeneroId idGenero = criarGenero("Masculino");
		SangueId idSangue = criarSangue("A+");
		usuarioService.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue));
		usuarioService.salvar(criarUsuario("lathuanny@hotmail.com", "lathuanny", idGenero, idSangue));
		List<Usuario> usuario = repoUsuario.findAll();
		assertThat(usuario.get(0), notNullValue());

		convenioService.salvar(criarConvenio("Unimed"));
		List<Convenio> convenio = repoConvenio.findAll();
		assertThat(convenio.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarPlano(convenio.get(0).getId()));

		this.mockMvc
				.perform(post("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O plano de saúde foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<PlanoDeSaude> planos = repo.findAll();
		assertThat(planos.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarPlano(planos.get(0)));

		this.mockMvc
				.perform(put("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "TokenError").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(put("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O plano de saúde foi alterado com sucesso")))
				.andExpect(status().isOk());

		jsonString = objectMapper.writeValueAsString(editarPlanoErroId(planos.get(0)));

		this.mockMvc
				.perform(put("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O plano de saúde a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarPlanoErroId(planos.get(0)));

		this.mockMvc
				.perform(put("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("lathuanny", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O plano de saúde a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarPlanoErro1(planos.get(0)));

		this.mockMvc
				.perform(put("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(
						jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do plano de saúde")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarPlanoErro2(planos.get(0)));

		this.mockMvc
				.perform(put("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(
						jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do plano de saúde")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarPlanoErro3(planos.get(0)));

		this.mockMvc
				.perform(put("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(
						jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do plano de saúde")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarPlanoErro4(planos.get(0)));

		this.mockMvc
				.perform(put("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(
						jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do plano de saúde")))
				.andExpect(status().isInternalServerError());

	}

	@Test
	public void testBuscarTodos() throws Exception {
		GeneroId idGenero = criarGenero("Masculino");
		SangueId idSangue = criarSangue("A+");
		usuarioService.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue));
		List<Usuario> usuario = repoUsuario.findAll();
		assertThat(usuario.get(0), notNullValue());

		convenioService.salvar(criarConvenio("Unimed"));
		List<Convenio> convenio = repoConvenio.findAll();
		assertThat(convenio.get(0), notNullValue());

		this.mockMvc.perform(get("/api/plano").header("token", logar("wagnerju", "1234")))
				.andExpect(
						jsonPath("$.error", equalTo("Não existe nenhum plano de saúde cadastrado no banco de dados")))
				.andExpect(status().isNotFound());

		String jsonString = objectMapper.writeValueAsString(criarPlano(convenio.get(0).getId()));

		this.mockMvc
				.perform(post("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O plano de saúde foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarPlano(convenio.get(0).getId()));

		this.mockMvc
				.perform(post("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O plano de saúde foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<PlanoDeSaude> planos = repo.findAll();
		assertThat(planos.get(0), notNullValue());
		assertThat(planos.get(1), notNullValue());

		this.mockMvc
				.perform(get("/api/plano").header("token", logar("wagnerju", "1234") + "TokenError").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc.perform(get("/api/plano").header("token", logar("wagnerju", "1234"))).andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		GeneroId idGenero = criarGenero("Masculino");
		SangueId idSangue = criarSangue("A+");
		usuarioService.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue));
		List<Usuario> usuario = repoUsuario.findAll();
		assertThat(usuario.get(0), notNullValue());

		convenioService.salvar(criarConvenio("Unimed"));
		List<Convenio> convenio = repoConvenio.findAll();
		assertThat(convenio.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarPlano(convenio.get(0).getId()));

		this.mockMvc
				.perform(post("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O plano de saúde foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<PlanoDeSaude> planos = repo.findAll();
		assertThat(planos.get(0), notNullValue());

		this.mockMvc
				.perform(get("/api/plano/" + planos.get(0).getId().toString()).header("token", logar("wagnerju", "1234")))
				.andExpect(jsonPath("$.convenio.id.value", equalTo(convenio.get(0).getId().toString())))
				.andExpect(status().isOk());

		this.mockMvc
				.perform(get("/api/plano/" + planos.get(0).getId().toString()).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "TokenError").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc.perform(get("/api/plano/" + new PlanoDeSaudeId().toString()).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O plano de saúde procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testDeletar() throws Exception {
		GeneroId idGenero = criarGenero("Masculino");
		SangueId idSangue = criarSangue("A+");
		usuarioService.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue));
		List<Usuario> usuario = repoUsuario.findAll();
		assertThat(usuario.get(0), notNullValue());

		convenioService.salvar(criarConvenio("Unimed"));
		List<Convenio> convenio = repoConvenio.findAll();
		assertThat(convenio.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarPlano(convenio.get(0).getId()));

		this.mockMvc
				.perform(post("/api/plano").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O plano de saúde foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<PlanoDeSaude> planos = repo.findAll();
		assertThat(planos.get(0), notNullValue());

		this.mockMvc
				.perform(delete("/api/plano/" + planos.get(0).getId().toString()).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234") + "TokenError").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc.perform(delete("/api/plano/" + new PlanoDeSaudeId().toString()).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O plano de saúde a ser deletado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(delete("/api/plano/" + planos.get(0).getId().toString()).header("token",
						logar("wagnerju", "1234")))
				.andExpect(jsonPath("$",
						equalTo("Plano de saúde ===> " + planos.get(0).getId().toString() + ": deletado com sucesso")))
				.andExpect(status().isOk());
	}

	private CriarConvenio criarConvenio(String nome) {
		CriarConvenio convenio = new CriarConvenio();
		convenio.setNome(nome);
		return convenio;
	}

	private CriarPlanoDeSaude criarPlano(ConvenioId idConvenio) {
		CriarPlanoDeSaude plano = new CriarPlanoDeSaude();
		plano.setIdConvenio(idConvenio);
		plano.setNumeroCartao(new BigInteger("1122334455667788990"));
		plano.setHabitacao("quarto");
		plano.setTerritorio("nacional");
		plano.setDataVencimento("1997-03-17");
		return plano;
	}

	private CriarPlanoDeSaude criarPlanoErro1(ConvenioId idConvenio) {
		CriarPlanoDeSaude plano = new CriarPlanoDeSaude();
		plano.setNumeroCartao(new BigInteger("1122334455667788990"));
		plano.setHabitacao("quarto");
		plano.setTerritorio("nacional");
		plano.setDataVencimento("1997-03-17");
		return plano;
	}

	private CriarPlanoDeSaude criarPlanoErro2(ConvenioId idConvenio) {
		CriarPlanoDeSaude plano = new CriarPlanoDeSaude();
		plano.setIdConvenio(idConvenio);
		plano.setHabitacao("quarto");
		plano.setTerritorio("nacional");
		plano.setDataVencimento("1997-03-17");
		return plano;
	}

	private CriarPlanoDeSaude criarPlanoErro3(ConvenioId idConvenio) {
		CriarPlanoDeSaude plano = new CriarPlanoDeSaude();
		plano.setIdConvenio(idConvenio);
		plano.setNumeroCartao(new BigInteger("1122334455667788990"));
		plano.setTerritorio("nacional");
		plano.setDataVencimento("1997-03-17");
		return plano;
	}

	private CriarPlanoDeSaude criarPlanoErro4(ConvenioId idConvenio) {
		CriarPlanoDeSaude plano = new CriarPlanoDeSaude();
		plano.setIdConvenio(idConvenio);
		plano.setNumeroCartao(new BigInteger("1122334455667788990"));
		plano.setHabitacao("quarto");
		plano.setDataVencimento("1997-03-17");
		return plano;
	}

	private EditarPlanoDeSaude editarPlano(PlanoDeSaude plano) {
		EditarPlanoDeSaude planoAtualizado = new EditarPlanoDeSaude();
		planoAtualizado.setId(plano.getId());
		planoAtualizado.setIdConvenio(plano.getIdConvenio());
		planoAtualizado.setNumeroCartao(plano.getNumeroCartao());
		planoAtualizado.setHabitacao("apartamento");
		planoAtualizado.setTerritorio(plano.getTerritorio());
		planoAtualizado.setDataVencimento("1997-03-17");
		return planoAtualizado;
	}

	private EditarPlanoDeSaude editarPlanoErroId(PlanoDeSaude plano) {
		EditarPlanoDeSaude planoAtualizado = new EditarPlanoDeSaude();
		planoAtualizado.setId(new PlanoDeSaudeId());
		planoAtualizado.setIdConvenio(plano.getIdConvenio());
		planoAtualizado.setNumeroCartao(plano.getNumeroCartao());
		planoAtualizado.setHabitacao("apartamento");
		planoAtualizado.setTerritorio(plano.getTerritorio());
		planoAtualizado.setDataVencimento("1997-03-17");
		return planoAtualizado;
	}

	private EditarPlanoDeSaude editarPlanoErro1(PlanoDeSaude plano) {
		EditarPlanoDeSaude planoAtualizado = new EditarPlanoDeSaude();
		planoAtualizado.setId(plano.getId());
		planoAtualizado.setNumeroCartao(plano.getNumeroCartao());
		planoAtualizado.setHabitacao("apartamento");
		planoAtualizado.setTerritorio(plano.getTerritorio());
		planoAtualizado.setDataVencimento("1997-03-17");
		return planoAtualizado;
	}

	private EditarPlanoDeSaude editarPlanoErro2(PlanoDeSaude plano) {
		EditarPlanoDeSaude planoAtualizado = new EditarPlanoDeSaude();
		planoAtualizado.setId(plano.getId());
		planoAtualizado.setIdConvenio(plano.getIdConvenio());
		planoAtualizado.setHabitacao("apartamento");
		planoAtualizado.setTerritorio(plano.getTerritorio());
		planoAtualizado.setDataVencimento("1997-03-17");
		return planoAtualizado;
	}

	private EditarPlanoDeSaude editarPlanoErro3(PlanoDeSaude plano) {
		EditarPlanoDeSaude planoAtualizado = new EditarPlanoDeSaude();
		planoAtualizado.setId(plano.getId());
		planoAtualizado.setIdConvenio(plano.getIdConvenio());
		planoAtualizado.setNumeroCartao(plano.getNumeroCartao());
		planoAtualizado.setTerritorio(plano.getTerritorio());
		planoAtualizado.setDataVencimento("1997-03-17");
		return planoAtualizado;
	}

	private EditarPlanoDeSaude editarPlanoErro4(PlanoDeSaude plano) {
		EditarPlanoDeSaude planoAtualizado = new EditarPlanoDeSaude();
		planoAtualizado.setId(plano.getId());
		planoAtualizado.setIdConvenio(plano.getIdConvenio());
		planoAtualizado.setNumeroCartao(plano.getNumeroCartao());
		planoAtualizado.setHabitacao("apartamento");
		planoAtualizado.setDataVencimento("1997-03-17");
		return planoAtualizado;
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

	private String logar(String nomeUsuario, String senha) throws NoSuchAlgorithmException {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginUsuario(corpoLogin).getBody();
	}

	private SangueId criarSangue(String tipo) {
		return repoSangue.save(new Sangue(new CriarSangue(tipo))).getIdSangue();
	}

	private GeneroId criarGenero(String tipo) {
		return repoGenero.save(new Genero(new CriarGenero(tipo))).getIdGenero();
	}

}
