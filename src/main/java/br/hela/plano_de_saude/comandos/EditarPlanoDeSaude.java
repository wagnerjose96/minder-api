package br.hela.planoDeSaude.comandos;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import br.hela.convenio.ConvenioId;
import br.hela.planoDeSaude.PlanoDeSaudeId;
import lombok.Data;

@Data
public class EditarPlanoDeSaude {
	private PlanoDeSaudeId id;
	@AttributeOverride(name = "value", column = @Column(name = "id_convenio"))
	private ConvenioId idConvenio;
	private int numeroCartao;
	private String habitacao;
	private String territorio;
}
