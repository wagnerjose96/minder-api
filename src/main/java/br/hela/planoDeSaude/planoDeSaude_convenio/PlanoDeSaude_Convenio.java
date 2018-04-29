package br.hela.planoDeSaude.planoDeSaude_convenio;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.hela.convenio.ConvenioId;
import br.hela.planoDeSaude.PlanoDeSaudeId;

@Entity
public class PlanoDeSaude_Convenio {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private PlanoDeSaude_Convenio_Id id;
	@AttributeOverride(name = "value", column = @Column(name = "id_convenio"))
	private ConvenioId idConvenio;
	@AttributeOverride(name = "value", column = @Column(name = "id_plano_de_saude"))
	private PlanoDeSaudeId idPlanoDeSaude;

	public PlanoDeSaude_Convenio() {
		this.id = new PlanoDeSaude_Convenio_Id();
	}

	public ConvenioId getIdConvenio() {
		return idConvenio;
	}

	public void setIdConvenio(ConvenioId idConvenio) {
		this.idConvenio = idConvenio;
	}

	public PlanoDeSaudeId getIdPlanoDeSaude() {
		return idPlanoDeSaude;
	}

	public void setIdPlanoDeSaude(PlanoDeSaudeId idPlanoDeSaude) {
		this.idPlanoDeSaude = idPlanoDeSaude;
	}

	public PlanoDeSaude_Convenio_Id getId() {
		return id;
	}
}
