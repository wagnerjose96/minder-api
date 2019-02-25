package br.minder.test.alarme;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import br.minder.MinderApplication;
import br.minder.alarme.Alarme;
import br.minder.alarme.AlarmeId;
import br.minder.alarme.AlarmeRepository;
import br.minder.alarme.AlarmeService;
import br.minder.alarme.comandos.CriarAlarme;
import br.minder.alarme.comandos.EditarAlarme;
import br.minder.conversor.ConverterData;
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.genero.Genero;
import br.minder.genero.GeneroId;
import br.minder.genero.GeneroRepository;
import br.minder.genero.comandos.CriarGenero;
import br.minder.medicamento.MedicamentoId;
import br.minder.medicamento.MedicamentoService;
import br.minder.medicamento.comandos.CriarMedicamento;
import br.minder.sangue.Sangue;
import br.minder.sangue.SangueId;
import br.minder.sangue.SangueRepository;
import br.minder.sangue.comandos.CriarSangue;
import br.minder.telefone.comandos.CriarTelefone;
import br.minder.usuario.Usuario;
import br.minder.usuario.UsuarioId;
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
@ActiveProfiles("application-test")
public class TestAlarmeService {
	@Autowired
	private WebApplicationContext context;

	@SuppressWarnings("unused")
	private MockMvc mockMvc;

	@Autowired
	private SangueRepository repoSangue;

	@Autowired
	private GeneroRepository repoSexo;

	@Autowired
	private UsuarioService serviceUsuario;

	@Autowired
	private MedicamentoService serviceMedicamento;

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private AlarmeRepository repoAlarme;

	@Autowired
	private AlarmeService serviceAlarme;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idSexo = criarSexo("Masculino");

