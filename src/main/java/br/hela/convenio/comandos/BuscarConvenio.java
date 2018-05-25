package br.hela.convenio.comandos;

import br.hela.convenio.ConvenioId;
import lombok.Data;

@Data
public class BuscarConvenio {
	private ConvenioId id;
	private String nome;
	private int ativo;
}
