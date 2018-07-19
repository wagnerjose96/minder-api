package br.minder.plano_de_saude.comandos;

import java.math.BigInteger;

import br.minder.convenio.comandos.BuscarConvenio;
import br.minder.plano_de_saude.PlanoDeSaude;
import br.minder.plano_de_saude.PlanoDeSaudeId;
import lombok.Data;

@Data
public class BuscarPlanoDeSaude {
	private PlanoDeSaudeId id;
	private BuscarConvenio convenio;
	private BigInteger numeroCartao;
	private String habitacao;
	private String territorio;

	public BuscarPlanoDeSaude(PlanoDeSaude comandos) {
		this.id = comandos.getId();
		this.numeroCartao = comandos.getNumeroCartao();
		this.habitacao = comandos.getHabitacao();
		this.territorio = comandos.getTerritorio();
	}
}
