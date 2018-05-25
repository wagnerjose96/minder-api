package br.hela.tipoGenero.comandos;

import br.hela.tipoGenero.GeneroId;
import lombok.Data;

@Data
public class EditarGenero {
	private GeneroId id;
	private String genero;
}
