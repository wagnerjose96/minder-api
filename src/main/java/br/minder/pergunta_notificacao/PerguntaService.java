package br.minder.pergunta_notificacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.minder.pergunta_notificacao.comandos.BuscarPergunta;
import br.minder.pergunta_notificacao.comandos.CriarPergunta;
import br.minder.pergunta_notificacao.comandos.EditarPergunta;
import br.minder.pergunta_notificacao.resposta.RespostaService;
import br.minder.pergunta_notificacao.resposta.comandos.BuscarResposta;

@Service
@Transactional
public class PerguntaService {
	@Autowired
	private PerguntaRepository repo;

	@Autowired
	private RespostaService respostaService;

	public Optional<PerguntaId> salvar(CriarPergunta comando) {
		if (comando.getDescricao() != null) {
			Pergunta novo = repo.save(new Pergunta(comando));
			return Optional.of(novo.getIdPergunta());
		}
		return Optional.empty();
	}

	public Optional<BuscarPergunta> encontrar(PerguntaId id) {
		Optional<Pergunta> pergunta = repo.findById(id);
		if (pergunta.isPresent()) {
			BuscarPergunta resultado = new BuscarPergunta(pergunta.get());
			resultado.setRespostas(construir(resultado));
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<BuscarPergunta> encontrar() {
		Optional<Pergunta> pergunta = repo.findAll().stream().findAny();
		if (pergunta.isPresent()) {
			BuscarPergunta resultado = new BuscarPergunta(pergunta.get());
			resultado.setRespostas(construir(resultado));
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<PerguntaId> alterar(EditarPergunta comando) {
		Optional<Pergunta> optional = repo.findById(comando.getIdPergunta());
		if (comando.getDescricao() != null && optional.isPresent()) {
			Pergunta pergunta = optional.get();
			pergunta.apply(comando);
			repo.save(pergunta);
			return Optional.of(comando.getIdPergunta());
		}
		return Optional.empty();
	}

	private List<BuscarResposta> construir(BuscarPergunta resultado) {
		Optional<List<BuscarResposta>> respostas = respostaService.encontrar();
		List<BuscarResposta> listaRespostas = new ArrayList<>();
		if (respostas.isPresent()) {
			for (BuscarResposta resposta : respostas.get()) {
				if (resultado.getIdPergunta().toString().equals(resposta.getIdPergunta().toString()))
					listaRespostas.add(resposta);
			}
		}
		return listaRespostas;
	}
}