package br.minder.emergencia;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

@Embeddable
public class EmergenciaId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public EmergenciaId() {
		super();
	}

	public EmergenciaId(String value) {
		super(value);
	}

}
