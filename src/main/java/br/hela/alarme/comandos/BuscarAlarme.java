package br.hela.alarme.comandos;

import java.util.Date;

import br.hela.alarme.Alarme;
import br.hela.alarme.AlarmeId;
import br.hela.medicamento.Medicamento;
import lombok.Data;

@Data
public class BuscarAlarme {
	private AlarmeId id;
	private Date dataInicio;
	private Date dataFim;
	private String quantidade;
	private String descricao;
	private int periodicidade;
	private Medicamento medicamento;

	public BuscarAlarme(Alarme comandos) {
		this.id = comandos.getId();
		this.dataInicio = comandos.getDataInicio();
		this.dataFim = comandos.getDataFim();
		this.quantidade = comandos.getQuantidade();
		this.descricao = comandos.getDescricao();
		this.periodicidade = comandos.getPeriodicidade();
	}

}
