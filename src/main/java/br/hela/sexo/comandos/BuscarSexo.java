package br.hela.sexo.comandos;

import br.hela.sexo.Sexo;
import br.hela.sexo.SexoId;
import lombok.Data;

@Data
public class BuscarSexo {
	private SexoId idSexo;
	private String sexo;
	
	public BuscarSexo(Sexo comandos) {
		this.idSexo = comandos.getIdSexo();
		this.sexo = comandos.getSexo();
	}

	public BuscarSexo() {
	}
}
