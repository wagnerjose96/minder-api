package br.minder.alarme;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

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
