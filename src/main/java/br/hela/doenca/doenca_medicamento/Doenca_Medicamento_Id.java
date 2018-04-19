package br.hela.doenca.doenca_medicamento;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class Doenca_Medicamento_Id extends BaseId {
	private static final long serialVersionUID = 1L;

	public Doenca_Medicamento_Id() {
		super();
	}

	public Doenca_Medicamento_Id(String value) {
		super(value);
	}
}
