package br.hela.convenio.comandos;

import br.hela.convenio.Convenio;
import br.hela.convenio.ConvenioId;
import lombok.Data;

@Data
public class BuscarConvenio {
	private ConvenioId id;
	private String nome;
	private int ativo;

	public BuscarConvenio() {
	}

	public BuscarConvenio(Convenio comandos) {
		this.id = comandos.getId();
		this.nome = comandos.getNome();
		this.ativo = comandos.getAtivo();
	}
}
