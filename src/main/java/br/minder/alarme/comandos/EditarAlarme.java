package br.minder.alarme.comandos;

import java.sql.Date;

import br.minder.alarme.AlarmeId;
import br.minder.medicamento.MedicamentoId;
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
