package br.minder.pergunta_notificacao;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

@Embeddable
public class PerguntaId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public PerguntaId() {
		super();
	}

	public PerguntaId(String value) {
		super(value);
	}

}
