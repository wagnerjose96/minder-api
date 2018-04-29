package br.hela.planoDeSaude.comandos;

import br.hela.convenio.ConvenioId;

public class CriarPlanoDeSaude {
	private int numeroCartao;
	private ConvenioId convenioId;

	public CriarPlanoDeSaude() {
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
