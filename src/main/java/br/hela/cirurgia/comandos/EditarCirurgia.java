package br.hela.cirurgia.comandos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.hela.cirurgia.CirurgiaId;
import br.hela.medicamento.MedicamentoId;

public class EditarCirurgia {
	private CirurgiaId idCirurgia;
	private String tipoCirurgia;
	private Date dataCirurgia;
	private String clinicaResponsavel;
	private String medicoResponsavel;
	private List<MedicamentoId> id_medicamentos = new ArrayList<MedicamentoId>();

	public EditarCirurgia() {
	}

	public CirurgiaId getIdCirurgia() {
		return idCirurgia;
	}

	public void setIdCirurgia(CirurgiaId idCirurgia) {
		this.idCirurgia = idCirurgia;
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
		return id_medicamentos;
	}

	public void setId_medicamentos(List<MedicamentoId> id_medicamentos) {
		this.id_medicamentos = id_medicamentos;
	}

}
