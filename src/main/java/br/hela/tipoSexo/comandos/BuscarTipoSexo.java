package br.hela.tipoSexo.comandos;

import br.hela.tipoSexo.TipoSexoId;
import lombok.Data;

@Data
public class BuscarTipoSexo {
	private TipoSexoId id;
	private String sexo;
}
