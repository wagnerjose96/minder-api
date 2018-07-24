package br.minder.test.alergia;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import br.minder.alergia.AlergiaRepository;
import br.minder.alergia.alergia_medicamento.AlergiaMedicamentoService;
import br.minder.alergia.Alergia;
import br.minder.alergia.AlergiaId;
import br.minder.alergia.comandos.CriarAlergia;
import br.minder.alergia.alergia_medicamento.AlergiaMedicamento;
import br.minder.endereco.comandos.CriarEndereco;
import br.minder.medicamento.MedicamentoId;
import br.minder.medicamento.MedicamentoService;
import br.minder.medicamento.comandos.CriarMedicamento;
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

@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class })
@Transactional
@Rollback
@WebAppConfiguration
@SpringBootTest(classes = { MinderApplication.class }, webEnvironment = WebEnvironment.MOCK)
public class TestAlergiaMedicamentoService {

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
	private SexoRepository repoSexo;

	@Autowired
	private UsuarioService serviceUsuario;

	@Autowired
	private MedicamentoService serviceMedicamento;

	@Autowired
	private UsuarioRepository repo;

	@Autowired
	private AlergiaRepository repoAlergia;

	@Autowired
	private AlergiaMedicamentoService alergiaMedicamentoService;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testCadastrar() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		Set<MedicamentoId> idsMedicamentos = new HashSet<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		Alergia alergia = new Alergia(criarAlergia(idsMedicamentos, "Asma"), usuarios.get(0).getId());
		AlergiaId idAlergia = repoAlergia.save(alergia).getIdAlergia();

		AlergiaMedicamento alergiaMedicamento = new AlergiaMedicamento();
		alergiaMedicamento.setIdAlergia(idAlergia);
		alergiaMedicamento.setIdMedicamento(idMedicamento);

		alergiaMedicamentoService.salvar(alergiaMedicamento);

		List<AlergiaMedicamento> alergiasMedicamentos = alergiaMedicamentoService.encontrar();
		assertThat(alergiasMedicamentos.size(), equalTo(1));
		assertThat(alergiasMedicamentos.get(0).getId().toString(), equalTo(alergiaMedicamento.getId().toString()));
		assertThat(alergiasMedicamentos.get(0).getIdAlergia().toString(),
				equalTo(alergiaMedicamento.getIdAlergia().toString()));
		assertThat(alergiasMedicamentos.get(0).getIdMedicamento().toString(),
				equalTo(alergiaMedicamento.getIdMedicamento().toString()));

	}

	@Test
	public void testBuscarPorId() throws Exception {
		SangueId idSangue = criarSangue("A+");
		SexoId idSexo = criarSexo("Masculino");

		serviceUsuario.salvar(criarUsuario("wagner@hotmail.com", "wagnerju", idSexo, idSangue)).get();
		MedicamentoId idMedicamento = serviceMedicamento.salvar(criarMedicamento("DorFlex", "100mg")).get();
		Set<MedicamentoId> idsMedicamentos = new HashSet<MedicamentoId>();
		idsMedicamentos.add(idMedicamento);

		List<Usuario> usuarios = repo.findAll();
		assertThat(usuarios.get(0), notNullValue());

		Alergia alergia = new Alergia(criarAlergia(idsMedicamentos, "Asma"), usuarios.get(0).getId());
		AlergiaId idAlergia = repoAlergia.save(alergia).getIdAlergia();

		AlergiaMedicamento alergiaMedicamento = new AlergiaMedicamento();
		alergiaMedicamento.setIdAlergia(idAlergia);
		alergiaMedicamento.setIdMedicamento(idMedicamento);

		alergiaMedicamentoService.salvar(alergiaMedicamento);

		Optional<AlergiaMedicamento> alergiasMedicamentos = alergiaMedicamentoService.encontrar(alergiaMedicamento.getId());
		assertThat(alergiasMedicamentos.get(), notNullValue());
		assertThat(alergiasMedicamentos.get().getId().toString(), equalTo(alergiaMedicamento.getId().toString()));
		assertThat(alergiasMedicamentos.get().getIdAlergia().toString(),
				equalTo(alergiaMedicamento.getIdAlergia().toString()));
		assertThat(alergiasMedicamentos.get().getIdMedicamento().toString(),
				equalTo(alergiaMedicamento.getIdMedicamento().toString()));		
	}

	private CriarAlergia criarAlergia(Set<MedicamentoId> idsMedicamentos, String nomeAlergia) {
		CriarAlergia alergia = new CriarAlergia();
		alergia.setDataDescoberta(Date.valueOf(LocalDate.of(2018, 07, 10)));
		alergia.setIdMedicamentos(idsMedicamentos);
		alergia.setTipoAlergia(nomeAlergia);
		alergia.setLocalAfetado("pulmao");
		alergia.setEfeitos("dor");
		return alergia;
	}

	private CriarMedicamento criarMedicamento(String nome, String composicao) {
		CriarMedicamento medicamento = new CriarMedicamento();
		medicamento.setComposicao(composicao);
		medicamento.setNomeMedicamento(nome);
		return medicamento;
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
		SangueId id = repoSangue.save(new Sangue(new CriarSangue(tipo))).getIdSangue();
		return id;
	}

	private SexoId criarSexo(String tipo) {
		SexoId id = repoSexo.save(new Sexo(new CriarSexo(tipo))).getIdGenero();
		return id;
	}
}

