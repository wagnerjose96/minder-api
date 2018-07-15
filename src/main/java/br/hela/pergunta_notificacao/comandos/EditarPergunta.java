package br.hela.pergunta_notificacao.comandos;

import br.hela.pergunta_notificacao.PerguntaId;
import lombok.Data;

@Data
public class EditarPergunta {
	private PerguntaId idPergunta;
	private String descricao;
}
