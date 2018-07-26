package br.minder.genero.comandos;

import br.minder.genero.Genero;
import br.minder.genero.GeneroId;
import lombok.Getter;

@Getter
public class BuscarGenero {
	private GeneroId id;
	private String genero;
	
	public BuscarGenero(Genero comandos) {
		this.id = comandos.getIdGenero();
		this.genero = comandos.getTipoGenero();
	}
}
