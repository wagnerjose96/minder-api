package br.hela.alarme;

import javax.persistence.Embeddable;

import br.hela.BaseId;

@Embeddable
public class AlarmeId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public AlarmeId() {
		super();
	}

	public AlarmeId(String value) {
		super(value);
	}
}
