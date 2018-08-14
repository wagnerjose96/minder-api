package br.minder.test.telefone;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
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
import br.minder.telefone.Telefone;
import br.minder.telefone.TelefoneId;
import br.minder.telefone.TelefoneRepository;
import br.minder.telefone.comandos.CriarTelefone;
import br.minder.telefone.comandos.EditarTelefone;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
public class TestTelefoneController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private TelefoneRepository repo;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarTelefone());

		this.mockMvc
				.perform(post("/api/telefone").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O telefone foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarTelefoneErro1());

		this.mockMvc
				.perform(post("/api/telefone").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O telefone não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(criarTelefoneErro2());

		this.mockMvc
				.perform(post("/api/telefone").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O telefone não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(criarTelefoneErro3());

		this.mockMvc
				.perform(post("/api/telefone").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O telefone não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testEditar() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarTelefone());

		this.mockMvc
				.perform(post("/api/telefone").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O telefone foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Telefone> telefones = repo.findAll();
		assertThat(telefones.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarTelefone(telefones.get(0)));

		this.mockMvc
				.perform(put("/api/telefone").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O telefone foi alterado com sucesso"))).andExpect(status().isOk());

		jsonString = objectMapper.writeValueAsString(editarTelefoneErroId(telefones.get(0)));

		this.mockMvc
				.perform(put("/api/telefone").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O telefone a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarTelefoneErro1(telefones.get(0)));

		this.mockMvc
				.perform(put("/api/telefone").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do telefone")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarTelefoneErro2(telefones.get(0)));

		this.mockMvc
				.perform(put("/api/telefone").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do telefone")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarTelefoneErro3(telefones.get(0)));

		this.mockMvc
				.perform(put("/api/telefone").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do telefone")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testBuscarTodos() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarTelefone());

		this.mockMvc.perform(get("/api/telefone")).andExpect(status().isNotFound());

		this.mockMvc
				.perform(post("/api/telefone").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O telefone foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarTelefone());

		this.mockMvc
				.perform(post("/api/telefone").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O telefone foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Telefone> telefones = repo.findAll();
		assertThat(telefones.get(0), notNullValue());
		assertThat(telefones.get(1), notNullValue());

		this.mockMvc.perform(get("/api/telefone"))
				.andExpect(jsonPath("$[0].id.value", equalTo(telefones.get(0).getId().toString())))
				.andExpect(jsonPath("$[1].id.value", equalTo(telefones.get(1).getId().toString())))
				.andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarTelefone());

		this.mockMvc.perform(get("/api/telefone/" + new TelefoneId().toString()))
				.andExpect(jsonPath("$.error", equalTo("O telefone procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(post("/api/telefone").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O telefone foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Telefone> telefones = repo.findAll();
		assertThat(telefones.get(0), notNullValue());

		this.mockMvc.perform(get("/api/telefone/" + telefones.get(0).getId().toString()))
				.andExpect(jsonPath("$.id.value", equalTo(telefones.get(0).getId().toString())))
				.andExpect(status().isOk());
	}

	private CriarTelefone criarTelefone() {
		CriarTelefone telefone = new CriarTelefone();
		telefone.setDdd(44);
		telefone.setNumero(999038860);
		return telefone;
	}

	private CriarTelefone criarTelefoneErro1() {
		CriarTelefone telefone = new CriarTelefone();
		telefone.setDdd(44);
		return telefone;
	}

	private CriarTelefone criarTelefoneErro2() {
		CriarTelefone telefone = new CriarTelefone();
		telefone.setNumero(999038860);
		return telefone;
	}

	private CriarTelefone criarTelefoneErro3() {
		CriarTelefone telefone = new CriarTelefone();
		return telefone;
	}

	private EditarTelefone editarTelefone(Telefone telefone) {
		EditarTelefone telefoneAtualizado = new EditarTelefone();
		telefoneAtualizado.setId(telefone.getId());
		telefoneAtualizado.setDdd(44);
		telefoneAtualizado.setNumero(999038860);
		return telefoneAtualizado;
	}

	private EditarTelefone editarTelefoneErroId(Telefone telefone) {
		EditarTelefone telefoneAtualizado = new EditarTelefone();
		telefoneAtualizado.setId(new TelefoneId());
		telefoneAtualizado.setDdd(44);
		telefoneAtualizado.setNumero(999038860);
		return telefoneAtualizado;
	}

	private EditarTelefone editarTelefoneErro1(Telefone telefone) {
		EditarTelefone telefoneAtualizado = new EditarTelefone();
		telefoneAtualizado.setId(telefone.getId());
		telefoneAtualizado.setNumero(999038860);
		return telefoneAtualizado;
	}

	private EditarTelefone editarTelefoneErro2(Telefone telefone) {
		EditarTelefone telefoneAtualizado = new EditarTelefone();
		telefoneAtualizado.setId(telefone.getId());
		telefoneAtualizado.setDdd(44);
		return telefoneAtualizado;
	}

	private EditarTelefone editarTelefoneErro3(Telefone telefone) {
		EditarTelefone telefoneAtualizado = new EditarTelefone();
		telefoneAtualizado.setId(telefone.getId());
		return telefoneAtualizado;
	}

}