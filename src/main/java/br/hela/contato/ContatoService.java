package br.hela.contato;

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

import br.hela.contato.Contato;
import br.hela.contato.ContatoId;
import br.hela.contato.comandos.BuscarContato;
import br.hela.contato.comandos.CriarContato;
import br.hela.contato.comandos.EditarContato;
import br.hela.contato.contato_telefone.Contato_Telefone;
import br.hela.contato.contato_telefone.Contato_Telefone_Service;
import br.hela.telefone.Telefone;
import br.hela.telefone.TelefoneId;
import br.hela.telefone.TelefoneService;

@Service
@Transactional
public class ContatoService {

	@Autowired
	private ContatoRepository repo;

	@Autowired
	private Contato_Telefone_Service service;
	
	@Autowired
	private TelefoneService telefoneService;

	public Optional<ContatoId> salvar(CriarContato comando) {
		Contato novo = repo.save(new Contato(comando));
		Optional<TelefoneId> idTelefone = telefoneService.salvar(comando.getTelefone());
		if (verificaTelefoneExistente(idTelefone.get()) && novo.getId() != null) {
			Contato_Telefone contatoTelefone = new Contato_Telefone();
			contatoTelefone.setIdContato(novo.getId());
			contatoTelefone.setIdTelefone(idTelefone.get());
			service.salvar(contatoTelefone);
		}
		return Optional.of(novo.getId());
	}

	public Optional<BuscarContato> encontrar(ContatoId contatoId) throws Exception {
		ResultSet rs = executeQuery(contatoId.toString());		
		BuscarContato contato = new BuscarContato(repo.findById(contatoId).get());
		String id = contatoId.toString();
		contato.setTelefone(telefone(rs, id));
		return Optional.of(contato);
	}

	public Optional<List<BuscarContato>> encontrar() throws Exception {
		List<Contato> contatos = repo.findAll();
		List<BuscarContato> rsContatos = new ArrayList<>();
		for (Contato contato : contatos) {
			ResultSet rs = executeQuery(contato.getId().toString());
			BuscarContato novo = new BuscarContato(contato);
			novo.setTelefone(telefone(rs, contato.getId().toString()));
			rsContatos.add(novo);
		}
		return Optional.of(rsContatos);
	}

	public Optional<ContatoId> alterar(EditarContato comando) {
		Optional<Contato> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			Contato contato = optional.get();
			contato.apply(comando);
			repo.save(contato);
			if (verificaTelefoneExistente(comando.getTelefone().getIdTelefone())) {
				telefoneService.alterar(comando.getTelefone());
			}
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}
	
	public Optional<String> deletar(ContatoId id){
		if(repo.findById(id).isPresent()) {
			repo.deleteById(id);
			return Optional.of("Contato" + id + " deletado com sucesso");
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

	private Telefone telefone(ResultSet rs, String id) throws Exception {
			Telefone telefone = new Telefone();;			
			while (rs.next()) {
			String idContato = rs.getString("id");
			if (id.equals(idContato)) {
				telefone.setIdTelefone(new TelefoneId(rs.getString("id_telefone")));
				int ddd = Integer.parseInt(rs.getString("ddd"));
				telefone.setDdd(ddd);
				int numero = Integer.parseInt(rs.getString("numero"));
				telefone.setNumero(numero);
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
		String query = "select c.id, a.id_telefone, a.ddd, a.numero from telefone a "
				+ "inner join contato_telefone b on a.id_telefone = b.id_telefone "
				+ "inner join contato c on b.id_contato = c.id " 
				+ "group by c.id, a.id_telefone having c.id = '"
				+ id + "' " + "order by c.nome";
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}
	
}
