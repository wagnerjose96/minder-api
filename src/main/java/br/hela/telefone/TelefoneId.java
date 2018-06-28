package br.hela.telefone;

import javax.persistence.Embeddable;

import br.hela.base_id.BaseId;

@Embeddable
public class TelefoneId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public TelefoneId() {
		super();
	}

	public TelefoneId(String value) {
		super(value);
	}

}
