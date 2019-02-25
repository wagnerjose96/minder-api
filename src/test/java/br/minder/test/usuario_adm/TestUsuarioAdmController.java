package br.minder.test.usuario_adm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.genero.Genero;
import br.minder.genero.GeneroId;
import br.minder.genero.GeneroRepository;
import br.minder.genero.comandos.CriarGenero;
import br.minder.login.LoginController;
import br.minder.login.comandos.LogarUsuario;
import br.minder.sangue.Sangue;
import br.minder.sangue.SangueId;
import br.minder.sangue.SangueRepository;
import br.minder.sangue.comandos.CriarSangue;
import br.minder.telefone.comandos.CriarTelefone;
import br.minder.usuario.Usuario;
import br.minder.usuario.UsuarioRepository;
import br.minder.usuario.UsuarioService;
import br.minder.usuario.comandos.CriarUsuario;
import br.minder.usuario_adm.UsuarioAdm;
import br.minder.usuario_adm.UsuarioAdmId;
import br.minder.usuario_adm.UsuarioAdmRepository;
import br.minder.usuario_adm.comandos.CriarUsuarioAdm;
import br.minder.usuario_adm.comandos.EditarUsuarioAdm;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
@ActiveProfiles("application-test")
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
	private GeneroRepository repoGenero;

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
				.perform(post("/api/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarAdmErro("admin"));

		this.mockMvc
				.perform(post("/api/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O administrador não foi salvo devido a um erro interno")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testEditar() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarAdm("admin"));

		this.mockMvc
				.perform(post("/api/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<UsuarioAdm> adms = repo.findAll();
		assertThat(adms.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarAdm(adms.get(0)));

		this.mockMvc
				.perform(put("/api/adm").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi alterado com sucesso")))
				.andExpect(status().isOk());

		this.mockMvc
				.perform(put("/api/adm").header("token", logarAdm("admin", "1234") + "erroToken")
						.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		jsonString = objectMapper.writeValueAsString(editarAdmErroId(adms.get(0)));

		this.mockMvc
				.perform(put("/api/adm").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("O administrador a ser alterado não existe no banco de dados")))
				.andExpect(status().isNotFound());

		jsonString = objectMapper.writeValueAsString(editarAdmErro(adms.get(0)));

		this.mockMvc
				.perform(put("/api/adm").header("token", logarAdm("admin", "1234")).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(jsonString))
				.andExpect(jsonPath("$.error", equalTo("Ocorreu um erro interno durante a alteração do administrador")))
				.andExpect(status().isInternalServerError());
	}

	@Test
	public void testBuscarTodos() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarAdm("admin"));

		this.mockMvc
				.perform(post("/api/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		jsonString = objectMapper.writeValueAsString(criarAdm("adm"));

		this.mockMvc
				.perform(post("/api/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<UsuarioAdm> adms = repo.findAll();
		assertThat(adms.get(0), notNullValue());
		assertThat(adms.get(1), notNullValue());

		this.mockMvc.perform(get("/api/adm").header("token", logarAdm("admin", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc.perform(get("/api/adm").header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$[0].id.value", equalTo(adms.get(0).getId().toString())))
				.andExpect(jsonPath("$[1].id.value", equalTo(adms.get(1).getId().toString())))
				.andExpect(status().isOk());

		String token = logarAdm("admin", "1234");

		this.mockMvc
				.perform(delete("/api/adm/" + adms.get(0).getId().toString()).header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$",
						equalTo("Administrador ===> " + adms.get(0).getId().toString() + ": deletado com sucesso")))
				.andExpect(status().isOk());

		this.mockMvc
				.perform(delete("/api/adm/" + adms.get(1).getId().toString()).header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$",
						equalTo("Administrador ===> " + adms.get(1).getId().toString() + ": deletado com sucesso")))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/api/adm").header("token", token))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhum administrador cadastrado no banco de dados")))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testBuscarTodosUsuarios() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarAdm("admin"));

		this.mockMvc
				.perform(post("/api/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi cadastrado com sucesso")))
				.andExpect(status().isCreated());
		List<UsuarioAdm> adms = repo.findAll();
		assertThat(adms.get(0), notNullValue());

		this.mockMvc.perform(get("/api/adm/usuarios").header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.error", equalTo("Não existe nenhum usuário cadastrado no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc.perform(get("/api/adm/usuarios").header("token", logarAdm("admin", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		GeneroId idGenero = criarGenero("Masculino");
		SangueId idSangue = criarSangue("A+");
		usuarioService.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue));
		usuarioService.salvar(criarUsuario("wagnerjose@hotmail.com", "wagner", idGenero, idSangue));
		List<Usuario> usuarios = repoUsuario.findAll();
		assertThat(usuarios.get(0), notNullValue());
		assertThat(usuarios.get(1), notNullValue());

		this.mockMvc.perform(get("/api/adm/usuarios").header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$[0].id.value", equalTo(usuarios.get(0).getId().toString())))
				.andExpect(jsonPath("$[1].id.value", equalTo(usuarios.get(1).getId().toString())))
				.andExpect(status().isOk());
	}

	@Test
	public void testBuscarPorId() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarAdm("admin"));

		this.mockMvc
				.perform(post("/api/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<UsuarioAdm> adms = repo.findAll();
		assertThat(adms.get(0), notNullValue());

		this.mockMvc
				.perform(get("/api/adm/" + adms.get(0).getId().toString()).header("token",
						logarAdm("admin", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc.perform(get("/api/adm/" + adms.get(0).getId().toString()).header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.id.value", equalTo(adms.get(0).getId().toString()))).andExpect(status().isOk());

		this.mockMvc.perform(get("/api/adm/" + new UsuarioAdmId().toString()).header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.error", equalTo("O administrador procurado não existe no banco de dados")))
				.andExpect(status().isNotFound());

	}

	@Test
	public void testDeletar() throws Exception {
		String jsonString = objectMapper.writeValueAsString(criarAdm("admin"));

		this.mockMvc
				.perform(post("/api/adm").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(jsonString))
				.andExpect(jsonPath("$", equalTo("O administrador foi cadastrado com sucesso")))
				.andExpect(status().isCreated());

		List<UsuarioAdm> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		this.mockMvc
				.perform(delete("/api/adm/" + usuarios.get(0).getId().toString()).header("token",
						logarAdm("admin", "1234") + "erroToken"))
				.andExpect(jsonPath("$.error", equalTo("Acesso negado"))).andExpect(status().isForbidden());

		this.mockMvc.perform(delete("/api/adm/" + new UsuarioAdmId().toString()).header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$.error", equalTo("O administrador a deletar não existe no banco de dados")))
				.andExpect(status().isNotFound());

		this.mockMvc
				.perform(
						delete("/api/adm/" + usuarios.get(0).getId().toString()).header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$",
						equalTo("Administrador ===> " + usuarios.get(0).getId().toString() + ": deletado com sucesso")))
				.andExpect(status().isOk());
	}

	private CriarUsuarioAdm criarAdm(String nome) {
		CriarUsuarioAdm adm = new CriarUsuarioAdm();
		adm.setNome(nome);
		adm.setSenha("1234");
		return adm;
	}

	private CriarUsuarioAdm criarAdmErro(String nome) {
		CriarUsuarioAdm adm = new CriarUsuarioAdm();
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

	private EditarUsuarioAdm editarAdmErroId(UsuarioAdm adm) {
		EditarUsuarioAdm admAtualizado = new EditarUsuarioAdm();
		admAtualizado.setId(new UsuarioAdmId());
		admAtualizado.setNome(adm.getNomeUsuario());
		admAtualizado.setSenha("12345");
		return admAtualizado;
	}

	private EditarUsuarioAdm editarAdmErro(UsuarioAdm adm) {
		EditarUsuarioAdm admAtualizado = new EditarUsuarioAdm();
		admAtualizado.setId(adm.getId());
		admAtualizado.setSenha("12345");
		return admAtualizado;
	}

	private String logarAdm(String nomeUsuario, String senha) throws NoSuchAlgorithmException {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginAdm(corpoLogin).getBody();
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
		return repoSangue.save(new Sangue(new CriarSangue(tipo))).getIdSangue();
	}

	private GeneroId criarGenero(String tipo) {
		return repoGenero.save(new Genero(new CriarGenero(tipo))).getIdGenero();
	}

}