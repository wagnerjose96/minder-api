package br.hela.sangue.comandos;

import br.hela.sangue.SangueId;
import lombok.Data;

@Data
public class EditarSangue {
	private SangueId idSangue;
	private String sangue;
}
