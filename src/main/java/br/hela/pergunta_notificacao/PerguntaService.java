package br.hela.pergunta_notificacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.pergunta_notificacao.comandos.BuscarPergunta;
import br.hela.pergunta_notificacao.comandos.CriarPergunta;
import br.hela.pergunta_notificacao.comandos.EditarPergunta;

@Service
@Transactional
public class PerguntaService {
	@Autowired
	private PerguntaRepository repo;

	public Optional<PerguntaId> salvar(CriarPergunta comando) {
		Pergunta novo = repo.save(new Pergunta(comando));
		return Optional.of(novo.getIdPergunta());
	}

	public Optional<BuscarPergunta> encontrar(PerguntaId id) {
		Optional<Pergunta> pergunta = repo.findById(id);
		if (pergunta.isPresent()) {
			BuscarPergunta resultado = new BuscarPergunta(pergunta.get());
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<List<BuscarPergunta>> encontrar() {
		List<BuscarPergunta> resultados = new ArrayList<>();
		List<Pergunta> perguntas = repo.findAll();
		for (Pergunta pergunta : perguntas) {
			BuscarPergunta med = new BuscarPergunta(pergunta);
			resultados.add(med);
		}
		return Optional.of(resultados);
	}

	public Optional<PerguntaId> alterar(EditarPergunta comando) {
		Optional<Pergunta> optional = repo.findById(comando.getIdPergunta());
		if (optional.isPresent()) {
			Pergunta pergunta = optional.get();
			pergunta.apply(comando);
			repo.save(pergunta);
			return Optional.of(comando.getIdPergunta());
		}
		return Optional.empty();
	}
}