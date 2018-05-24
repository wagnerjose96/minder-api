package br.hela.doenca.comandos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.hela.doenca.Doenca;
import br.hela.doenca.DoencaId;
import br.hela.medicamento.Medicamento;

public class BuscarDoenca {
	private DoencaId idDoenca;
	private String nomeDoenca;
	private Date dataDescoberta;
	private List<Medicamento> medicamentos = new ArrayList<Medicamento>();

	public BuscarDoenca() {
	}

	public BuscarDoenca(Doenca comandos) {
		this.idDoenca = comandos.getIdDoenca();
		this.nomeDoenca = comandos.getNomeDoenca();
		this.dataDescoberta = comandos.getDataDescoberta();
	}

	public DoencaId getIdDoenca() {
		return idDoenca;
	}

	public void setIdDoenca(DoencaId idDoenca) {
		this.idDoenca = idDoenca;
	}

	public String getNomeDoenca() {
		return nomeDoenca;
	}

	public void setNomeDoenca(String nomeDoenca) {
		this.nomeDoenca = nomeDoenca;
	}

	public Date getDataDescoberta() {
		return dataDescoberta;
	}

	public void setDataDescoberta(Date dataDescoberta) {
		this.dataDescoberta = dataDescoberta;
	}

	public List<Medicamento> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(List<Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}

}