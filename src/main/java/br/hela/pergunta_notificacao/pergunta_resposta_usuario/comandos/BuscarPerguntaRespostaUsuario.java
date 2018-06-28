package br.hela.pergunta_notificacao.pergunta_resposta_usuario.comandos;

import br.hela.pergunta_notificacao.comandos.BuscarPergunta;
import br.hela.pergunta_notificacao.pergunta_resposta_usuario.PerguntaRespostaUsuario;
import br.hela.pergunta_notificacao.pergunta_resposta_usuario.PerguntaRespostaUsuarioId;
import br.hela.pergunta_notificacao.resposta.comandos.BuscarResposta;
import lombok.Data;

@Data
public class BuscarPerguntaRespostaUsuario {
	private PerguntaRespostaUsuarioId id;
	private BuscarPergunta pergunta;
	private BuscarResposta resposta;
	
	public BuscarPerguntaRespostaUsuario(PerguntaRespostaUsuario comando) {
		this.id = comando.getId();
	}
}
