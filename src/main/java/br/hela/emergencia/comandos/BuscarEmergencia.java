package br.hela.emergencia.comandos;

import br.hela.emergencia.EmergenciaId;
import lombok.Data;

@Data
public class BuscarEmergencia {
	private EmergenciaId id;
	private int doadorDeOrgaos;
	private int ataqueConvulsivos;
	private String problemasCardiacos;
}
