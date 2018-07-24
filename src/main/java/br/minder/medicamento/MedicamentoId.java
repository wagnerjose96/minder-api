package br.minder.medicamento;

import javax.persistence.Embeddable;

import br.minder.base_id.BaseId;

@Embeddable
public class MedicamentoId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public MedicamentoId() {
		super();
	}

	public MedicamentoId(String value) {
		super(value);
	}

}
