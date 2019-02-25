package br.minder.test.exceptions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import br.minder.MinderApplication;
import br.minder.exceptions.ErrorDetail;
import br.minder.exceptions.ErrorDetailId;
import br.minder.exceptions.ErrorDetailRepository;
import br.minder.exceptions.ErrorDetailService;
import br.minder.exceptions.comandos.CriarErrorDetail;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
@ActiveProfiles("application-test")
public class TestExceptionController {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Autowired
	private ErrorDetailRepository repo;

	@Autowired
	private ErrorDetailService service;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		repo.deleteAll();
	}

	@Test
	public void testBuscarTodos() throws Exception {

		this.mockMvc.perform(get("/api/exception"))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhuma exceção cadastrada no banco de dados")))
				.andExpect(status().isNotFound());

		service.salvar(criarErrorDetail());
		service.salvar(criarErrorDetail());

		List<ErrorDetail> erros = repo.findAll();
		assertThat(erros.get(0), notNullValue());
		assertThat(erros.get(1), notNullValue());

		this.mockMvc.perform(get("/api/exception"))
				.andExpect(jsonPath("$[0].idErrorDetail.value", equalTo(erros.get(0).getIdErrorDetail().toString())))
				.andExpect(jsonPath("$[1].idErrorDetail.value", equalTo(erros.get(1).getIdErrorDetail().toString())))
				.andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {

		this.mockMvc.perform(get("/api/exception/" + new ErrorDetailId()))
				.andExpect(jsonPath("$.error", equalTo("A Exceção buscada não existe no banco de dados")))
				.andExpect(status().isNotFound());

		service.salvar(criarErrorDetail());

		List<ErrorDetail> erros = repo.findAll();
		assertThat(erros.get(0), notNullValue());

		this.mockMvc.perform(get("/api/exception/" + erros.get(0).getIdErrorDetail().toString()))
				.andExpect(jsonPath("$.idErrorDetail.value", equalTo(erros.get(0).getIdErrorDetail().toString())))
				.andExpect(status().isOk());

	}

	private CriarErrorDetail criarErrorDetail() {
		CriarErrorDetail error = new CriarErrorDetail();
		error.setDeveloperMessage("mensagem de teste");
		error.setError("erro teste");
		error.setHttpStatus(400);
		error.setType("teste");
		return error;
	}

}