package br.hela.planoDeSaude.planoDeSaude_convenio;

import javax.persistence.Embeddable;

import br.hela.BaseId;

@Embeddable
public class PlanoDeSaude_Convenio_Id extends BaseId{
	private static final long serialVersionUID = 8965550305250511524L;

	public PlanoDeSaude_Convenio_Id() {
		super();
	}

	public PlanoDeSaude_Convenio_Id(String value) {
		super(value);
	}
}
