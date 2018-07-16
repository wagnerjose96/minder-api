package br.minder.pergunta_notificacao.resposta.comandos;

import br.minder.pergunta_notificacao.PerguntaId;
import lombok.Data;

@Data
public class CriarResposta {
	private String descricao;
	private PerguntaId idPergunta;
}
