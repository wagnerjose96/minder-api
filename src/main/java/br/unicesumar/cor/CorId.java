package br.unicesumar.cor;

import javax.persistence.Embeddable;

import br.unicesumar.BaseId;

@Embeddable
public class CorId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;
	
	public CorId() {
		super();
	}
	public CorId(String value) {
		super(value);
	}

}
