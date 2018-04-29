package br.hela.planoDeSaude;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;

import br.hela.convenio.ConvenioId;
import br.hela.planoDeSaude.comandos.CriarPlanoDeSaude;
import br.hela.planoDeSaude.comandos.EditarPlanoDeSaude;

@Entity
@Audited
public class PlanoDeSaude {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private PlanoDeSaudeId id;
	@AttributeOverride(name = "value", column = @Column(name = "id_convenio"))
	private ConvenioId idConvenio;
	private int numeroCartao;
	private String habitacao;
	private String territorio;
	
	public PlanoDeSaude() {
		
	}
	
	public PlanoDeSaude(CriarPlanoDeSaude comando) {
		this.id = new PlanoDeSaudeId();
		this.idConvenio = comando.getIdConvenio();
		this.numeroCartao = comando.getNumeroCartao();
		this.habitacao = comando.getHabitacao();
		this.territorio = comando.getTerritorio();
	}

	public void apply(EditarPlanoDeSaude comando) {
		this.id = comando.getId();
		this.idConvenio = comando.getIdConvenio();
		this.numeroCartao = comando.getNumeroCartao();
		this.habitacao = comando.getHabitacao();
		this.territorio = comando.getTerritorio();
	}
	
	
	public int getNumeroCartao() {
		return numeroCartao;
	}
	
	public void setNumeroCartao(int numeroCartao) {
		this.numeroCartao = numeroCartao;
	}
	
	public PlanoDeSaudeId getId() {
		return id;
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

	public ConvenioId getIdConvenio() {
		return idConvenio;
	}

	public void setIdConvenio(ConvenioId idConvenio) {
		this.idConvenio = idConvenio;
	}

}
