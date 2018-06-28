package br.hela.pergunta_notificacao.resposta.comandos;

import br.hela.pergunta_notificacao.resposta.Resposta;
import br.hela.pergunta_notificacao.resposta.RespostaId;
import lombok.Data;

@Data
public class BuscarResposta {
	private RespostaId idResposta;
	private String descricao;
	
	public BuscarResposta(Resposta comando) {
		this.idResposta = comando.getIdResposta();
		this.descricao = comando.getDescricao();
	}
	
}
