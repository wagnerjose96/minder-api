package br.minder.doenca.comandos;

import java.util.ArrayList;
import java.util.List;

import br.minder.conversor.ConverterData;
import br.minder.doenca.Doenca;
import br.minder.doenca.DoencaId;
import br.minder.medicamento.comandos.BuscarMedicamento;
import lombok.Data;

@Data
public class BuscarDoenca {
	private DoencaId idDoenca;
	private String nomeDoenca;
	private String dataDescoberta;
	private List<BuscarMedicamento> medicamentos = new ArrayList<>();

	public BuscarDoenca(Doenca comandos) {
		this.idDoenca = comandos.getIdDoenca();
		this.nomeDoenca = comandos.getNomeDoenca();
		Long dataLong = comandos.getDataDescoberta().getTime();
		this.dataDescoberta = ConverterData.converterData(dataLong);
	}

	public BuscarDoenca() {

	}

}