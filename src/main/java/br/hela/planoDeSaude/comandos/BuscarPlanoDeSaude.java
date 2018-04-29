package br.hela.planoDeSaude.comandos;

import br.hela.convenio.Convenio;
import br.hela.planoDeSaude.PlanoDeSaude;
import br.hela.planoDeSaude.PlanoDeSaudeId;

public class BuscarPlanoDeSaude {
	private PlanoDeSaudeId id;
	private int numeroCartao;
	private Convenio convenio;

	public BuscarPlanoDeSaude() {
	}
	
	public BuscarPlanoDeSaude(PlanoDeSaude comandos) {
		this.id = comandos.getId();
		this.numeroCartao = comandos.getNumeroCartao();
	}

	public PlanoDeSaudeId getId() {
		return id;
	}

	public void setId(PlanoDeSaudeId id) {
		this.id = id;
	}

	public int getNumeroCartao() {
		return numeroCartao;
	}

	public void setNumeroCartao(int numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

}
