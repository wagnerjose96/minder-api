package br.hela.convenio;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.convenio.comandos.CriarConvenio;
import br.hela.convenio.comandos.EditarConvenio;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "nome", "ativo"})
public class Convenio {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private ConvenioId id;
	private String nome;
	private int ativo;

	public Convenio() {
	}

	public Convenio(CriarConvenio comandos) {
		this.id = new ConvenioId();
		this.nome = comandos.getNome();
		this.ativo = comandos.getAtivo();
	}

	public void apply(EditarConvenio comandos) {
		this.id = comandos.getId();
		this.nome = comandos.getNome();
		this.ativo = comandos.getAtivo();
	}
}
