package br.hela.contato;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class ContatoId extends BaseId {
	private static final long serialVersionUID = 1L;

	public ContatoId() {
		super();
	}

	public ContatoId(String value) {
		super(value);
	}
}
