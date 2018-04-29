package br.hela.planoDeSaude.comandos;

import br.hela.convenio.Convenio;
import br.hela.convenio.ConvenioId;
import br.hela.planoDeSaude.PlanoDeSaude;
import br.hela.planoDeSaude.PlanoDeSaudeId;

public class BuscarPlanoDeSaude {
	private PlanoDeSaudeId id;
	private Convenio convenio;
	private int numeroCartao;
	private String habitacao;
	private String territorio;
	
	public BuscarPlanoDeSaude(PlanoDeSaude comandos) {
		this.id = comandos.getId();
		this.numeroCartao = comandos.getNumeroCartao();
		this.habitacao = comandos.getHabitacao();
		this.territorio = comandos.getTerritorio();
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

	public String getHabitacao() {
		return habitacao;
	}

	public void setHabitacao(String habitacao) {
		this.habitacao = habitacao;
	}

	public String getTerritorio() {
		return territorio;
	}

	public void setTerritorio(String territorio) {
		this.territorio = territorio;
	}

}

