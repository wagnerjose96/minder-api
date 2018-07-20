package br.minder.pergunta_notificacao.comandos;

import java.util.ArrayList;
import java.util.List;
import br.minder.pergunta_notificacao.Pergunta;
import br.minder.pergunta_notificacao.PerguntaId;
import br.minder.pergunta_notificacao.resposta.comandos.BuscarResposta;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuscarPergunta {
	private PerguntaId idPergunta;
	private String descricao;
	private List<BuscarResposta> respostas = new ArrayList<>();

	public BuscarPergunta(Pergunta comando) {
		this.idPergunta = comando.getIdPergunta();
		this.descricao = comando.getDescricao();
	}

}
