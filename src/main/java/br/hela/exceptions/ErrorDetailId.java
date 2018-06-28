package br.hela.exceptions;

import javax.persistence.Embeddable;

import br.hela.base_id.BaseId;

@Embeddable
public class ErrorDetailId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public ErrorDetailId() {
		super();
	}

	public ErrorDetailId(String value) {
		super(value);
	}
}
