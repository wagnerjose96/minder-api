package br.hela.cirurgia;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class CirurgiaId extends BaseId {

	private static final long serialVersionUID = 8965550305250511524L;

	public CirurgiaId() {
		super();
	}

	public CirurgiaId(String value) {
		super(value);
	}

}
