package br.hela.doenca.doenca_medicamento;

import javax.persistence.Embeddable;

import br.hela.base_id.BaseId;

@Embeddable
public class DoencaMedicamentoId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public DoencaMedicamentoId() {
		super();
	}

	public DoencaMedicamentoId(String value) {
		super(value);
	}
}