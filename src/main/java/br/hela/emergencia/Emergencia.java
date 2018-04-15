package br.hela.emergencia;

import java.util.List;

import br.hela.alergia.AlergiaId;
import br.hela.doenca.DoencaId;
import br.hela.emergencia.comandos.CriarEmergencia;
import br.hela.medicamento.MedicamentoId;

public class Emergencia {
	private EmergenciaId id;
	private String planoDeSaude;
	private String endereco;
	private List<AlergiaId> alergias;
	private List<DoencaId> doencas;
	private List<MedicamentoId> medicamentos;
	private Boolean doadorDeOrgaos;
	private Boolean ataqueConvucivos;
	private String problemasCardiacos;

	public Emergencia(CriarEmergencia comandos) {
		this.id = new EmergenciaId();
		this.planoDeSaude = comandos.getPlanoDeSaude();
		this.doadorDeOrgaos = comandos.getDoadorDeOrgaos();
		this.ataqueConvucivos = comandos.getAtaqueConvucivos();
		this.problemasCardiacos = comandos.getProblemasCardiacos();
	}

	public EmergenciaId getId() {
		return id;
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

	public List<AlergiaId> getAlergias() {
		return alergias;
	}

	public void setAlergias(List<AlergiaId> alergias) {
		this.alergias = alergias;
	}

	public List<DoencaId> getDoencas() {
		return doencas;
	}

	public void setDoencas(List<DoencaId> doencas) {
		this.doencas = doencas;
	}

	public List<MedicamentoId> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(List<MedicamentoId> medicamentos) {
		this.medicamentos = medicamentos;
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
