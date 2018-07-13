package br.hela.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import br.hela.Escoladeti2018Application;
import br.hela.endereco.comandos.CriarEndereco;
import br.hela.endereco.comandos.EditarEndereco;
import br.hela.login.LoginController;
import br.hela.login.comandos.LogarAdm;
import br.hela.login.comandos.LogarUsuario;
import br.hela.sangue.Sangue;
import br.hela.sangue.SangueId;
import br.hela.sangue.SangueRepository;
import br.hela.sangue.comandos.CriarSangue;
import br.hela.sexo.Sexo;
import br.hela.sexo.SexoId;
import br.hela.sexo.SexoRepository;
import br.hela.sexo.comandos.CriarSexo;
import br.hela.telefone.comandos.CriarTelefone;
import br.hela.telefone.comandos.EditarTelefone;
import br.hela.usuario.Usuario;
import br.hela.usuario.UsuarioRepository;
import br.hela.usuario.comandos.CriarUsuario;
import br.hela.usuario.comandos.EditarUsuario;
import br.hela.usuario_adm.UsuarioAdm;
import br.hela.usuario_adm.UsuarioAdmRepository;
import br.hela.usuario_adm.UsuarioAdmService;
import br.hela.usuario_adm.comandos.CriarUsuarioAdm;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { Escoladeti2018Application.class }, webEnvironment = WebEnvironment.MOCK)
public class TestUsuarioController {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Autowired
	private SangueRepository repoSangue;

	@Autowired
	private SexoRepository repoSexo;

	@Autowired
	private LoginController login;

	@Autowired
	private UsuarioRepository repo;

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
		SexoId idSexo = criarSexo("Masculino");
		SangueId idSangue = criarSangue("A+");

		String jsonString = objectMapper
				.writeValueAsString(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue));

		this.mockMvc.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(jsonString)).andExpect(status().isCreated());
	}

	@Test
	public void testEditar() throws Exception {
		SexoId idSexo = criarSexo("Masculino");
		SangueId idSangue = criarSangue("A+");

		String jsonString = objectMapper
				.writeValueAsString(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue));

		this.mockMvc.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(jsonString)).andExpect(status().isCreated());

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		jsonString = objectMapper.writeValueAsString(editarUsuario(usuarios.get(0)));

		this.mockMvc
				.perform(put("/usuarios").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.header("token", logar("wagnerju", "1234")).content(jsonString))
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$", equalTo("O usuário foi alterado com sucesso"))).andExpect(status().isOk());

	}

	@Test
	public void testBuscarPorId() throws Exception {
		SexoId idSexo = criarSexo("Masculino");
		SangueId idSangue = criarSangue("A+");

		String jsonString = objectMapper
				.writeValueAsString(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue));

		this.mockMvc.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(jsonString)).andExpect(status().isCreated());

		List<Usuario> usuarios = repo.findAll();

		this.mockMvc.perform(get("/usuarios/" + usuarios.get(0).getId().toString()))
				.andExpect(jsonPath("$", notNullValue())).andExpect(jsonPath("$.username", equalTo("wagnerju")))
				.andExpect(status().isOk());
	}

	@Test
	public void testBurcarTodos() throws Exception {
		SexoId idSexo = criarSexo("Masculino");
		SangueId idSangue = criarSangue("A+");

		String jsonString = objectMapper
				.writeValueAsString(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue));

		this.mockMvc.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(jsonString)).andExpect(status().isCreated());

		jsonString = objectMapper
				.writeValueAsString(criarUsuario("wagner_junior@hotmail.com", "wagner", idSexo, idSangue));

		this.mockMvc.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(jsonString)).andExpect(status().isCreated());

		admService.salvar(criarAdm());

		List<UsuarioAdm> adm = repoAdm.findAll();
		assertThat(adm.get(0), notNullValue());
		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		this.mockMvc.perform(get("/usuarios").header("token", logarAdm("admin", "1234")))
				.andExpect(jsonPath("$", notNullValue())).andExpect(jsonPath("$[0].username", equalTo("wagnerju")))
				.andExpect(jsonPath("$[1].username", equalTo("wagner"))).andExpect(status().isOk());

	}

	@Test
	public void testDeletar() throws Exception {
		SexoId idSexo = criarSexo("Masculino");
		SangueId idSangue = criarSangue("A+");

		String jsonString = objectMapper
				.writeValueAsString(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue));

		this.mockMvc.perform(post("/usuarios").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(jsonString)).andExpect(status().isCreated());

		List<Usuario> usuarios = repo.findAll();

		this.mockMvc
				.perform(delete("/usuarios/" + usuarios.get(0).getId().toString()).header("token",
						logar("wagnerju", "1234")))
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$",
						equalTo("Usuário ===> " + usuarios.get(0).getId().toString() + ": deletado com sucesso")))
				.andExpect(status().isOk());
	}

	private CriarUsuario criarUsuario(String email, String username, SexoId idSexo, SangueId idSangue) {
		CriarEndereco endereco = new CriarEndereco();
		endereco.setBairro("Zona 6");
		endereco.setCidade("Maringá");
		endereco.setEstado("Paraná");
		endereco.setNumero(1390);
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

	private EditarUsuario editarUsuario(Usuario usuario) {
		EditarEndereco endereco = new EditarEndereco();
		endereco.setId(usuario.getIdEndereco());
		endereco.setBairro("Zona 6");
		endereco.setCidade("Maringá");
		endereco.setEstado("Paraná");
		endereco.setNumero(1390);
		endereco.setRua("Castro Alves");

		EditarTelefone telefone = new EditarTelefone();
		telefone.setId(usuario.getIdTelefone());
		telefone.setDdd(44);
		telefone.setNumero(999038860);

		EditarUsuario usuarioEditado = new EditarUsuario();
		usuarioEditado.setId(usuario.getId());
		usuarioEditado.setSenha(usuario.getSenha());
		usuarioEditado.setNome("Wagner Junior");
		usuarioEditado.setEndereco(endereco);
		usuarioEditado.setTelefone(telefone);
		usuarioEditado.setImagem(usuario.getImagemUsuario());

		return usuarioEditado;
	}

	private String logar(String nomeUsuario, String senha) {
		LogarUsuario corpoLogin = new LogarUsuario();
		corpoLogin.setIdentificador(nomeUsuario);
		corpoLogin.setSenha(senha);
		return login.loginUsuario(corpoLogin).getBody();
	}

	private SangueId criarSangue(String tipo) {
		return repoSangue.save(new Sangue(new CriarSangue(tipo))).getIdSangue();
	}

	private SexoId criarSexo(String tipo) {
		return repoSexo.save(new Sexo(new CriarSexo(tipo))).getIdGenero();
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