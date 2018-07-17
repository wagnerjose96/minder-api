package br.minder.cirurgia;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

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
