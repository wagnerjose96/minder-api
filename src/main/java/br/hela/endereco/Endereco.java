package br.hela.endereco;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.endereco.comandos.CriarEndereco;
import br.hela.endereco.comandos.EditarEndereco;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "rua", "bairro", "cidade", "estado", "complemento", "numero"})
public class Endereco {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private EnderecoId id;
	private String rua;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	private int numero;

	public Endereco() {
	}
	
	public void apply(EditarEndereco comando) {
		this.id =  comando.getId();
		this.rua = comando.getRua();
		this.bairro = comando.getBairro();
		this.cidade = comando.getCidade();;
		this.estado = comando.getEstado();;
		this.complemento = comando.getComplemento();;
		this.numero = comando.getNumero();;
	}

	public Endereco(CriarEndereco comando) {
		this.id =  new EnderecoId();
		this.rua = comando.getRua();
		this.bairro = comando.getBairro();
		this.cidade = comando.getCidade();;
		this.estado = comando.getEstado();;
		this.complemento = comando.getComplemento();;
		this.numero = comando.getNumero();;
	}
}
