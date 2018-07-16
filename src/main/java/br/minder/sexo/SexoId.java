package br.minder.sexo;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

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
