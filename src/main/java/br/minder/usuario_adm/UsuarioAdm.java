package br.minder.usuario_adm;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;

import br.minder.security.Criptografia;
import br.minder.usuario_adm.comandos.CriarUsuarioAdm;
import br.minder.usuario_adm.comandos.EditarUsuarioAdm;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "nomeUsuario", "senha" })
public class UsuarioAdm {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private UsuarioAdmId id;
	@Column(name = "nome_usuario")
	private String nomeUsuario;
	private String senha;
	
	public UsuarioAdm(){
	}

	public UsuarioAdm(CriarUsuarioAdm comando) {
		this.id = new UsuarioAdmId();
		this.nomeUsuario = comando.getNome();
		this.senha = Criptografia.criptografa(comando.getSenha());
	}
	
	public void apply(EditarUsuarioAdm comando) {
		this.id = comando.getId();
		this.nomeUsuario = comando.getNome();
		this.senha = Criptografia.criptografa(comando.getSenha());
	}
	
}
