package br.hela.contato;

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

@Service
@Transactional
public class ContatoService {
	@Autowired
	private ContatoRepository repo;


	public Optional<ContatoId> salvar(CriarContato comando) {
		Contato novo = repo.save(new Contato(comando));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarContato> encontrar(ContatoId contatoId) throws Exception {
		Contato contato = repo.findById(contatoId).get();
		BuscarContato resultado = new BuscarContato(contato);
		return Optional.of(resultado);
	}

	public Optional<List<BuscarContato>> encontrar() throws Exception {
		List<Contato> contatos = repo.findAll();
		List<BuscarContato> resultados = new ArrayList<>();
		for (Contato contato : contatos) {
			BuscarContato nova = new BuscarContato(contato);
			resultados.add(nova);
		}
		return Optional.of(resultados);
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
