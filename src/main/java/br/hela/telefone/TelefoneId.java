package br.hela.telefone;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class TelefoneId extends BaseId {
	private static final long serialVersionUID = 1L;

	public TelefoneId() {
		super();
	}

	public TelefoneId(String value) {
		super(value);
	}

}
