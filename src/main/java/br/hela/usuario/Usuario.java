package br.hela.usuario;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.esqueciSenha.comandos.GerarSenha;
import br.hela.security.Criptografia;
import br.hela.usuario.comandos.CriarUsuario;
import br.hela.usuario.comandos.EditarUsuario;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "nome_completo", "nome_usuario", "email", "senha", "tipo_sangue", "endereco", "telefone", "data_nascimento", "sexo", "imagem_usuario", "ativo"})
public class Usuario {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private UsuarioId id;
	private String nome_completo;
	private String nome_usuario;
	private String email;
	private String senha;
	private String tipo_sangue;
	private String endereco;
	private int telefone;
	private Date data_nascimento;
	private String sexo;
	private String imagem_usuario;
	private int ativo;
	
	public Usuario() {
	}

	public Usuario(CriarUsuario comando) {
		this.id = new UsuarioId();
		this.nome_completo = comando.getNome();
		this.nome_usuario = comando.getUsername();
		this.email = comando.getEmail();
		this.senha = Criptografia.criptografa(comando.getSenha());
		this.tipo_sangue = comando.getTipoSanguineo();
		this.endereco = comando.getEndereco();
		this.telefone = comando.getTelefone();
		this.data_nascimento = comando.getDataNascimento();
		this.sexo = comando.getSexo();
		this.imagem_usuario = comando.getImagem();
		this.ativo = 1;
	}

	public void apply(EditarUsuario comando) {
		this.id = comando.getId();
		this.nome_completo = comando.getNome();
		this.senha = Criptografia.criptografa(comando.getSenha());
		this.tipo_sangue = comando.getTipoSanguineo();
		this.endereco = comando.getEndereco();
		this.telefone = comando.getTelefone();
		this.data_nascimento = comando.getDataNascimento();
		this.sexo = comando.getSexo();
		this.imagem_usuario = comando.getImagem();
	}
	
	public void applySenha(GerarSenha comando) {
		this.email = comando.getEmail();
		this.senha = Criptografia.criptografa(comando.getSenha());
	}

	

}