		UsuarioId idUsuario = serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue))
				.get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		AlarmeId idAlarme = serviceAlarme.salvar(criarAlarme(idMedicamento, "Tomar medicamento"), idUsuario).get();

		Alarme alarme = repoAlarme.findAll().get(0);
		assertThat(alarme, notNullValue());

		assertThat(alarme.getId().getValue().toString(), equalTo(idAlarme.toString()));
		assertThat(ConverterData.converterData(alarme.getDataInicio().getTime()), equalTo("10-07-2018"));
		assertThat(ConverterData.converterData(alarme.getDataFim().getTime()), equalTo("20-07-2018"));
		assertThat(alarme.getDescricao(), equalTo("Tomar medicamento"));
		assertThat(alarme.getIdMedicamento().toString(), equalTo(idMedicamento.toString()));
		assertThat(alarme.getPeriodicidade(), equalTo(8));
		assertThat(alarme.getQuantidade(), equalTo("1"));
		 assertThat(alarme.getHoraPrimeiraDose().toString(), equalTo("12:12:00"));
		 assertThat(alarme.getHoraUltimaDose().toString(), equalTo("12:12:00"));
		assertThat(alarme.getIdUsuario().toString(), equalTo(idUsuario.toString()));
	}

	@Test
	public void testEditar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idSexo = criarSexo("Masculino");

		UsuarioId idUsuario = serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue))
				.get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		AlarmeId idAlarme = serviceAlarme.salvar(criarAlarme(idMedicamento, "Tomar medicamento"), idUsuario).get();

		Alarme alarme = repoAlarme.findAll().get(0);
		assertThat(alarme, notNullValue());

		assertThat(alarme.getId().getValue().toString(), equalTo(idAlarme.toString()));
		assertThat(ConverterData.converterData(alarme.getDataInicio().getTime()), equalTo("10-07-2018"));
		assertThat(ConverterData.converterData(alarme.getDataFim().getTime()), equalTo("20-07-2018"));
		assertThat(alarme.getDescricao(), equalTo("Tomar medicamento"));
		assertThat(alarme.getIdMedicamento().toString(), equalTo(idMedicamento.toString()));
		assertThat(alarme.getPeriodicidade(), equalTo(8));
		assertThat(alarme.getQuantidade(), equalTo("1"));
		assertThat(alarme.getIdUsuario().toString(), equalTo(idUsuario.toString()));
		assertThat(alarme.getHoraPrimeiraDose().toString(), equalTo("12:12:00"));
		assertThat(alarme.getHoraUltimaDose().toString(), equalTo("12:12:00"));

		AlarmeId idAlarmeEditado = serviceAlarme.alterar(editarAlarme(alarme)).get();

		Alarme alarmeEditado = repoAlarme.findAll().get(0);
		assertThat(alarmeEditado, notNullValue());

		assertThat(repoAlarme.findAll().size(), equalTo(1));

		assertThat(alarmeEditado.getId().getValue().toString(), equalTo(idAlarmeEditado.toString()));
		assertThat(ConverterData.converterData(alarme.getDataInicio().getTime()), equalTo("10-07-2018"));
		assertThat(ConverterData.converterData(alarme.getDataFim().getTime()), equalTo("20-07-2018"));
		assertThat(alarmeEditado.getDescricao(), equalTo("Tomar medicamento Editado !!!"));
		assertThat(alarmeEditado.getIdMedicamento().toString(), equalTo(idMedicamento.toString()));
		assertThat(alarmeEditado.getPeriodicidade(), equalTo(8));
		assertThat(alarmeEditado.getQuantidade(), equalTo("1"));
		assertThat(alarme.getHoraPrimeiraDose().toString(), equalTo("12:12:00"));
		assertThat(alarme.getHoraUltimaDose().toString(), equalTo("16:12:00"));
		assertThat(alarmeEditado.getIdUsuario().toString(), equalTo(idUsuario.toString()));
	}

	@Test
	public void testBuscarPorId() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idSexo = criarSexo("Masculino");

		UsuarioId idUsuario = serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue))
				.get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		AlarmeId idAlarme = serviceAlarme.salvar(criarAlarme(idMedicamento, "Tomar medicamento"), idUsuario).get();

		Alarme alarme = repoAlarme.findAll().get(0);
		assertThat(alarme, notNullValue());

		assertThat(alarme.getId().getValue().toString(), equalTo(idAlarme.toString()));
		assertThat(ConverterData.converterData(alarme.getDataInicio().getTime()), equalTo("10-07-2018"));
		assertThat(ConverterData.converterData(alarme.getDataFim().getTime()), equalTo("20-07-2018"));
		assertThat(alarme.getDescricao(), equalTo("Tomar medicamento"));
		assertThat(alarme.getIdMedicamento().toString(), equalTo(idMedicamento.toString()));
		assertThat(alarme.getPeriodicidade(), equalTo(8));
		assertThat(alarme.getQuantidade(), equalTo("1"));
		assertThat(alarme.getHoraPrimeiraDose().toString(), equalTo("12:12:00"));
		assertThat(alarme.getHoraUltimaDose().toString(), equalTo("12:12:00"));
		assertThat(alarme.getIdUsuario().toString(), equalTo(idUsuario.toString()));

		Alarme alarmeEncontrado = repoAlarme.findById(alarme.getId()).get();
		assertThat(alarmeEncontrado.getId().getValue().toString(), equalTo(idAlarme.toString()));
		assertThat(ConverterData.converterData(alarmeEncontrado.getDataInicio().getTime()), equalTo("10-07-2018"));
		assertThat(ConverterData.converterData(alarmeEncontrado.getDataFim().getTime()), equalTo("20-07-2018"));
		assertThat(alarmeEncontrado.getDescricao(), equalTo("Tomar medicamento"));
		assertThat(alarmeEncontrado.getPeriodicidade(), equalTo(8));
		assertThat(alarmeEncontrado.getQuantidade(), equalTo("1"));
		assertThat(alarme.getHoraPrimeiraDose().toString(), equalTo("12:12:00"));
		assertThat(alarme.getHoraUltimaDose().toString(), equalTo("12:12:00"));
		assertThat(alarmeEncontrado.getIdMedicamento().toString(), equalTo(idMedicamento.toString()));

	}

	@Test
	public void testBurcarTodos() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idSexo = criarSexo("Masculino");

		UsuarioId idUsuario = serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue))
				.get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		AlarmeId idAlarme1 = serviceAlarme.salvar(criarAlarme(idMedicamento, "Tomar medicamento"), idUsuario).get();

		Alarme alarme1 = repoAlarme.findAll().get(0);
		assertThat(alarme1, notNullValue());

		assertThat(alarme1.getId().getValue().toString(), equalTo(idAlarme1.toString()));
		assertThat(ConverterData.converterData(alarme1.getDataInicio().getTime()), equalTo("10-07-2018"));
		assertThat(ConverterData.converterData(alarme1.getDataFim().getTime()), equalTo("20-07-2018"));
		assertThat(alarme1.getDescricao(), equalTo("Tomar medicamento"));
		assertThat(alarme1.getIdMedicamento().toString(), equalTo(idMedicamento.toString()));
		assertThat(alarme1.getPeriodicidade(), equalTo(8));
		assertThat(alarme1.getQuantidade(), equalTo("1"));
		assertThat(alarme1.getHoraPrimeiraDose().toString(), equalTo("12:12:00"));
		assertThat(alarme1.getHoraUltimaDose().toString(), equalTo("12:12:00"));
		assertThat(alarme1.getIdUsuario().toString(), equalTo(idUsuario.toString()));

		AlarmeId idAlarme2 = serviceAlarme.salvar(criarAlarme(idMedicamento, "Tomar medicamento 2"), idUsuario).get();

		List<Alarme> alarmes = repoAlarme.findAll();
		assertThat(alarmes.size(), equalTo(2));
		Alarme alarme2 = alarmes.get(1);
		assertThat(alarme2, notNullValue());

		assertThat(alarme2.getId().getValue().toString(), equalTo(idAlarme2.toString()));
		assertThat(ConverterData.converterData(alarme2.getDataInicio().getTime()), equalTo("10-07-2018"));
		assertThat(ConverterData.converterData(alarme2.getDataFim().getTime()), equalTo("20-07-2018"));
		assertThat(alarme2.getDescricao(), equalTo("Tomar medicamento 2"));
		assertThat(alarme2.getIdMedicamento().toString(), equalTo(idMedicamento.toString()));
		assertThat(alarme2.getPeriodicidade(), equalTo(8));
		assertThat(alarme2.getQuantidade(), equalTo("1"));
		assertThat(alarme2.getHoraPrimeiraDose().toString(), equalTo("12:12:00"));
		assertThat(alarme2.getHoraUltimaDose().toString(), equalTo("12:12:00"));
		assertThat(alarme2.getIdUsuario().toString(), equalTo(idUsuario.toString()));

	}

	@Test
	public void testDeletar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		GeneroId idSexo = criarSexo("Masculino");

		UsuarioId idUsuario = serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue))
				.get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		AlarmeId idAlarme = serviceAlarme.salvar(criarAlarme(idMedicamento, "Tomar medicamento"), idUsuario).get();

		Alarme alarme = repoAlarme.findAll().get(0);
		assertThat(alarme, notNullValue());

		assertThat(alarme.getId().getValue().toString(), equalTo(idAlarme.toString()));
		assertThat(ConverterData.converterData(alarme.getDataInicio().getTime()), equalTo("10-07-2018"));
		assertThat(ConverterData.converterData(alarme.getDataFim().getTime()), equalTo("20-07-2018"));
		assertThat(alarme.getDescricao(), equalTo("Tomar medicamento"));
		assertThat(alarme.getIdMedicamento().toString(), equalTo(idMedicamento.toString()));
		assertThat(alarme.getPeriodicidade(), equalTo(8));
		assertThat(alarme.getQuantidade(), equalTo("1"));
		assertThat(alarme.getHoraPrimeiraDose().toString(), equalTo("12:12:00"));
		assertThat(alarme.getHoraUltimaDose().toString(), equalTo("12:12:00"));
		assertThat(alarme.getIdUsuario().toString(), equalTo(idUsuario.toString()));

		Optional<String> mensagemDeletado = serviceAlarme.deletar(idAlarme, idUsuario);

		assertThat(mensagemDeletado.get(), notNullValue());
		assertThat(mensagemDeletado.get(),
				equalTo("Alarme ===> " + alarme.getId().toString() + ": deletado com sucesso"));
	}

	private CriarAlarme criarAlarme(MedicamentoId idMedicamento, String descrição) {
		CriarAlarme alarme = new CriarAlarme();
		alarme.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarme.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarme.setDescricao(descrição);
		alarme.setIdMedicamento(idMedicamento);
		alarme.setPeriodicidade(8);
		alarme.setQuantidade("1");
		alarme.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		return alarme;
	}

	private EditarAlarme editarAlarme(Alarme alarme) {
		EditarAlarme alarmeEditado = new EditarAlarme();
		alarmeEditado.setId(alarme.getId());
		alarmeEditado.setDataInicio(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alarmeEditado.setDataFim(Date.valueOf(LocalDate.of(2018, 07, 20)));
		alarmeEditado.setDescricao("Tomar medicamento Editado !!!");
		alarmeEditado.setIdMedicamento(alarme.getIdMedicamento());
		alarmeEditado.setPeriodicidade(8);
		alarmeEditado.setQuantidade("1");
		alarmeEditado.setHoraPrimeiraDose(Time.valueOf(LocalTime.of(12, 12)));
		alarmeEditado.setHoraUltimaDose(Time.valueOf(LocalTime.of(16, 12)));
		return alarmeEditado;
	}

	private CriarMedicamento criarMedicamento(String nome, String composicao) {
		CriarMedicamento medicamento = new CriarMedicamento();
		medicamento.setComposicao(composicao);
		medicamento.setNomeMedicamento(nome);
		return medicamento;
	}

	private CriarUsuario criarUsuario(String email, String username, GeneroId idSexo, SangueId idSangue) {
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
		usuario.setIdGenero(idSexo);
		usuario.setSenha("1234");
		usuario.setTelefone(telefone);
		usuario.setUsername(username);

		return usuario;
	}

	private SangueId criarSangue(String tipo) {
		SangueId id = repoSangue.save(new Sangue(new CriarSangue(tipo))).getIdSangue();
		return id;
	}

	private GeneroId criarSexo(String tipo) {
		GeneroId id = repoSexo.save(new Genero(new CriarGenero(tipo))).getIdGenero();
		return id;
	}
}