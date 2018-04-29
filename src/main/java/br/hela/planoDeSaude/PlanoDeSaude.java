package br.hela.planoDeSaude;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.planoDeSaude.comandos.CriarPlanoDeSaude;
import br.hela.planoDeSaude.comandos.EditarPlanoDeSaude;

@Entity
@Audited
public class PlanoDeSaude {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private PlanoDeSaudeId id;
	private int numeroCartao;

	public PlanoDeSaude() {

	}

	public PlanoDeSaude(CriarPlanoDeSaude comando) {
		this.id = new PlanoDeSaudeId();
		this.numeroCartao = comando.getNumeroCartao();
	}

	public void apply(EditarPlanoDeSaude comando) {
		this.id = comando.getId();
		this.numeroCartao = comando.getNumeroCartao();
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

}
