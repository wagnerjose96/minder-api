package br.hela.cirurgia.comandos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.hela.cirurgia.Cirurgia;
import br.hela.cirurgia.CirurgiaId;
import br.hela.medicamento.comandos.BuscarMedicamento;
import lombok.Data;

@Data
public class BuscarCirurgia {
	private CirurgiaId idCirurgia;
	private String tipoCirurgia;
	private Date dataCirurgia;
	private String clinicaResponsavel;
	private String medicoResponsavel;
	private List<BuscarMedicamento> medicamentos = new ArrayList<>();

	public BuscarCirurgia(Cirurgia comando) {
		this.idCirurgia = comando.getIdCirurgia();
		this.tipoCirurgia = comando.getTipoCirurgia();
		this.dataCirurgia = comando.getDataCirurgia();
		this.clinicaResponsavel = comando.getClinicaResponsavel();
		this.medicoResponsavel = comando.getMedicoResponsavel();
	}
}
