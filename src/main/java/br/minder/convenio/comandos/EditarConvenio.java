package br.minder.convenio.comandos;

import br.minder.convenio.ConvenioId;
import lombok.Data;

@Data
public class EditarConvenio {
	private ConvenioId id;
	private String nome;
	private int ativo;
}
