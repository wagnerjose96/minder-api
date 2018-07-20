package br.minder.pergunta_notificacao.resposta.comandos;

import br.minder.pergunta_notificacao.PerguntaId;
import br.minder.pergunta_notificacao.resposta.Resposta;
import br.minder.pergunta_notificacao.resposta.RespostaId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuscarResposta {
	private RespostaId idResposta;
	private String descricao;
	private PerguntaId idPergunta;

	public BuscarResposta(Resposta comando) {
		this.idResposta = comando.getIdResposta();
		this.descricao = comando.getDescricao();
		this.idPergunta = comando.getIdPergunta();
	}

}
