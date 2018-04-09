package br.hela.doenca;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class DoencaId extends BaseId {
private static final long serialVersionUID = 8965550305250511524L;
	
	public DoencaId() {
		super();
	}
	public DoencaId(String value) {
		super(value);
	}

}
