package br.hela.emergencia.comandos;

import br.hela.emergencia.Emergencia;
import br.hela.emergencia.EmergenciaId;
import lombok.Data;

@Data
public class BuscarEmergencia {
	private EmergenciaId id;
	private int ataqueConvulsivos;
	private String problemasCardiacos;
	public int doadorDeOrgaos;
	
	
	public BuscarEmergencia(Emergencia comandos) {
		this.id = comandos.getIdEmergencia();
		this.ataqueConvulsivos = comandos.getAtaqueConvulsivos();
		this.problemasCardiacos = comandos.getProblemasCardiacos();
		this.doadorDeOrgaos = comandos.getDoadorDeOrgaos();
	}

	public BuscarEmergencia() {
	}
}
