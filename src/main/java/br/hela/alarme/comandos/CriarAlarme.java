package br.hela.alarme.comandos;

import java.util.Date;
import br.hela.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class CriarAlarme {
	private Date dataInicio;
	private Date dataFim;
	private String quantidade;
	private String descricao;
	private int periodicidade;
	private MedicamentoId idMedicamento;
}
