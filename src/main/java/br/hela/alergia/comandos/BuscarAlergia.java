package br.hela.alergia.comandos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import br.hela.alergia.Alergia;
import br.hela.alergia.AlergiaId;
import br.hela.medicamento.comandos.BuscarMedicamento;
import lombok.Data;

@Data
public class BuscarAlergia {
	private AlergiaId idAlergia;
	private String tipoAlergia;
	private String localAfetado;
	private String dataDescoberta;
	private String efeitos;
	private List<BuscarMedicamento> medicamentos = new ArrayList<>();

	public BuscarAlergia(Alergia comando) {
		this.idAlergia = comando.getIdAlergia();
		this.tipoAlergia = comando.getTipoAlergia();
		this.localAfetado = comando.getLocalAfetado();
		Long dataLong = comando.getDataDescoberta().getTime(); // pega os milessegundos;
		this.dataDescoberta = converterData(dataLong);
		this.efeitos = comando.getEfeitos();
	}

	private String converterData(Long data) {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		String dataFormatada = formato.format(data);
		return dataFormatada;
	}

	public BuscarAlergia() {

	}
}
