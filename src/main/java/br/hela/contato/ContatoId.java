package br.hela.contato;

import javax.persistence.Embeddable;

import br.hela.base_id.BaseId;

@Embeddable
public class ContatoId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public ContatoId() {
		super();
	}

	public ContatoId(String value) {
		super(value);
	}
}
