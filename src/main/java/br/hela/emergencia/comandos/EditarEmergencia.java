package br.hela.emergencia.comandos;

import br.hela.emergencia.EmergenciaId;

public class EditarEmergencia {
	private EmergenciaId id;
	private int doadorDeOrgaos;
	private int ataqueConvulsivos;
	private String problemasCardiacos;

	public EditarEmergencia() {
	}

	public EmergenciaId getId() {
		return id;
	}

	public int getDoadorDeOrgaos() {
		return doadorDeOrgaos;
	}

	public void setDoadorDeOrgaos(int doadorDeOrgaos) {
		this.doadorDeOrgaos = doadorDeOrgaos;
	}

	public int getAtaqueConvulsivos() {
		return ataqueConvulsivos;
	}

	public void setAtaqueConvulsivos(int ataqueConvulsivos) {
		this.ataqueConvulsivos = ataqueConvulsivos;
	}

	public String getProblemasCardiacos() {
		return problemasCardiacos;
	}

	public void setProblemasCardiacos(String problemasCardiacos) {
		this.problemasCardiacos = problemasCardiacos;
	}

}
