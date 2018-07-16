package br.minder.pergunta_notificacao.pergunta_resposta_usuario;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

@Embeddable
public class PerguntaRespostaUsuarioId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public PerguntaRespostaUsuarioId() {
		super();
	}

	public PerguntaRespostaUsuarioId(String value) {
		super(value);
	}
}
