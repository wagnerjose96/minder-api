package br.minder.sangue.comandos;

import br.minder.sangue.SangueId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarSangue {
	private SangueId idSangue;
	private String tipoSanguineo;
}
