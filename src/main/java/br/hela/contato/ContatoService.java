package br.hela.contato;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.hela.contato.Contato;
import br.hela.contato.ContatoId;
import br.hela.contato.comandos.CriarContato;
import br.hela.contato.comandos.EditarContato;

@Service
@Transactional
public class ContatoService {
	@Autowired
	private ContatoRepository repo;


	public Optional<ContatoId> salvar(CriarContato comando) {
		Contato novo = repo.save(new Contato(comando));
		return Optional.of(novo.getId());
	}

	public Optional<Contato> encontrar(ContatoId contatoId) throws Exception {
		Contato contato = repo.findById(contatoId).get();
		return Optional.of(contato);
	}

	public Optional<List<Contato>> encontrar() throws Exception {
		List<Contato> contatos = repo.findAll();
		return Optional.of(contatos);
	}

	public Optional<ContatoId> alterar(EditarContato comando) {
		Optional<Contato> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			Contato contato = optional.get();
			contato.apply(comando);
			repo.save(contato);
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
}
