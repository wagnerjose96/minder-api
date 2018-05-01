package br.hela.alarme.alarme_medicamento;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class Alarme_Medicamento_Id extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public Alarme_Medicamento_Id() {
		super();
	}

	public Alarme_Medicamento_Id(String value) {
		super(value);
	}
}
