package br.hela.emergencia.comandos;

import br.hela.emergencia.EmergenciaId;

public class EditarEmergencia {
	private EmergenciaId id;
	private String planoDeSaude;
	private String contatoEmergencia;
	private String endereco;
	private Boolean doadorDeOrgaos;
	private Boolean ataqueConvucivos;
	private String problemasCardiacos;

	public EditarEmergencia() {
	}

	public EmergenciaId getId() {
		return id;
	}

	public void setId(EmergenciaId id) {
		this.id = id;
	}

	public String getPlanoDeSaude() {
		return planoDeSaude;
	}

	public void setPlanoDeSaude(String planoDeSaude) {
		this.planoDeSaude = planoDeSaude;
	}

	public String getContatoEmergencia() {
		return contatoEmergencia;
	}

	public void setContatoEmergencia(String contatoEmergencia) {
		this.contatoEmergencia = contatoEmergencia;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
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
