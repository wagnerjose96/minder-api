package br.minder.sangue.comandos;

import br.minder.sangue.SangueId;
import lombok.Data;

@Data
public class EditarSangue {
	private SangueId idSangue;
	private String tipoSanguineo;
}
