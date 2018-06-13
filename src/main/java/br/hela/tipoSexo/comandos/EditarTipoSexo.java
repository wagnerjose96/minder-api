package br.hela.tipoSexo.comandos;

import br.hela.tipoSexo.TipoSexoId;
import lombok.Data;

@Data
public class EditarTipoSexo {
	private TipoSexoId id;
	private String sexo;
}
