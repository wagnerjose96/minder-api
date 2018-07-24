package br.minder.pergunta_notificacao.comandos;

import br.minder.pergunta_notificacao.PerguntaId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarPergunta {
	private PerguntaId idPergunta;
	private String descricao;
}
