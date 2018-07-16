package br.minder.convenio;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

@Embeddable
public class ConvenioId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public ConvenioId() {
		super();
	}

	public ConvenioId(String value) {
		super(value);
	}
}
