package br.hela.pergunta_notificacao.comandos;

import java.util.ArrayList;
import java.util.List;

import br.hela.pergunta_notificacao.Pergunta;
import br.hela.pergunta_notificacao.PerguntaId;
import br.hela.pergunta_notificacao.resposta.comandos.BuscarResposta;
import lombok.Data;

@Data
public class BuscarPergunta {
	private PerguntaId idPergunta;
	private String descricao;
	private List<BuscarResposta> respostas = new ArrayList<>();

	public BuscarPergunta(Pergunta comando) {
		this.idPergunta = comando.getIdPergunta();
		this.descricao = comando.getDescricao();
	}

}
