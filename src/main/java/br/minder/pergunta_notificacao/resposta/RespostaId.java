package br.minder.pergunta_notificacao.resposta;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

@Embeddable
public class RespostaId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public RespostaId() {
		super();
	}

	public RespostaId(String value) {
		super(value);
	}

}
