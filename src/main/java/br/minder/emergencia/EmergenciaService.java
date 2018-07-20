package br.minder.emergencia;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.minder.alergia.AlergiaService;
import br.minder.alergia.comandos.BuscarAlergia;
import br.minder.cirurgia.CirurgiaService;
import br.minder.cirurgia.comandos.BuscarCirurgia;
import br.minder.contato.ContatoId;
import br.minder.contato.comandos.BuscarContato;
import br.minder.doenca.DoencaService;
import br.minder.doenca.comandos.BuscarDoenca;
import br.minder.emergencia.comandos.BuscarEmergencia;
import br.minder.emergencia.comandos.CriarEmergencia;
import br.minder.emergencia.comandos.EditarEmergencia;
import br.minder.endereco.comandos.BuscarEndereco;
import br.minder.sangue.comandos.BuscarSangue;
import br.minder.telefone.TelefoneId;
import br.minder.telefone.comandos.BuscarTelefone;
import br.minder.usuario.UsuarioId;
import br.minder.usuario.UsuarioService;
import br.minder.usuario.comandos.BuscarUsuario;

@Service
@Transactional
public class EmergenciaService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sql = "select e.id_emergencia, b.id_contato, c.id, c.nome, t.ddd, t.numero, t.id as id_telefone "
			+ "from emergencia e " + "inner join contato_emergencia b on e.id_emergencia = b.id_emergencia "
			+ "inner join contato c on c.id = b.id_contato " + "inner join telefone t on c.id_telefone = t.id "
			+ "group by e.id_emergencia, c.id, b.id_contato, t.id having e.id_emergencia = ? ";

	@Autowired
	private AlergiaService alergiaService;

	@Autowired
	private CirurgiaService cirurgiaService;

	@Autowired
	private DoencaService doencaService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EmergenciaRepository repo;

	public Optional<EmergenciaId> salvar(CriarEmergencia comando, UsuarioId id) {
		Emergencia novo = repo.save(new Emergencia(comando, id));
		return Optional.of(novo.getIdEmergencia());
	}

	public Optional<BuscarEmergencia> encontrar(EmergenciaId id, UsuarioId idUsuario) {
		Optional<Emergencia> emergencia = repo.findById(id);
		if (emergencia.isPresent() && emergencia.get().getIdUsuario().toString().equals(idUsuario.toString())) {
			return Optional.of(
					construirEmergencia(usuarioService.encontrar(idUsuario), new BuscarEmergencia(emergencia.get())));
		}
		return Optional.empty();
	}

	private BuscarEmergencia construirEmergencia(Optional<BuscarUsuario> user, BuscarEmergencia resultado) {
		List<BuscarContato> contatos = executeQuery(resultado.getId().toString(), sql);
		if (user.isPresent()) {
			String nome = user.get().getNome();
			BuscarEndereco endereco = user.get().getEndereco();
			BuscarSangue sangue = user.get().getSangue();
			Optional<List<BuscarAlergia>> alergias = alergiaService.encontrar(user.get().getId());
			Optional<List<BuscarCirurgia>> cirurgias = cirurgiaService.encontrar(user.get().getId());
			Optional<List<BuscarDoenca>> doencas = doencaService.encontrar(user.get().getId());
			if (alergias.isPresent() && cirurgias.isPresent() && doencas.isPresent()) {
				resultado.setAlergias(alergias.get());
				resultado.setCirurgias(cirurgias.get());
				resultado.setDoencas(doencas.get());
			}
			resultado.setNomeDoUsuario(nome);
			resultado.setTipoSanguineo(sangue);
			resultado.setEndereco(endereco);
			resultado.setContatos(contatos);

		}
		return resultado;
	}

	public Optional<BuscarEmergencia> encontrar(UsuarioId id) {
		List<Emergencia> emergencias = repo.findAll();
		for (Emergencia emergencia : emergencias) {
			if (id.toString().equals(emergencia.getIdUsuario().toString())) {
				BuscarEmergencia resultados = construirEmergencia(usuarioService.encontrar(id),
						new BuscarEmergencia(emergencia));
				return Optional.of(resultados);
			}
		}
		return Optional.empty();
	}

	public Optional<EmergenciaId> alterar(EditarEmergencia comando) {
		Optional<Emergencia> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			Emergencia emer = optional.get();
			emer.apply(comando);
			repo.save(emer);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

	private List<BuscarContato> executeQuery(String id, String sql) {
		return jdbcTemplate.query(sql, new Object[] { id }, (rs, rowNum) -> {
			BuscarContato contato = new BuscarContato();
			BuscarTelefone telefone = new BuscarTelefone();
			String idEmergencia = rs.getString("id_emergencia");
			if (id.equals(idEmergencia)) {
				contato.setId(new ContatoId(rs.getString("id")));
				contato.setNome(rs.getString("nome"));
				telefone.setId(new TelefoneId(rs.getString("id_telefone")));
				telefone.setDdd(rs.getInt("ddd"));
				telefone.setNumero(rs.getInt("numero"));
				contato.setTelefone(telefone);
			}
			return contato;
		});
	}
}