package br.hela.sexo.comandos;

import br.hela.sexo.Sexo;
import br.hela.sexo.SexoId;
import lombok.Data;

@Data
public class BuscarSexo {
	private SexoId id;
	private String genero;
	
	public BuscarSexo(Sexo comandos) {
		this.id = comandos.getIdGenero();
		this.genero = comandos.getGenero();
	}

	public BuscarSexo() {
	}
}
