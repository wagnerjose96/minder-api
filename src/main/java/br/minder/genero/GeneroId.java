package br.minder.genero;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

@Embeddable
public class GeneroId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public GeneroId() {
		super();
	}

	public GeneroId(String value) {
		super(value);
	}

}
