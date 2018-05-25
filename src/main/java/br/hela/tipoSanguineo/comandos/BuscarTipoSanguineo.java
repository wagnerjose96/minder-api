package br.hela.tipoSanguineo.comandos;

import br.hela.tipoSanguineo.TipoSanguineoId;
import lombok.Data;

@Data
public class BuscarTipoSanguineo {
	private TipoSanguineoId tipoSanguineoId;
	private String tipoSangue;
	private String rh;
}
