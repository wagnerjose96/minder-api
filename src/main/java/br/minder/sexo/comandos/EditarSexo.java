package br.minder.sexo.comandos;

import br.minder.sexo.SexoId;
import lombok.Data;

@Data
public class EditarSexo {
	private SexoId id;
	private String genero;
}
