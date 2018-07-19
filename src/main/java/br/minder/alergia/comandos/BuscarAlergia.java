package br.minder.alergia.comandos;

import java.util.ArrayList;
import java.util.List;

import br.minder.alergia.Alergia;
import br.minder.alergia.AlergiaId;
import br.minder.conversor.ConverterData;
import br.minder.medicamento.comandos.BuscarMedicamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
		Long dataLong = comando.getDataDescoberta().getTime();
		this.dataDescoberta = ConverterData.converterData(dataLong);
		this.efeitos = comando.getEfeitos();
	}
}
