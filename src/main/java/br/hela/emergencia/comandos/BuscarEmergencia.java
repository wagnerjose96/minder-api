package br.hela.emergencia.comandos;

import br.hela.emergencia.Emergencia;
import br.hela.emergencia.EmergenciaId;
import lombok.Data;

@Data
public class BuscarEmergencia {
	private EmergenciaId id;
	private int doadorDeOrgaos;
	private int ataqueConvulsivos;
	private String problemasCardiacos;
	
	public BuscarEmergencia() {
	}
	
	public BuscarEmergencia(Emergencia comandos) {
		this.id = comandos.getId();
		this.doadorDeOrgaos = comandos.getDoadorDeOrgaos();
		this.ataqueConvulsivos = comandos.getAtaqueConvulsivos();
		this.problemasCardiacos = comandos.getProblemasCardiacos();
	}
}
