package br.hela.pergunta_notificacao.resposta.comandos;

import br.hela.pergunta_notificacao.resposta.RespostaId;
import lombok.Data;

@Data
public class EditarResposta {
	private RespostaId idResposta;
	private String descricao;
}
