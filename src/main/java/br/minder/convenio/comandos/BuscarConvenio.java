package br.minder.convenio.comandos;

import br.minder.convenio.Convenio;
import br.minder.convenio.ConvenioId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuscarConvenio {
	private ConvenioId id;
	private String nome;
	private int ativo;

	public BuscarConvenio(Convenio comandos) {
		this.id = comandos.getId();
		this.nome = comandos.getNome();
		this.ativo = comandos.getAtivo();
	}
}
