package br.hela.plano_de_saude;

import javax.persistence.Embeddable;

import br.hela.base_id.BaseId;

@Embeddable
public class PlanoDeSaudeId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;

	public PlanoDeSaudeId() {
		super();
	}

	public PlanoDeSaudeId(String value) {
		super(value);
	}
}