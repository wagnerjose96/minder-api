package br.minder.test.contato;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
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
import br.minder.contato.ContatoId;
import br.minder.contato.ContatoService;
import br.minder.contato.comandos.CriarContato;
import br.minder.contato.contato_emergencia.ContatoEmergencia;
import br.minder.contato.contato_emergencia.ContatoEmergenciaService;
import br.minder.emergencia.Emergencia;
import br.minder.emergencia.EmergenciaRepository;
import br.minder.emergencia.EmergenciaService;
import br.minder.emergencia.comandos.CriarEmergencia;
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.genero.Genero;
import br.minder.genero.GeneroId;
import br.minder.genero.GeneroRepository;
import br.minder.genero.comandos.CriarGenero;
import br.minder.sangue.Sangue;
import br.minder.sangue.SangueId;
import br.minder.sangue.SangueRepository;
import br.minder.sangue.comandos.CriarSangue;
import br.minder.telefone.comandos.CriarTelefone;
import br.minder.usuario.Usuario;
import br.minder.usuario.UsuarioRepository;
import br.minder.usuario.UsuarioService;
import br.minder.usuario.comandos.CriarUsuario;

@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
public class TestContatoEmergenciaService {

	@Autowired
	private WebApplicationContext context;

	@SuppressWarnings("unused")
	@Autowired
	private ObjectMapper objectMapper;

	@SuppressWarnings("unused")
	private MockMvc mockMvc;

	@Autowired
	private SangueRepository repoSangue;

	@Autowired
	private GeneroRepository repoGenero;

	@Autowired
	private UsuarioService serviceUsuario;

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private EmergenciaRepository repoEmergencia;

	@Autowired
	private EmergenciaService serviceEmergencia;

	@Autowired
	private ContatoEmergenciaService contatoEmergenciaService;

	@Autowired
	private ContatoService serviceContato;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idGenero = criarGenero("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idGenero, idSangue)).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		serviceEmergencia.salvar(criarEmergencia(), usuarios.get(0).getId());

		List<Emergencia> emergencias = repoEmergencia.findAll();
		assertThat(emergencias.get(0), notNullValue());

		ContatoId idContato = serviceContato.salvar(criarContato("wagner"), usuarios.get(0).getId()).get();

		ContatoEmergencia contatoEmergencia = new ContatoEmergencia();
		contatoEmergencia.setIdContato(idContato);
		contatoEmergencia.setIdEmergencia(emergencias.get(0).getIdEmergencia());

		contatoEmergenciaService.salvar(contatoEmergencia);

		List<ContatoEmergencia> contatosEmergencias = contatoEmergenciaService.encontrar();

		assertThat(contatosEmergencias.get(0), notNullValue());
		assertThat(contatosEmergencias.get(0).getIdContato().toString(), equalTo(idContato.toString()));
		assertThat(contatosEmergencias.get(0).getIdEmergencia().toString(),
				equalTo(emergencias.get(0).getIdEmergencia().toString()));

	}

	private CriarContato criarContato(String nome) {
		CriarContato contato = new CriarContato();
		contato.setTelefone(telefone());
		contato.setNome(nome);
		return contato;
	}

	private CriarTelefone telefone() {
		CriarTelefone telefone = new CriarTelefone();
		telefone.setDdd(44);
		telefone.setNumero(997703828);
		return telefone;
	}

	private CriarEmergencia criarEmergencia() {
		CriarEmergencia emergencia = new CriarEmergencia();
		emergencia.setAtaqueConvulsivos(1);
		emergencia.setDoadorDeOrgaos(1);
		emergencia.setProblemasCardiacos("Não");
		return emergencia;
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
		SangueId id = repoSangue.save(new Sangue(new CriarSangue(tipo))).getIdSangue();
		return id;
	}

	private GeneroId criarGenero(String tipo) {
		GeneroId id = repoGenero.save(new Genero(new CriarGenero(tipo))).getIdGenero();
		return id;
	}
}
