package br.minder.genero.comandos;

import br.minder.genero.GeneroId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarGenero {
	private GeneroId id;
	private String genero;
}
