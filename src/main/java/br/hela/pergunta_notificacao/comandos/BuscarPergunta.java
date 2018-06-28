package br.hela.pergunta_notificacao.comandos;

import br.hela.pergunta_notificacao.Pergunta;
import br.hela.pergunta_notificacao.PerguntaId;
import lombok.Data;

@Data
public class BuscarPergunta {
	private PerguntaId idPergunta;
	private String descricao;
	
	public BuscarPergunta(Pergunta comando) {
		this.idPergunta = comando.getIdPergunta();
		this.descricao = comando.getDescricao();
	}
	
}
