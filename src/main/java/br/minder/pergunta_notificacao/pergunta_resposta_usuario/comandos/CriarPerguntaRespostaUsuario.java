package br.minder.pergunta_notificacao.pergunta_resposta_usuario.comandos;

import br.minder.pergunta_notificacao.PerguntaId;
import br.minder.pergunta_notificacao.resposta.RespostaId;
import lombok.Data;

@Data
public class CriarPerguntaRespostaUsuario {
	private PerguntaId idPergunta;
	private RespostaId idResposta;
}
