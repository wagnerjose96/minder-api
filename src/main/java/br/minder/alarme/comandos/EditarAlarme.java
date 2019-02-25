package br.minder.alarme.comandos;

import java.sql.Date;
import java.sql.Time;

import br.minder.alarme.AlarmeId;
import br.minder.medicamento.MedicamentoId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarAlarme {
	private AlarmeId id;
	private Date dataInicio;
	private Date dataFim;
	private String quantidade;
	private String descricao;
	private int periodicidade;
	private MedicamentoId idMedicamento;
	private Time horaPrimeiraDose;
	private Time horaUltimaDose;

}
