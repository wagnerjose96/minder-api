package br.minder.pergunta_notificacao.resposta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.minder.pergunta_notificacao.PerguntaId;
import br.minder.pergunta_notificacao.resposta.comandos.BuscarResposta;
import br.minder.pergunta_notificacao.resposta.comandos.CriarResposta;
import br.minder.pergunta_notificacao.resposta.comandos.EditarResposta;

@Service
@Transactional
public class RespostaService {
	@Autowired
	private RespostaRepository repo;

	public Optional<RespostaId> salvar(CriarResposta comando) {
		if (comando.getDescricao() != null && comando.getIdPergunta() != null
				&& contarRespostas(comando.getIdPergunta())) {
			Resposta novo = repo.save(new Resposta(comando));
			return Optional.of(novo.getIdResposta());
		}
		return Optional.empty();
	}

	public Optional<BuscarResposta> encontrar(RespostaId id) {
		Optional<Resposta> resposta = repo.findById(id);
		if (resposta.isPresent()) {
			BuscarResposta resultado = new BuscarResposta(resposta.get());
			return Optional.of(resultado);
		}
		return Optional.empty();
	}

	public Optional<List<BuscarResposta>> encontrar() {
		List<BuscarResposta> resultados = new ArrayList<>();
		List<Resposta> respostas = repo.findAll();
		if (!respostas.isEmpty()) {
			for (Resposta resposta : respostas) {
				BuscarResposta res = new BuscarResposta(resposta);
				resultados.add(res);
			}
			return Optional.of(resultados);
		}
		return Optional.empty();
	}

	public Optional<RespostaId> alterar(EditarResposta comando) {
		Optional<Resposta> optional = repo.findById(comando.getIdResposta());
		if (comando.getDescricao() != null && optional.isPresent()) {
			Resposta resposta = optional.get();
			resposta.apply(comando);
			repo.save(resposta);
			return Optional.of(comando.getIdResposta());
		}
		return Optional.empty();
	}

	private boolean contarRespostas(PerguntaId idPergunta) {
		List<Resposta> respostas = repo.findAll();
		int contadorRespostas = 0;
		for (Resposta resposta : respostas) {
			if (resposta.getIdPergunta().toString().equals(idPergunta.toString())) {
				contadorRespostas++;
			}
		}
		return (contadorRespostas < 4);
	}
}