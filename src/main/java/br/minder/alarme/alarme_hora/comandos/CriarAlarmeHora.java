package br.minder.alarme.alarme_hora.comandos;

import java.util.Date;

import br.minder.alarme.AlarmeId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarAlarmeHora {
	private AlarmeId idAlarme;
	private Date dataAlarme;
}
