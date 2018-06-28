package br.hela.pergunta_notificacao.pergunta_resposta_usuario.comandos;

import br.hela.pergunta_notificacao.PerguntaId;
import br.hela.pergunta_notificacao.resposta.RespostaId;
import lombok.Data;

@Data
public class CriarPerguntaRespostaUsuario {
	private PerguntaId idPergunta;
	private RespostaId idResposta;
}
