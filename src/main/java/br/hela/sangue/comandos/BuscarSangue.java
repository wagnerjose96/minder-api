package br.hela.sangue.comandos;

import br.hela.sangue.Sangue;
import br.hela.sangue.SangueId;
import lombok.Data;

@Data
public class BuscarSangue {
	private SangueId idSangue;
	private String sangue;

	public BuscarSangue(Sangue comandos) {
		this.idSangue = comandos.getIdSangue();
		this.sangue = comandos.getTipoSanguineo();
	}
}
