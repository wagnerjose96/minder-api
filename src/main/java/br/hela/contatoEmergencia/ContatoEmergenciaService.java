package br.hela.contatoEmergencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.hela.contatoEmergencia.comandos.BuscarContatoEmergencia;
import br.hela.contatoEmergencia.comandos.CriarContatoEmergencia;
import br.hela.contatoEmergencia.comandos.EditarContatoEmergencia;
import br.hela.contatoEmergencia.contato_emergencia_telefone.Contato_emergencia_telefone;
import br.hela.contatoEmergencia.contato_emergencia_telefone.Contato_emergencia_telefone_Service;
import br.hela.telefone.Telefone;
import br.hela.telefone.TelefoneId;
import br.hela.telefone.TelefoneService;

@Service
@Transactional
public class ContatoEmergenciaService {

	@Autowired
	private ContatoEmergenciaRepository repo;

	@Autowired
	private Contato_emergencia_telefone_Service service;

	@Autowired
	private TelefoneService telefoneService;

	public Optional<ContatoEmergenciaId> salvar(CriarContatoEmergencia comando) throws NullPointerException {
		ContatoEmergencia novo = repo.save(new ContatoEmergencia(comando));
		for (TelefoneId id_telefone : comando.getId_telefones()) {
			do {
				if (verificaTelefoneExistente(id_telefone)) {
					Contato_emergencia_telefone contatoEmergenciaTelefone = new Contato_emergencia_telefone();
					contatoEmergenciaTelefone.setIdContatoEmergencia(novo.getIdContatoEmergencia());
					contatoEmergenciaTelefone.setIdTelefone(id_telefone);
					service.salvar(contatoEmergenciaTelefone);
				}
			} while (verificarTelefoneÚnico(id_telefone, comando.getId_telefones()));
		}
		return Optional.of(novo.getIdContatoEmergencia());
	}

	public Optional<BuscarContatoEmergencia> encontrar(ContatoEmergenciaId contatoEmergenciaId) throws Exception {
		ResultSet rs = executeQuery(contatoEmergenciaId.toString());
		BuscarContatoEmergencia contatoEmergencia = new BuscarContatoEmergencia(repo.findById(contatoEmergenciaId).get());
		String id = contatoEmergenciaId.toString();
		contatoEmergencia.setTelefones(telefones(rs, id));
		return Optional.of(contatoEmergencia);
	}

	public Optional<List<BuscarContatoEmergencia>> encontrar() throws Exception {
		List<ContatoEmergencia> contatoEmergencias = repo.findAll();
		List<BuscarContatoEmergencia> rsContatoEmergencia = new ArrayList<>();
		for (ContatoEmergencia contatoEmergencia : contatoEmergencias) {
			ResultSet rs = executeQuery(contatoEmergencia.getIdContatoEmergencia().toString());
			BuscarContatoEmergencia nova = new BuscarContatoEmergencia(contatoEmergencia);
			nova.setTelefones(telefones(rs, contatoEmergencia.getIdContatoEmergencia().toString()));
			rsContatoEmergencia.add(nova);
		}
		return Optional.of(rsContatoEmergencia);
	}

	public Optional<ContatoEmergenciaId> alterar(EditarContatoEmergencia comando) {
		Optional<ContatoEmergencia> optional = repo.findById(comando.getIdContatoEmergencia());
		if (optional.isPresent()) {
			ContatoEmergencia contatoEmergencia = optional.get();
			contatoEmergencia.apply(comando);
			repo.save(contatoEmergencia);
			for (TelefoneId id_telefone : comando.getId_telefones()) {
				if (verificaTelefoneExistente(id_telefone)) {
					Contato_emergencia_telefone contatoEmergenciaTelefone = new Contato_emergencia_telefone();
					contatoEmergenciaTelefone.setIdContatoEmergencia(comando.getIdContatoEmergencia());
					contatoEmergenciaTelefone.setIdTelefone(id_telefone);
					service.salvar(contatoEmergenciaTelefone);
				}
			}
			return Optional.of(comando.getIdContatoEmergencia());
		}
		return Optional.empty();
	}

	private boolean verificaTelefoneExistente(TelefoneId id) {
		if (!telefoneService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

	private List<Telefone> telefones(ResultSet rs, String id) throws Exception {
		List<Telefone> tels = new ArrayList<>();
		while (rs.next()) {
			String idContatoEmergencia = rs.getString("id");
			if (id.equals(idContatoEmergencia)) {
				Telefone tel = new Telefone();
				tel.setTelefoneId(new TelefoneId(rs.getString("id")));
				tel.setDdd(rs.getInt("ddd"));
				tel.setNumero(rs.getInt("numero"));
				tels.add(tel);
			}
		}
		return tels;
	}

	private Statement connect() throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/escoladeti2018", "postgres",
				"11223344");
		Statement stmt = con.createStatement();
		return stmt;
	}

	private boolean verificarTelefoneÚnico(TelefoneId id_telefone, List<TelefoneId> list) {
		for (TelefoneId telefoneId : list) {
			if (telefoneId.equals(id_telefone)) {
				return false;
			}
		}
		return true;
	}
	
	private ResultSet executeQuery(String id) throws Exception {
		Statement stmt = connect();
		String query = "select c.id, a.id,"
				+ "a.numero, a.ddd from telefone a "
				+ "inner join contato_emergencia_telefone b on a.id = b.id "
				+ "inner join contato_emergencia c on b.id = c.id " + "group by c.id, a.id having c.id = '"
				+ id + "' " + "order by c.nome_contato";
		ResultSet rs = stmt.executeQuery(query);
		return rs;
		
	}
	
}
