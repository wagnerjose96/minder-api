package br.hela.test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import br.hela.Escoladeti2018Application;
import br.hela.login.LoginController;
import br.hela.login.comandos.LogarAdm;
import br.hela.medicamento.comandos.CriarMedicamento;
import br.hela.usuario_adm.UsuarioAdm;
import br.hela.usuario_adm.UsuarioAdmRepository;
import br.hela.usuario_adm.UsuarioAdmService;
import br.hela.usuario_adm.comandos.CriarUsuarioAdm;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { Escoladeti2018Application.class }, webEnvironment = WebEnvironment.MOCK)

public class TestMedicamentoController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

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

		String jsonString = objectMapper.writeValueAsString(criarMedicamento());

		this.mockMvc
				.perform(post("/medicamentos").header("token", logarAdm("admin", "1234"))
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(status().isCreated());
	}

	@Test
	public void testeEditar() throws Exception {

	}

	private CriarMedicamento criarMedicamento() {
		CriarMedicamento medicamento = new CriarMedicamento();
		medicamento.setNomeMedicamento("Medicamento de Teste");
		medicamento.setComposicao("teste");
		return medicamento;
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
