package br.hela.emergencia.comandos;

public class EditarEmergencia {

	private String planoDeSaude;
	private Boolean doadorDeOrgaos;
	private Boolean ataqueConvucivos;
	private String problemasCardiacos;

	public EditarEmergencia() {
		}

	public String getPlanoDeSaude() {
		return planoDeSaude;
	}

	public void setPlanoDeSaude(String planoDeSaude) {
		this.planoDeSaude = planoDeSaude;
	}

	public Boolean getDoadorDeOrgaos() {
		return doadorDeOrgaos;
	}

	public void setDoadorDeOrgaos(Boolean doadorDeOrgaos) {
		this.doadorDeOrgaos = doadorDeOrgaos;
	}

	public Boolean getAtaqueConvucivos() {
		return ataqueConvucivos;
	}

	public void setAtaqueConvucivos(Boolean ataqueConvucivos) {
		this.ataqueConvucivos = ataqueConvucivos;
	}

	public String getProblemasCardiacos() {
		return problemasCardiacos;
	}

	public void setProblemasCardiacos(String problemasCardiacos) {
		this.problemasCardiacos = problemasCardiacos;
	}

}
