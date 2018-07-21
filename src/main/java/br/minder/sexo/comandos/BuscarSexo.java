package br.minder.sexo.comandos;

import br.minder.sexo.Sexo;
import br.minder.sexo.SexoId;
import lombok.Getter;

@Getter
public class BuscarSexo {
	private SexoId id;
	private String genero;
	
	public BuscarSexo(Sexo comandos) {
		this.id = comandos.getIdGenero();
		this.genero = comandos.getGenero();
	}
}
