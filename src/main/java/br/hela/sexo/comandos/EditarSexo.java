package br.hela.sexo.comandos;

import br.hela.sexo.SexoId;
import lombok.Data;

@Data
public class EditarSexo {
	private SexoId idSexo;
	private String sexo;
}
