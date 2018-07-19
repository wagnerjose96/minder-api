package br.minder.alarme.comandos;

import br.minder.alarme.Alarme;
import br.minder.alarme.AlarmeId;
import br.minder.conversor.ConverterData;
import br.minder.medicamento.comandos.BuscarMedicamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
