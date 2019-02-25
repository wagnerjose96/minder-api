package br.minder.emergencia.comandos;

import br.minder.emergencia.EmergenciaId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarEmergencia {
	private EmergenciaId id;
	private int ataqueConvulsivos;
	private String problemasCardiacos;
	private int doadorDeOrgaos;
	private int hipertensao;
	private int diabetes;
}
