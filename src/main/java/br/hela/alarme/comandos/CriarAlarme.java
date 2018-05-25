package br.hela.alarme.comandos;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import br.hela.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class CriarAlarme {
	private Date dataInicio;
	private Date dataFim;
	private String quantidade;
	private String descricao;
	private int periodicidade;
	@AttributeOverride(name = "value", column = @Column(name = "id_medicamento"))
	private MedicamentoId idMedicamento;

}
