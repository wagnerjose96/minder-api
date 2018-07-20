package br.minder.convenio;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;

import br.minder.convenio.comandos.CriarConvenio;
import br.minder.convenio.comandos.EditarConvenio;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Getter
public class Convenio {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private ConvenioId id;
	private String nome;
	@Setter
	private int ativo;

	public Convenio() {
	}

	public Convenio(CriarConvenio comandos) {
		this.id = new ConvenioId();
		this.nome = comandos.getNome();
		this.ativo = 1;
	}

	public void apply(EditarConvenio comandos) {
		this.id = comandos.getId();
		this.nome = comandos.getNome();
		this.ativo = comandos.getAtivo();
	}
}
