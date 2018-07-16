package br.minder.sangue.comandos;

import br.minder.sangue.Sangue;
import br.minder.sangue.SangueId;
import lombok.Data;

@Data
public class BuscarSangue {
	private SangueId idSangue;
	private String tipoSanguineo;

	public BuscarSangue(Sangue comandos) {
		this.idSangue = comandos.getIdSangue();
		this.tipoSanguineo = comandos.getTipoSanguineo();
	}
}
