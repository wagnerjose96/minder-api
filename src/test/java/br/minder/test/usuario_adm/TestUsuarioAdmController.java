package br.minder.test.usuario_adm;

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
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.login.LoginController;
import br.minder.login.comandos.LogarAdm;
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
import br.minder.usuario_adm.UsuarioAdm;
import br.minder.usuario_adm.UsuarioAdmRepository;
import br.minder.usuario_adm.comandos.CriarUsuarioAdm;
import br.minder.usuario_adm.comandos.EditarUsuarioAdm;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
public class TestUsuarioAdmController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private UsuarioAdmRepository repo;

	@Autowired
	private LoginController login;

	@Autowired
	private SangueRepository repoSangue;

	@Autowired
	private SexoRepository repoSexo;

	@Autowired
	private UsuarioRepository repoUsuario;

	@Autowired
	private UsuarioService usuarioService;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarAdm("admin"));

		this.mockMvc
				.perform(post("/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi cadastrado com sucesso")))
				.andExpect(status().isCreated());
	}

	@Test
	public void testEditar() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarAdm("admin"));

		this.mockMvc
				.perform(post("/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<UsuarioAdm> adms = repo.findAll();
		assertThat(adms.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarAdm(adms.get(0)));

		this.mockMvc
				.perform(put("/adm").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi alterado com sucesso")))
				.andExpect(status().isOk());
	}

	@Test
	public void testBuscarTodos() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarAdm("admin"));

		this.mockMvc
				.perform(post("/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarAdm("adm"));

		this.mockMvc
				.perform(post("/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<UsuarioAdm> adms = repo.findAll();
		assertThat(adms.get(0), notNullValue());
		assertThat(adms.get(1), notNullValue());

		this.mockMvc.perform(get("/adm").header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$[0].id.value", equalTo(adms.get(0).getId().toString())))
				.andExpect(jsonPath("$[1].id.value", equalTo(adms.get(1).getId().toString())))
				.andExpect(status().isOk());
	}

	@Test
	public void testBuscarTodosUsuarios() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarAdm("admin"));

		this.mockMvc
				.perform(post("/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi cadastrado com sucesso")))
				.andExpect(status().isCreated());
		List<UsuarioAdm> adms = repo.findAll();
		assertThat(adms.get(0), notNullValue());

		SexoId idSexo = criarSexo("Masculino");
		SangueId idSangue = criarSangue("A+");
		usuarioService.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue));
		usuarioService.salvar(criarUsuario("wagnerjose@hotmail.com", "wagner", idSexo, idSangue));
		List<Usuario> usuarios = repoUsuario.findAll();
		assertThat(usuarios.get(0), notNullValue());
		assertThat(usuarios.get(1), notNullValue());

		this.mockMvc.perform(get("/adm/usuarios").header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$[0].id.value", equalTo(usuarios.get(0).getId().toString())))
				.andExpect(jsonPath("$[1].id.value", equalTo(usuarios.get(1).getId().toString())))
				.andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarAdm("admin"));

		this.mockMvc
				.perform(post("/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<UsuarioAdm> adms = repo.findAll();
		assertThat(adms.get(0), notNullValue());

		this.mockMvc.perform(get("/adm/" + adms.get(0).getId().toString()).header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.id.value", equalTo(adms.get(0).getId().toString()))).andExpect(status().isOk());
	}

	private CriarUsuarioAdm criarAdm(String nome) {
		CriarUsuarioAdm adm = new CriarUsuarioAdm();
		adm.setNome(nome);
		adm.setSenha("1234");
		return adm;
	}

	private EditarUsuarioAdm editarAdm(UsuarioAdm adm) {
		EditarUsuarioAdm admAtualizado = new EditarUsuarioAdm();
		admAtualizado.setId(adm.getId());
		admAtualizado.setNome(adm.getNomeUsuario());
		admAtualizado.setSenha("12345");
		return admAtualizado;
	}

	private String logarAdm(String nomeUsuario, String senha) {
		LogarAdm corpoLogin = new LogarAdm();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginAdm(corpoLogin).getBody();
	}

	private CriarUsuario criarUsuario(String email, String username, SexoId idSexo, SangueId idSangue) {
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
		usuario.setIdSexo(idSexo);
		usuario.setSenha("1234");
		usuario.setTelefone(telefone);
		usuario.setUsername(username);

		return usuario;
	}

	private SangueId criarSangue(String tipo) {
		return repoSangue.save(new Sangue(new CriarSangue(tipo))).getIdSangue();
	}

	private SexoId criarSexo(String tipo) {
		return repoSexo.save(new Sexo(new CriarSexo(tipo))).getIdGenero();
	}

}