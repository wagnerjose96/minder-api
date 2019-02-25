package br.minder.emergencia.comandos;

import br.minder.emergencia.Emergencia;
import br.minder.emergencia.EmergenciaId;
import br.minder.endereco.comandos.BuscarEndereco;
import br.minder.sangue.comandos.BuscarSangue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuscarEmergencia {
	private EmergenciaId id;
	private String nomeDoUsuario;
	private BuscarEndereco endereco;
	private BuscarSangue tipoSanguineo;
	private int ataqueConvulsivos;
	private String problemasCardiacos;
	private int doadorDeOrgaos;
	private int hipertensao;
	private int diabetes;

	public BuscarEmergencia(Emergencia comandos) {
		this.id = comandos.getIdEmergencia();
		this.ataqueConvulsivos = comandos.getAtaqueConvulsivos();
		this.problemasCardiacos = comandos.getProblemasCardiacos();
		this.doadorDeOrgaos = comandos.getDoadorDeOrgaos();
		this.hipertensao = comandos.getHipertensao();
		this.diabetes = comandos.getDiabetes();
	}

	public BuscarEmergencia() {
	}
}
