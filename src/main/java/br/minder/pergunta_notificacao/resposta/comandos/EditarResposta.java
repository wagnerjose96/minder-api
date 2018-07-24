package br.minder.pergunta_notificacao.resposta.comandos;

import br.minder.pergunta_notificacao.resposta.RespostaId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarResposta {
	private RespostaId idResposta;
	private String descricao;
}
