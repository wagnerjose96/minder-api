package br.hela.alarme.comandos;

import java.text.SimpleDateFormat;
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
		Long dataLongInicio = comandos.getDataInicio().getTime(); // pega os milessegundos;
		this.dataInicio = converterData(dataLongInicio);
		Long dataLongFim = comandos.getDataFim().getTime(); // pega os milessegundos;
		this.dataFim = converterData(dataLongFim);
		this.quantidade = comandos.getQuantidade();
		this.descricao = comandos.getDescricao();
		this.periodicidade = comandos.getPeriodicidade();
	}

	private String converterData(Long data) {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		String dataFormatada = formato.format(data);
		return dataFormatada;
	}

	public BuscarAlarme() {
	}

}
