package br.hela.emergencia;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import br.hela.emergencia.comandos.CriarEmergencia;
import br.hela.emergencia.comandos.EditarEmergencia;

@Entity
@Audited
public class Emergencia {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private EmergenciaId id;
	private String planoDeSaude;
	private String contatoEmergencia;
	private String endereco;
	private Boolean doadorDeOrgaos;
	private Boolean ataqueConvucivos;
	private String problemasCardiacos;

	public Emergencia() {
	}
	
	public Emergencia(CriarEmergencia comandos) {
		this.id = new EmergenciaId();
		this.planoDeSaude = comandos.getPlanoDeSaude();
		this.doadorDeOrgaos = comandos.getDoadorDeOrgaos();
		this.ataqueConvucivos = comandos.getAtaqueConvucivos();
		this.problemasCardiacos = comandos.getProblemasCardiacos();
		this.endereco = comandos.getEndereco();
		this.contatoEmergencia = comandos.getContatoEmergencia();
	}
	
	public void apply(EditarEmergencia comandos) {
		this.id = comandos.getId();
		this.planoDeSaude = comandos.getPlanoDeSaude();
		this.doadorDeOrgaos = comandos.getDoadorDeOrgaos();
		this.ataqueConvucivos = comandos.getAtaqueConvucivos();
		this.problemasCardiacos = comandos.getProblemasCardiacos();
		this.endereco = comandos.getEndereco();
		this.contatoEmergencia = comandos.getContatoEmergencia();
	}
	

	public EmergenciaId getId() {
		return id;
	}

	public String getContatoEmergencia() {
		return contatoEmergencia;
	}

	public void setContatoEmergencia(String contatoEmergencia) {
		this.contatoEmergencia = contatoEmergencia;
	}

	public String getPlanoDeSaude() {
		return planoDeSaude;
	}

	public void setPlanoDeSaude(String planoDeSaude) {
		this.planoDeSaude = planoDeSaude;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Emergencia other = (Emergencia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
