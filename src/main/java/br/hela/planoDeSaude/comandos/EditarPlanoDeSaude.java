package br.hela.planoDeSaude.comandos;

import br.hela.convenio.ConvenioId;
import br.hela.planoDeSaude.PlanoDeSaudeId;

public class EditarPlanoDeSaude {
	private PlanoDeSaudeId id;
	private int numeroCartao;
	private ConvenioId convenioId;
	
	public EditarPlanoDeSaude() {
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
	
	public ConvenioId getConvenioId() {
		return convenioId;
	}

	public void setConvenioId(ConvenioId convenioId) {
		this.convenioId = convenioId;
	}

}
