package br.hela.planoDeSaude.comandos;

import br.hela.convenio.comandos.BuscarConvenio;
import br.hela.planoDeSaude.PlanoDeSaude;
import br.hela.planoDeSaude.PlanoDeSaudeId;
import lombok.Data;

@Data
public class BuscarPlanoDeSaude {
	private PlanoDeSaudeId id;
	private BuscarConvenio convenio;
	private int numeroCartao;
	private String habitacao;
	private String territorio;

	public BuscarPlanoDeSaude(PlanoDeSaude comandos) {
		this.id = comandos.getId();
		this.numeroCartao = comandos.getNumeroCartao();
		this.habitacao = comandos.getHabitacao();
		this.territorio = comandos.getTerritorio();
	}
}
