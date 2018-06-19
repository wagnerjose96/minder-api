package br.hela.cirurgia.comandos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import br.hela.cirurgia.Cirurgia;
import br.hela.cirurgia.CirurgiaId;
import br.hela.medicamento.comandos.BuscarMedicamento;
import lombok.Data;

@Data
public class BuscarCirurgia {
	private CirurgiaId idCirurgia;
	private String tipoCirurgia;
	private String dataCirurgia;
	private String clinicaResponsavel;
	private String medicoResponsavel;
	private List<BuscarMedicamento> medicamentos = new ArrayList<>();

	public BuscarCirurgia(Cirurgia comando) {
		this.idCirurgia = comando.getIdCirurgia();
		this.tipoCirurgia = comando.getTipoCirurgia();
		Long dataLong = comando.getDataCirurgia().getTime(); // pega os milessegundos;
		this.dataCirurgia = converterData(dataLong);
		this.clinicaResponsavel = comando.getClinicaResponsavel();
		this.medicoResponsavel = comando.getMedicoResponsavel();
	}

	private String converterData(Long data) {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		String dataFormatada = formato.format(data);
		return dataFormatada;
	}

	public BuscarCirurgia() {

	}

}
