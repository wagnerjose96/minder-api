package br.minder.alarme.comandos;

import java.util.Date;

import br.minder.medicamento.MedicamentoId;
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
