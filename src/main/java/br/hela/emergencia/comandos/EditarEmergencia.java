package br.hela.emergencia.comandos;

import br.hela.emergencia.EmergenciaId;
import lombok.Data;

@Data
public class EditarEmergencia {
	private EmergenciaId id;
	private int ataqueConvulsivos;
	private String problemasCardiacos;
	public int doadorDeOrgaos;
}
