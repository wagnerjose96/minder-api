package br.hela.cirurgia.comandos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.hela.medicamento.MedicamentoId;

public class CriarCirurgia {
	private String tipoCirurgia;
	private Date dataCirurgia;
	private String clinicaResponsavel;
	private String medicoResponsavel;
	private List<MedicamentoId> id_medicamentos = new ArrayList<MedicamentoId>();

	public CriarCirurgia() {
	}

	public String getTipoCirurgia() {
		return tipoCirurgia;
	}

	public void setTipoCirurgia(String tipoCirurgia) {
		this.tipoCirurgia = tipoCirurgia;
	}

	public Date getDataCirurgia() {
		return dataCirurgia;
	}

	public void setDataCirurgia(Date dataCirurgia) {
		this.dataCirurgia = dataCirurgia;
	}

	public String getClinicaResponsavel() {
		return clinicaResponsavel;
	}

	public void setClinicaResponsavel(String clinicaResponsavel) {
		this.clinicaResponsavel = clinicaResponsavel;
	}

	public String getMedicoResponsavel() {
		return medicoResponsavel;
	}

	public void setMedicoResponsavel(String medicoResponsavel) {
		this.medicoResponsavel = medicoResponsavel;
	}

	public List<MedicamentoId> getId_medicamentos() {
		return this.id_medicamentos;
	}

	public void setId_medicamentos(List<MedicamentoId> id_medicamentos) {
		this.id_medicamentos.addAll(id_medicamentos);
	}
}
