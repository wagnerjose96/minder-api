package br.minder.emergencia.comandos;

import br.minder.emergencia.EmergenciaId;
import lombok.Data;

@Data
public class EditarEmergencia {
	private EmergenciaId id;
	private int ataqueConvulsivos;
	private String problemasCardiacos;
	private int doadorDeOrgaos;
}
