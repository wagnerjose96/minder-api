package br.hela.planoDeSaude.comandos;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;

import br.hela.convenio.ConvenioId;

public class CriarPlanoDeSaude {
	@AttributeOverride(name = "value", column = @Column(name = "id_convenio"))
	private ConvenioId idConvenio;
	private int numeroCartao;
	private String habitacao;
	private String territorio;

	public CriarPlanoDeSaude() {
	}

	public ConvenioId getIdConvenio() {
		return idConvenio;
	}

	public void setIdConvenio(ConvenioId idConvenio) {
		this.idConvenio = idConvenio;
	}

	public int getNumeroCartao() {
		return numeroCartao;
	}

	public void setNumeroCartao(int numeroCartao) {
		this.numeroCartao = numeroCartao;
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
