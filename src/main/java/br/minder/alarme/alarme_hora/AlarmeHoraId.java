package br.minder.alarme.alarme_hora;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

@Embeddable
public class AlarmeHoraId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public AlarmeHoraId() {
		super();
	}

	public AlarmeHoraId(String value) {
		super(value);
	}
}
