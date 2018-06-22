package br.hela.alarme.comandos;

import br.hela.ConverterData;
import br.hela.alarme.Alarme;
import br.hela.alarme.AlarmeId;
import br.hela.medicamento.comandos.BuscarMedicamento;
import lombok.Data;

@Data
public class BuscarAlarme {
	private AlarmeId id;
	private String dataInicio;
	private String dataFim;
	private String quantidade;
	private String descricao;
	private int periodicidade;
	private BuscarMedicamento medicamento;

	public BuscarAlarme(Alarme comandos) {
		this.id = comandos.getId();
		Long dataLongInicio = comandos.getDataInicio().getTime();
		this.dataInicio = ConverterData.converterData(dataLongInicio);
		Long dataLongFim = comandos.getDataFim().getTime();
		this.dataFim = ConverterData.converterData(dataLongFim);
		this.quantidade = comandos.getQuantidade();
		this.descricao = comandos.getDescricao();
		this.periodicidade = comandos.getPeriodicidade();
	}

	public BuscarAlarme() {
	}

}
