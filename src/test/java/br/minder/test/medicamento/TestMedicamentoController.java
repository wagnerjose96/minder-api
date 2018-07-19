package br.minder.test.medicamento;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.minder.MinderApplication;
import br.minder.login.LoginController;
import br.minder.login.comandos.LogarAdm;
import br.minder.medicamento.Medicamento;
import br.minder.medicamento.MedicamentoId;
import br.minder.medicamento.MedicamentoRepository;
import br.minder.medicamento.comandos.CriarMedicamento;
import br.minder.medicamento.comandos.EditarMedicamento;
import br.minder.usuario_adm.UsuarioAdm;
import br.minder.usuario_adm.UsuarioAdmRepository;
import br.minder.usuario_adm.UsuarioAdmService;
import br.minder.usuario_adm.comandos.CriarUsuarioAdm;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
public class TestMedicamentoController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private MedicamentoRepository repo;

	@Autowired
	private LoginController login;

	@Autowired
	private UsuarioAdmRepository repoAdm;

	@Autowired
	private UsuarioAdmService admService;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarMedicamento("Medicamento de Teste"));

		this.mockMvc
				.perform(post("/medicamentos").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O medicamento foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(new CriarMedicamento());

		this.mockMvc
				.perform(
						post("/medicamentos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
								.header("token", logarAdm("admin", "1234") + "erroToken").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc
				.perform(
						post("/medicamentos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
								.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O medicamento não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(criarMedicamentoErro1("Medicamento de Teste"));
		this.mockMvc
				.perform(
						post("/medicamentos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
								.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O medicamento não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(criarMedicamentoErro2());
		this.mockMvc
				.perform(
						post("/medicamentos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
								.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O medicamento não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

	}

	@Test
	public void testEditar() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarMedicamento("Medicamento de Teste"));

		this.mockMvc
				.perform(post("/medicamentos").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O medicamento foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Medicamento> medicamentos = repo.findAll();
		assertThat(medicamentos.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarMedicamento(medicamentos.get(0)));

		this.mockMvc
				.perform(put("/medicamentos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O medicamento foi alterado com sucesso"))).andExpect(status().isOk());

		this.mockMvc
				.perform(put("/medicamentos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234") + "erroToken").content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		jsonString = objectMapper.writeValueAsString(editarMedicamentoErro());

		this.mockMvc
				.perform(put("/medicamentos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O medicamento a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarMedicamentoErroId());

		this.mockMvc
				.perform(put("/medicamentos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O medicamento a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarMedicamentoErro1(medicamentos.get(0)));
		this.mockMvc
				.perform(put("/medicamentos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do medicamento")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarMedicamentoErro2(medicamentos.get(0)));
		this.mockMvc
				.perform(put("/medicamentos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do medicamento")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarMedicamentoErro3(medicamentos.get(0)));
		this.mockMvc
				.perform(put("/medicamentos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do medicamento")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarMedicamentoErroId1());
		this.mockMvc
				.perform(put("/medicamentos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O medicamento a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarMedicamentoErroId2());
		this.mockMvc
				.perform(put("/medicamentos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logarAdm("admin", "1234")).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O medicamento a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testBuscarTodos() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		this.mockMvc.perform(get("/medicamentos"))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhum medicamento cadastrado no banco de dados")))
				.andExpect(status().isNotFound());

		String jsonString = objectMapper.writeValueAsString(criarMedicamento("Medicamento de Teste"));

		this.mockMvc
				.perform(post("/medicamentos").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O medicamento foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarMedicamento("Medicamento de Teste 2"));

		this.mockMvc
				.perform(post("/medicamentos").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O medicamento foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Medicamento> medicamentos = repo.findAll();
		assertThat(medicamentos.get(0), notNullValue());
		assertThat(medicamentos.get(1), notNullValue());

		this.mockMvc.perform(get("/medicamentos"))
				.andExpect(jsonPath("$[0].nomeMedicamento", equalTo("Medicamento de Teste")))
				.andExpect(jsonPath("$[1].nomeMedicamento", equalTo("Medicamento de Teste 2")))
				.andExpect(status().isOk());

		this.mockMvc
				.perform(delete("/medicamentos/" + medicamentos.get(0).getIdMedicamento().toString()).header("token",
						logarAdm("admin", "1234")))
				.andExpect(jsonPath("$", equalTo("Medicamento ===> " + medicamentos.get(0).getIdMedicamento().toString()
						+ ": deletado com sucesso")))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/medicamentos"))
				.andExpect(jsonPath("$[0].nomeMedicamento", equalTo("Medicamento de Teste 2")))
				.andExpect(status().isOk());

		this.mockMvc
				.perform(delete("/medicamentos/" + medicamentos.get(1).getIdMedicamento().toString()).header("token",
						logarAdm("admin", "1234")))
				.andExpect(jsonPath("$", equalTo("Medicamento ===> " + medicamentos.get(1).getIdMedicamento().toString()
						+ ": deletado com sucesso")))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/medicamentos")).andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarMedicamento("Medicamento de Teste"));

		this.mockMvc
				.perform(post("/medicamentos").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O medicamento foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Medicamento> medicamentos = repo.findAll();
		assertThat(medicamentos.get(0), notNullValue());

		this.mockMvc.perform(get("/medicamentos/" + medicamentos.get(0).getIdMedicamento().toString()))
				.andExpect(jsonPath("$.nomeMedicamento", equalTo("Medicamento de Teste"))).andExpect(status().isOk());

		this.mockMvc.perform(get("/medicamentos/" + new MedicamentoId().toString()))
				.andExpect(jsonPath("$.error", equalTo("O medicamento procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(delete("/medicamentos/" + medicamentos.get(0).getIdMedicamento().toString()).header("token",
						logarAdm("admin", "1234")))
				.andExpect(jsonPath("$", equalTo("Medicamento ===> " + medicamentos.get(0).getIdMedicamento().toString()
						+ ": deletado com sucesso")))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/medicamentos/" + medicamentos.get(0).getIdMedicamento().toString()))
				.andExpect(jsonPath("$.error", equalTo("O medicamento procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeletar() throws Exception {
		admService.salvar(criarAdm());
		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());

		String jsonString = objectMapper.writeValueAsString(criarMedicamento("Medicamento de Teste"));

		this.mockMvc
				.perform(post("/medicamentos").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O medicamento foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Medicamento> medicamentos = repo.findAll();
		assertThat(medicamentos.get(0), notNullValue());

		this.mockMvc
				.perform(delete("/medicamentos/" + medicamentos.get(0).getIdMedicamento().toString()).header("token",
						logarAdm("admin", "1234")))
				.andExpect(jsonPath("$", equalTo("Medicamento ===> " + medicamentos.get(0).getIdMedicamento().toString()
						+ ": deletado com sucesso")))
				.andExpect(status().isOk());

		this.mockMvc
				.perform(delete("/medicamentos/" + new MedicamentoId().toString()).header("token",
						logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.error", equalTo("O medicamento a deletar não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(delete("/medicamentos/" + medicamentos.get(0).getIdMedicamento().toString()).header("token",
						logarAdm("admin", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

	}

	private CriarMedicamento criarMedicamento(String nome) {
		CriarMedicamento medicamento = new CriarMedicamento();
		medicamento.setNomeMedicamento(nome);
		medicamento.setComposicao("teste");
		return medicamento;
	}

	private CriarMedicamento criarMedicamentoErro1(String nome) {
		CriarMedicamento medicamento = new CriarMedicamento();
		medicamento.setNomeMedicamento(nome);
		return medicamento;
	}

	private CriarMedicamento criarMedicamentoErro2() {
		CriarMedicamento medicamento = new CriarMedicamento();
		medicamento.setComposicao("teste");
		return medicamento;
	}

	private EditarMedicamento editarMedicamento(Medicamento medicamento) {
		EditarMedicamento medicamentoAtualizado = new EditarMedicamento();
		medicamentoAtualizado.setIdMedicamento(medicamento.getIdMedicamento());
		medicamentoAtualizado.setNomeMedicamento("Teste Put");
		medicamentoAtualizado.setComposicao(medicamento.getComposicao());
		medicamentoAtualizado.setAtivo(1);
		return medicamentoAtualizado;
	}

	private EditarMedicamento editarMedicamentoErro1(Medicamento medicamento) {
		EditarMedicamento medicamentoAtualizado = new EditarMedicamento();
		medicamentoAtualizado.setIdMedicamento(medicamento.getIdMedicamento());
		medicamentoAtualizado.setComposicao(medicamento.getComposicao());
		medicamentoAtualizado.setAtivo(1);
		return medicamentoAtualizado;
	}

	private EditarMedicamento editarMedicamentoErro2(Medicamento medicamento) {
		EditarMedicamento medicamentoAtualizado = new EditarMedicamento();
		medicamentoAtualizado.setIdMedicamento(medicamento.getIdMedicamento());
		medicamentoAtualizado.setNomeMedicamento("Teste Put");
		medicamentoAtualizado.setAtivo(1);
		return medicamentoAtualizado;
	}

	private EditarMedicamento editarMedicamentoErro3(Medicamento medicamento) {
		EditarMedicamento medicamentoAtualizado = new EditarMedicamento();
		medicamentoAtualizado.setIdMedicamento(medicamento.getIdMedicamento());
		return medicamentoAtualizado;
	}

	private EditarMedicamento editarMedicamentoErro() {
		EditarMedicamento medicamentoAtualizado = new EditarMedicamento();
		medicamentoAtualizado.setIdMedicamento(new MedicamentoId());
		medicamentoAtualizado.setNomeMedicamento("Teste Erro");
		medicamentoAtualizado.setComposicao("Teste Erro");
		medicamentoAtualizado.setAtivo(1);
		return medicamentoAtualizado;
	}

	private EditarMedicamento editarMedicamentoErroId() {
		EditarMedicamento editar = new EditarMedicamento();
		editar.setIdMedicamento(new MedicamentoId());
		return editar;
	}

	private EditarMedicamento editarMedicamentoErroId1() {
		EditarMedicamento editar = new EditarMedicamento();
		editar.setIdMedicamento(new MedicamentoId());
		editar.setNomeMedicamento("Teste Erro");
		return editar;
	}

	private EditarMedicamento editarMedicamentoErroId2() {
		EditarMedicamento editar = new EditarMedicamento();
		editar.setIdMedicamento(new MedicamentoId());
		editar.setComposicao("Teste Erro");
		return editar;
	}

	private CriarUsuarioAdm criarAdm() {
		CriarUsuarioAdm adm = new CriarUsuarioAdm();
		adm.setNome("admin");
		adm.setSenha("1234");
		return adm;
	}

	private String logarAdm(String nomeUsuario, String senha) {
		LogarAdm corpoLogin = new LogarAdm();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginAdm(corpoLogin).getBody();
	}

}