package br.hela.emergencia.comandos;

public class CriarEmergencia {
	private String idPlanoSaude;
	private String contatoEmergencia;
	private String endereco;
	private Boolean doadorDeOrgaos;
	private Boolean ataqueConvucivos;
	private String problemasCardiacos;

	public CriarEmergencia() {
	}

	public String getContatoEmergencia() {
		return contatoEmergencia;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public void setContatoEmergencia(String contatoEmergencia) {
		this.contatoEmergencia = contatoEmergencia;
	}

	public String getIdPlanoSaude() {
		return idPlanoSaude;
	}

	public void setIdPlanoSaude(String idPlanoSaude) {
		this.idPlanoSaude = idPlanoSaude;
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
