package br.hela.contato;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
	JdbcTemplate jdbcTemplate;

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
		List<Telefone> telefone = executeQuery(contatoId.toString());
		BuscarContato contato = new BuscarContato(repo.findById(contatoId).get());
		contato.setTelefone(telefone.get(0));
		return Optional.of(contato);
	}

	public Optional<List<BuscarContato>> encontrar() throws Exception {
		List<Contato> contatos = repo.findAll();
		List<BuscarContato> rsContatos = new ArrayList<>();
		for (Contato contato : contatos) {
			List<Telefone> telefone = executeQuery(contato.getId().toString());
			BuscarContato novo = new BuscarContato(contato);
			novo.setTelefone(telefone.get(0));
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

	public Optional<String> deletar(ContatoId id) {
		if (repo.findById(id).isPresent()) {
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

	private List<Telefone> executeQuery(String id) throws Exception {
		List<Telefone> telefones = jdbcTemplate.query("select c.id, a.id_telefone, a.ddd, a.numero from telefone a "
				+ "inner join contato_telefone b on a.id_telefone = b.id_telefone "
				+ "inner join contato c on b.id_contato = c.id " + "group by c.id, a.id_telefone having c.id = ?",
				new Object[] { id }, (rs, rowNum) -> {
					Telefone telefone = new Telefone();
					String idContato = rs.getString("id");
					if (id.equals(idContato)) {
						telefone.setIdTelefone(new TelefoneId(rs.getString("id_telefone")));
						int ddd = Integer.parseInt(rs.getString("ddd"));
						telefone.setDdd(ddd);
						int numero = Integer.parseInt(rs.getString("numero"));
						telefone.setNumero(numero);
					}
					return telefone;
				});
		return telefones;
	}
}
