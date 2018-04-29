package br.hela.convenio;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.convenio.comandos.CriarConvenio;
import br.hela.convenio.comandos.EditarConvenio;

@Entity
@Audited
public class Convenio {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setId(ConvenioId id) {
		this.id = id;
	}

	public ConvenioId getId() {
		return id;
	}

	public int getAtivo() {
		return ativo;
	}

	public void setAtivo(int ativo) {
		this.ativo = ativo;
	}
}
