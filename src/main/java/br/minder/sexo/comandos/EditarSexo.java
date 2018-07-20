package br.minder.sexo.comandos;

import br.minder.sexo.SexoId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarSexo {
	private SexoId id;
	private String genero;
}
