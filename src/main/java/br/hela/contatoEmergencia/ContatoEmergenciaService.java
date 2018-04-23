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
import br.hela.telefone.comandos.EditarTelefone;

@Service
@Transactional
public class ContatoEmergenciaService {

	@Autowired
	private ContatoEmergenciaRepository repo;

	@Autowired
	private Contato_emergencia_telefone_Service service;

	@Autowired
	private TelefoneService telefoneService;

	public Optional<ContatoEmergenciaId> salvar(CriarContatoEmergencia comando) {
		ContatoEmergencia novo = repo.save(new ContatoEmergencia(comando));
		Optional<TelefoneId> idTelefone = telefoneService.salvar(comando.getTelefone());
		if (idTelefone.isPresent()) {
			Contato_emergencia_telefone contatoEmergenciaTelefone = new Contato_emergencia_telefone();
			contatoEmergenciaTelefone.setIdContatoEmergencia(novo.getIdContatoEmergencia());
			contatoEmergenciaTelefone.setIdTelefone(idTelefone.get());
			service.salvar(contatoEmergenciaTelefone);
		}

		return Optional.of(novo.getIdContatoEmergencia());
	}

	public Optional<BuscarContatoEmergencia> encontrar(ContatoEmergenciaId contatoEmergenciaId) throws Exception {
		ResultSet rs = executeQuery(contatoEmergenciaId.toString());
		BuscarContatoEmergencia contatoEmergencia = new BuscarContatoEmergencia(
				repo.findById(contatoEmergenciaId).get());
		String id = contatoEmergenciaId.toString();
		contatoEmergencia.setTelefone(telefone(rs, id));
		return Optional.of(contatoEmergencia);
	}

	public Optional<List<BuscarContatoEmergencia>> encontrar() throws Exception {
		List<ContatoEmergencia> contatoEmergencias = repo.findAll();
		List<BuscarContatoEmergencia> rsContatoEmergencia = new ArrayList<>();
		for (ContatoEmergencia contatoEmergencia : contatoEmergencias) {
			ResultSet rs = executeQuery(contatoEmergencia.getIdContatoEmergencia().toString());
			BuscarContatoEmergencia nova = new BuscarContatoEmergencia(contatoEmergencia);
			nova.setTelefone(telefone(rs, contatoEmergencia.getIdContatoEmergencia().toString()));
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
		}
		if (verificaTelefoneExistente(comando.getTelefone().getTelefoneId())) {
			EditarTelefone telefone = new EditarTelefone(comando.getTelefone());
			telefoneService.alterar(telefone);
		}
		return Optional.of(comando.getIdContatoEmergencia());
	}

	private boolean verificaTelefoneExistente(TelefoneId id) {
		if (!telefoneService.encontrar(id).isPresent()) {
			return false;
		} else {
			return true;
		}
	}

	private Telefone telefone(ResultSet rs, String id) throws Exception {
		Telefone telefone = new Telefone();
		while (rs.next()) {
			String idContatoEmergencia = rs.getString("id");
			if (id.equals(idContatoEmergencia)) {
				telefone.setTelefoneId(new TelefoneId(rs.getString("id")));
				telefone.setDdd(rs.getInt("ddd"));
				telefone.setNumero(rs.getInt("numero"));
			}
		}
		return telefone;
	}

	private Statement connect() throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/escoladeti2018", "postgres",
				"11223344");
		Statement stmt = con.createStatement();
		return stmt;
	}

	private ResultSet executeQuery(String id) throws Exception {
		Statement stmt = connect();
		String query = "select c.id, a.id," + "a.numero, a.ddd from telefone a "
				+ "inner join contato_emergencia_telefone b on a.id = b.id "
				+ "inner join contato_emergencia c on b.id = c.id " + "group by c.id, a.id having c.id = '" + id + "' "
				+ "order by c.nome_contato";
		ResultSet rs = stmt.executeQuery(query);
		return rs;

	}

}
