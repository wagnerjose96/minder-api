package br.hela.usuarioAdm;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.security.Criptografia;
import br.hela.usuarioAdm.comandos.CriarUsuarioAdm;
import br.hela.usuarioAdm.comandos.EditarUsuarioAdm;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "nome_usuario", "senha" })
public class UsuarioAdm {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private UsuarioAdmId id;
	private String nome_usuario;
	private String senha;
	
	public UsuarioAdm(){
	}

	public UsuarioAdm(CriarUsuarioAdm comando) {
		this.id = new UsuarioAdmId();
		this.nome_usuario = comando.getUsername();
		this.senha = Criptografia.criptografa(comando.getSenha());
	}
	
	public void apply(EditarUsuarioAdm comando) {
		this.id = comando.getId();
		this.nome_usuario = comando.getUsername();
		this.senha = Criptografia.criptografa(comando.getSenha());
	}
	
}
