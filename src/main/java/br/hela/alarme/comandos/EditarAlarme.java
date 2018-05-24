package br.hela.alarme.comandos;

import java.util.Date;

import br.hela.alarme.AlarmeId;
import br.hela.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class EditarAlarme {
	private AlarmeId id;
	private Date dataInicio;
	private Date dataFim;
	private String quantidade;
	private String descricao;
	private int periodicidade;
	private MedicamentoId idMedicamento;

}
