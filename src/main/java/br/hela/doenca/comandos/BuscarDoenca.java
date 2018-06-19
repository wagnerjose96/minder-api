package br.hela.doenca.comandos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import br.hela.doenca.Doenca;
import br.hela.doenca.DoencaId;
import br.hela.medicamento.comandos.BuscarMedicamento;
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
		Long dataLong = comandos.getDataDescoberta().getTime(); // pega os milessegundos;
		this.dataDescoberta = converterData(dataLong);
	}

	private String converterData(Long data) {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		String dataFormatada = formato.format(data);
		return dataFormatada;
	}

	public BuscarDoenca() {

	}

}