package br.hela.sexo;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class SexoId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public SexoId() {
		super();
	}

	public SexoId(String value) {
		super(value);
	}

}