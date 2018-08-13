package br.minder.cirurgia.comandos;

import java.util.ArrayList;
import java.util.List;

import br.minder.cirurgia.Cirurgia;
import br.minder.cirurgia.CirurgiaId;
import br.minder.conversor.ConverterData;
import br.minder.medicamento.comandos.BuscarMedicamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
		Long dataLong = comando.getDataCirurgia().getTime();
		this.dataCirurgia = ConverterData.converterData(dataLong);
		this.clinicaResponsavel = comando.getClinicaResponsavel();
		this.medicoResponsavel = comando.getMedicoResponsavel();
	}
}
