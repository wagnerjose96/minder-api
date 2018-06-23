package br.hela.sexo.comandos;

import br.hela.sexo.Sexo;
import br.hela.sexo.SexoId;
import lombok.Data;

@Data
public class BuscarSexo {
	private SexoId idGenero;
	private String genero;
	
	public BuscarSexo(Sexo comandos) {
		this.idGenero = comandos.getIdGenero();
		this.genero = comandos.getGenero();
	}

	public BuscarSexo() {
	}
}
