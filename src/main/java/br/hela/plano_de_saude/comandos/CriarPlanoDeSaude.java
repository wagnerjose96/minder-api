package br.hela.plano_de_saude.comandos;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import br.hela.convenio.ConvenioId;
import lombok.Data;

@Data
public class CriarPlanoDeSaude {
	@AttributeOverride(name = "value", column = @Column(name = "id_convenio"))
	private ConvenioId idConvenio;
	private int numeroCartao;
	private String habitacao;
	private String territorio;
}
