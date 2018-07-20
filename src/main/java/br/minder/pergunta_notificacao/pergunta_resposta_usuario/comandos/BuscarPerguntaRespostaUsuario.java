package br.minder.pergunta_notificacao.pergunta_resposta_usuario.comandos;

import br.minder.pergunta_notificacao.comandos.BuscarPergunta;
import br.minder.pergunta_notificacao.pergunta_resposta_usuario.PerguntaRespostaUsuario;
import br.minder.pergunta_notificacao.pergunta_resposta_usuario.PerguntaRespostaUsuarioId;
import br.minder.pergunta_notificacao.resposta.comandos.BuscarResposta;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuscarPerguntaRespostaUsuario {
	private PerguntaRespostaUsuarioId id;
	private BuscarPergunta pergunta;
	private BuscarResposta resposta;
	
	public BuscarPerguntaRespostaUsuario(PerguntaRespostaUsuario comando) {
		this.id = comando.getId();
	}
}
