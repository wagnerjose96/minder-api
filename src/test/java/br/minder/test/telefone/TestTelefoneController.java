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
				.perform(post("/telefones").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O telefone foi cadastrado com sucesso")))
				.andExpect(status().isCreated());
	}

	@Test
	public void testEditar() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarTelefone());

		this.mockMvc
				.perform(post("/telefones").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O telefone foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Telefone> telefones = repo.findAll();
		assertThat(telefones.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarTelefone(telefones.get(0)));

		this.mockMvc
				.perform(put("/telefones").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O telefone foi alterado com sucesso"))).andExpect(status().isOk());
	}

	@Test
	public void testBuscarTodos() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarTelefone());

		this.mockMvc
				.perform(post("/telefones").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O telefone foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarTelefone());

		this.mockMvc
				.perform(post("/telefones").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O telefone foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Telefone> telefones = repo.findAll();
		assertThat(telefones.get(0), notNullValue());
		assertThat(telefones.get(1), notNullValue());

		this.mockMvc.perform(get("/telefones"))
				.andExpect(jsonPath("$[0].id.value", equalTo(telefones.get(0).getId().toString())))
				.andExpect(jsonPath("$[1].id.value", equalTo(telefones.get(1).getId().toString())))
				.andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarTelefone());

		this.mockMvc
				.perform(post("/telefones").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O telefone foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Telefone> telefones = repo.findAll();
		assertThat(telefones.get(0), notNullValue());

		this.mockMvc.perform(get("/telefones/" + telefones.get(0).getId().toString()))
				.andExpect(jsonPath("$.id.value", equalTo(telefones.get(0).getId().toString())))
				.andExpect(status().isOk());
	}

	private CriarTelefone criarTelefone() {
		CriarTelefone telefone = new CriarTelefone();
		telefone.setDdd(44);
		telefone.setNumero(999038860);
		return telefone;
	}

	private EditarTelefone editarTelefone(Telefone telefone) {
		EditarTelefone telefoneAtualizado = new EditarTelefone();
		telefoneAtualizado.setId(telefone.getId());
		telefoneAtualizado.setDdd(44);
		telefoneAtualizado.setNumero(999038860);
		return telefoneAtualizado;
	}

}