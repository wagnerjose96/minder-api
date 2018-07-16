package br.hela.test.endereco;

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
import br.hela.endereco.Endereco;
import br.hela.endereco.EnderecoRepository;
import br.hela.endereco.comandos.CriarEndereco;
import br.hela.endereco.comandos.EditarEndereco;

@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { Escoladeti2018Application.class }, webEnvironment = WebEnvironment.MOCK)
public class TestEnderecoController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private EnderecoRepository repoEndereco;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		final String jsonString = objectMapper.writeValueAsString(criarEndereco("Apartamento"));

		this.mockMvc
				.perform(post("/enderecos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O endereço foi cadastrado com sucesso")))
				.andExpect(status().isCreated());
	}

	@Test
	public void testEditar() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarEndereco("Apartamento"));

		this.mockMvc
				.perform(post("/enderecos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O endereço foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Endereco> enderecos = repoEndereco.findAll();
		assertThat(enderecos.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarEndereco(enderecos.get(0)));

		this.mockMvc
				.perform(put("/enderecos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O endereço foi alterado com sucesso"))).andExpect(status().isOk());

	}

	@Test
	public void testBuscarPorId() throws Exception {
		final String jsonString = objectMapper.writeValueAsString(criarEndereco("Apartamento"));

		this.mockMvc
				.perform(post("/enderecos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O endereço foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Endereco> enderecos = repoEndereco.findAll();
		assertThat(enderecos.get(0), notNullValue());

		this.mockMvc.perform(get("/enderecos/" + enderecos.get(0).getId().toString()))
				.andExpect(jsonPath("$.bairro", equalTo("Zona 6"))).andExpect(jsonPath("$.cidade", equalTo("Maringá")))
				.andExpect(status().isOk());
	}

	@Test
	public void testBurcarTodos() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarEndereco("Apartamento"));

		this.mockMvc
				.perform(post("/enderecos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O endereço foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarEndereco("Casa"));

		this.mockMvc
				.perform(post("/enderecos").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O endereço foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Endereco> enderecos = repoEndereco.findAll();
		assertThat(enderecos.get(0), notNullValue());

		this.mockMvc.perform(get("/enderecos")).andExpect(jsonPath("$[0].complemento", equalTo("Apartamento")))
				.andExpect(jsonPath("$[1].complemento", equalTo("Casa"))).andExpect(status().isOk());
	}

	private CriarEndereco criarEndereco(String complemento) {
		CriarEndereco endereco = new CriarEndereco();
		endereco.setBairro("Zona 6");
		endereco.setCidade("Maringá");
		endereco.setEstado("Paraná");
		endereco.setComplemento(complemento);
		endereco.setNumero(1390);
		endereco.setRua("Castro Alves");
		return endereco;
	}

	private EditarEndereco editarEndereco(Endereco endereco) {
		EditarEndereco enderecoEditado = new EditarEndereco();
		enderecoEditado.setId(endereco.getId());
		enderecoEditado.setBairro("Jardim Panorama");
		enderecoEditado.setCidade("Sarandi");
		enderecoEditado.setComplemento(endereco.getComplemento());
		enderecoEditado.setEstado(endereco.getEstado());
		enderecoEditado.setNumero(endereco.getNumero());
		enderecoEditado.setRua(endereco.getRua());
		return enderecoEditado;
	}
}