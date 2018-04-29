package br.hela.contatoEmergencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.hela.contatoEmergencia.comandos.CriarContatoEmergencia;
import br.hela.contatoEmergencia.contato_emergencia_telefone.Contato_emergencia_telefone;
import br.hela.contatoEmergencia.contato_emergencia_telefone.Contato_emergencia_telefone_Service;
import br.hela.telefone.Telefone;
import br.hela.telefone.TelefoneId;
import br.hela.telefone.TelefoneService;
import br.hela.telefone.comandos.CriarTelefone;

@Service
@Transactional
public class ContatoEmergenciaService {

	@Autowired
	private ContatoEmergenciaRepository repo;

	@Autowired
	private Contato_emergencia_telefone_Service service;

	@Autowired
	private TelefoneService telefoneService;

	public Optional<ContatoEmergenciaId> salvar(CriarContatoEmergencia comando, Optional<TelefoneId> idTelefone) {
		ContatoEmergencia contato = repo.save(new ContatoEmergencia(comando));
		return Optional.of(contato.getIdContatoEmergencia());
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
