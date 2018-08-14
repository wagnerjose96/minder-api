package br.minder.test.endereco;

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

import br.minder.MinderApplication;
import br.minder.endereco.Endereco;
import br.minder.endereco.EnderecoId;
import br.minder.endereco.EnderecoRepository;
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.endereco.comandos.EditarEndereco;

@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
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
		String jsonString = objectMapper.writeValueAsString(criarEndereco("Apartamento"));

		this.mockMvc
				.perform(post("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O endereço foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarEnderecoErro1());

		this.mockMvc
				.perform(post("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O endereço não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(criarEnderecoErro2());

		this.mockMvc
				.perform(post("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O endereço não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(criarEnderecoErro3());

		this.mockMvc
				.perform(post("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O endereço não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(criarEnderecoErro4());

		this.mockMvc
				.perform(post("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O endereço não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(criarEnderecoErro5());

		this.mockMvc
				.perform(post("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O endereço não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testEditar() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarEndereco("Apartamento"));

		this.mockMvc
				.perform(post("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O endereço foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Endereco> enderecos = repoEndereco.findAll();
		assertThat(enderecos.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarEndereco(enderecos.get(0)));

		this.mockMvc
				.perform(put("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O endereço foi alterado com sucesso"))).andExpect(status().isOk());

		jsonString = objectMapper.writeValueAsString(editarEnderecoErroId());

		this.mockMvc
				.perform(put("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O endereço a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarEnderecoErro1(enderecos.get(0)));

		this.mockMvc
				.perform(put("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do endereço")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarEnderecoErro3(enderecos.get(0)));

		this.mockMvc
				.perform(put("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do endereço")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarEnderecoErro4(enderecos.get(0)));

		this.mockMvc
				.perform(put("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do endereço")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarEnderecoErro5(enderecos.get(0)));

		this.mockMvc
				.perform(put("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do endereço")))
				.andExpect(status().isInternalServerError());

		jsonString = objectMapper.writeValueAsString(editarEnderecoErroId1(enderecos.get(0)));

		this.mockMvc
				.perform(put("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O endereço a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarEnderecoErroId3(enderecos.get(0)));

		this.mockMvc
				.perform(put("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O endereço a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarEnderecoErroId4(enderecos.get(0)));

		this.mockMvc
				.perform(put("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O endereço a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarEnderecoErroId5(enderecos.get(0)));

		this.mockMvc
				.perform(put("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O endereço a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarEnderecoErroId6(enderecos.get(0)));

		this.mockMvc
				.perform(put("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O endereço a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testBuscarPorId() throws Exception {
		final String jsonString = objectMapper.writeValueAsString(criarEndereco("Apartamento"));

		this.mockMvc
				.perform(post("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O endereço foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Endereco> enderecos = repoEndereco.findAll();
		assertThat(enderecos.get(0), notNullValue());

		this.mockMvc.perform(get("/api/endereco/" + enderecos.get(0).getId().toString()))
				.andExpect(jsonPath("$.bairro", equalTo("Zona 6"))).andExpect(jsonPath("$.cidade", equalTo("Maringá")))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/api/endereco/" + new EnderecoId().toString()))
				.andExpect(jsonPath("$.error", equalTo("O endereço procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testBurcarTodos() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarEndereco("Apartamento"));

		this.mockMvc.perform(get("/api/endereco"))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhum endereço cadastrado no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(post("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O endereço foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarEndereco("Casa"));

		this.mockMvc
				.perform(post("/api/endereco").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O endereço foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<Endereco> enderecos = repoEndereco.findAll();
		assertThat(enderecos.get(0), notNullValue());

		this.mockMvc.perform(get("/api/endereco")).andExpect(jsonPath("$[0].complemento", equalTo("Apartamento")))
				.andExpect(jsonPath("$[1].complemento", equalTo("Casa"))).andExpect(status().isOk());
	}

	private CriarEndereco criarEndereco(String complemento) {
		CriarEndereco endereco = new CriarEndereco();
		endereco.setBairro("Zona 6");
		endereco.setCidade("Maringá");
		endereco.setEstado("Paraná");
		endereco.setComplemento(complemento);
		endereco.setNumero("1390");
		endereco.setRua("Castro Alves");
		return endereco;
	}

	private CriarEndereco criarEnderecoErro1() {
		CriarEndereco endereco = new CriarEndereco();
		endereco.setCidade("Maringá");
		endereco.setEstado("Paraná");
		endereco.setNumero("1390");
		endereco.setRua("Castro Alves");
		return endereco;
	}

	private CriarEndereco criarEnderecoErro2() {
		CriarEndereco endereco = new CriarEndereco();
		endereco.setBairro("Zona 6");
		endereco.setEstado("Paraná");
		endereco.setNumero("1390");
		endereco.setRua("Castro Alves");
		return endereco;
	}

	private CriarEndereco criarEnderecoErro3() {
		CriarEndereco endereco = new CriarEndereco();
		endereco.setBairro("Zona 6");
		endereco.setCidade("Maringá");
		endereco.setNumero("1390");
		endereco.setRua("Castro Alves");
		return endereco;
	}

	private CriarEndereco criarEnderecoErro4() {
		CriarEndereco endereco = new CriarEndereco();
		endereco.setBairro("Zona 6");
		endereco.setCidade("Maringá");
		endereco.setEstado("Paraná");
		endereco.setNumero("1390");
		return endereco;
	}

	private CriarEndereco criarEnderecoErro5() {
		CriarEndereco endereco = new CriarEndereco();
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

	private EditarEndereco editarEnderecoErroId6(Endereco endereco) {
		EditarEndereco enderecoEditado = new EditarEndereco();
		enderecoEditado.setId(new EnderecoId());
		return enderecoEditado;
	}

	private EditarEndereco editarEnderecoErroId1(Endereco endereco) {
		EditarEndereco enderecoEditado = new EditarEndereco();
		enderecoEditado.setId(new EnderecoId());
		enderecoEditado.setCidade("Sarandi");
		enderecoEditado.setComplemento(endereco.getComplemento());
		enderecoEditado.setEstado(endereco.getEstado());
		enderecoEditado.setNumero(endereco.getNumero());
		enderecoEditado.setRua(endereco.getRua());
		return enderecoEditado;
	}

	private EditarEndereco editarEnderecoErroId3(Endereco endereco) {
		EditarEndereco enderecoEditado = new EditarEndereco();
		enderecoEditado.setId(new EnderecoId());
		enderecoEditado.setBairro("Jardim Panorama");
		enderecoEditado.setComplemento(endereco.getComplemento());
		enderecoEditado.setEstado(endereco.getEstado());
		enderecoEditado.setNumero(endereco.getNumero());
		enderecoEditado.setRua(endereco.getRua());
		return enderecoEditado;
	}

	private EditarEndereco editarEnderecoErroId4(Endereco endereco) {
		EditarEndereco enderecoEditado = new EditarEndereco();
		enderecoEditado.setId(new EnderecoId());
		enderecoEditado.setBairro("Jardim Panorama");
		enderecoEditado.setCidade("Sarandi");
		enderecoEditado.setComplemento(endereco.getComplemento());
		enderecoEditado.setNumero(endereco.getNumero());
		enderecoEditado.setRua(endereco.getRua());
		return enderecoEditado;
	}

	private EditarEndereco editarEnderecoErroId5(Endereco endereco) {
		EditarEndereco enderecoEditado = new EditarEndereco();
		enderecoEditado.setId(new EnderecoId());
		enderecoEditado.setBairro("Jardim Panorama");
		enderecoEditado.setCidade("Sarandi");
		enderecoEditado.setComplemento(endereco.getComplemento());
		enderecoEditado.setEstado(endereco.getEstado());
		enderecoEditado.setNumero(endereco.getNumero());
		return enderecoEditado;
	}

	private EditarEndereco editarEnderecoErro1(Endereco endereco) {
		EditarEndereco enderecoEditado = new EditarEndereco();
		enderecoEditado.setId(endereco.getId());
		enderecoEditado.setCidade("Sarandi");
		enderecoEditado.setComplemento(endereco.getComplemento());
		enderecoEditado.setEstado(endereco.getEstado());
		enderecoEditado.setNumero(endereco.getNumero());
		enderecoEditado.setRua(endereco.getRua());
		return enderecoEditado;
	}

	private EditarEndereco editarEnderecoErro3(Endereco endereco) {
		EditarEndereco enderecoEditado = new EditarEndereco();
		enderecoEditado.setId(endereco.getId());
		enderecoEditado.setBairro("Jardim Panorama");
		enderecoEditado.setComplemento(endereco.getComplemento());
		enderecoEditado.setEstado(endereco.getEstado());
		enderecoEditado.setNumero(endereco.getNumero());
		enderecoEditado.setRua(endereco.getRua());
		return enderecoEditado;
	}

	private EditarEndereco editarEnderecoErro4(Endereco endereco) {
		EditarEndereco enderecoEditado = new EditarEndereco();
		enderecoEditado.setId(endereco.getId());
		enderecoEditado.setBairro("Jardim Panorama");
		enderecoEditado.setCidade("Sarandi");
		enderecoEditado.setComplemento(endereco.getComplemento());
		enderecoEditado.setNumero(endereco.getNumero());
		enderecoEditado.setRua(endereco.getRua());
		return enderecoEditado;
	}

	private EditarEndereco editarEnderecoErro5(Endereco endereco) {
		EditarEndereco enderecoEditado = new EditarEndereco();
		enderecoEditado.setId(endereco.getId());
		enderecoEditado.setBairro("Jardim Panorama");
		enderecoEditado.setCidade("Sarandi");
		enderecoEditado.setComplemento(endereco.getComplemento());
		enderecoEditado.setEstado(endereco.getEstado());
		enderecoEditado.setNumero(endereco.getNumero());
		return enderecoEditado;
	}

	private EditarEndereco editarEnderecoErroId() {
		EditarEndereco enderecoEditado = new EditarEndereco();
		enderecoEditado.setId(new EnderecoId());
		return enderecoEditado;
	}
}