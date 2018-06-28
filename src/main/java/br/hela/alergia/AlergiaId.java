package br.hela.alergia;

import javax.persistence.Embeddable;

import br.hela.base_id.BaseId;

@Embeddable
public class AlergiaId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public AlergiaId() {
		super();
	}

	public AlergiaId(String value) {
		super(value);
	}
}
