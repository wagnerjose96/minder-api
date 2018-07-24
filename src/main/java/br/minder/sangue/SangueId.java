package br.minder.sangue;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

@Embeddable
public class SangueId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public SangueId() {
		super();
	}

	public SangueId(String value) {
		super(value);
	}

}
