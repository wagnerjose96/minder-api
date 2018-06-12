package br.hela.convenio.comandos;

import br.hela.convenio.ConvenioId;
import lombok.Data;

@Data
public class EditarConvenio {
	private ConvenioId id;
	private String nome;
}
