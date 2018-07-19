package br.minder.convenio.comandos;

import br.minder.convenio.ConvenioId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarConvenio {
	private ConvenioId id;
	private String nome;
	private int ativo;
}
