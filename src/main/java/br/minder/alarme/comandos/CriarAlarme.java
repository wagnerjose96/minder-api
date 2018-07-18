package br.minder.alarme.comandos;

import java.sql.Date;

import br.minder.medicamento.MedicamentoId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarAlarme {
	private Date dataInicio;
	private Date dataFim;
	private String quantidade;
	private String descricao;
	private int periodicidade;
	private MedicamentoId idMedicamento;
}
