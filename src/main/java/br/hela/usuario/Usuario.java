package br.hela.usuario;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.usuario.comandos.CriarUsuario;
import br.hela.usuario.comandos.EditarUsuario;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "nome_completo", "nome_usuario", "email", "senha", "tipo_sangue", "endereco", "telefone", "data_nascimento", "sexo", "imagem_usuario"})
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
		this.nome_completo = comando.getNome_completo();
		this.nome_usuario = comando.getNome_usuario();
		this.email = comando.getEmail();
		this.senha = comando.getSenha();
		this.tipo_sangue = comando.getTipo_sangue();
		this.endereco = comando.getEndereco();
		this.telefone = comando.getTelefone();
		this.data_nascimento = comando.getData_nascimento();
		this.sexo = comando.getSexo();
		this.imagem_usuario = comando.getImagem_usuario();
		this.ativo = comando.getAtivo();
	}

	public void apply(EditarUsuario comando) {
		this.id = comando.getId();
		this.nome_completo = comando.getNome_completo();
		this.senha = comando.getSenha();
		this.tipo_sangue = comando.getTipo_sangue();
		this.endereco = comando.getEndereco();
		this.telefone = comando.getTelefone();
		this.data_nascimento = comando.getData_nascimento();
		this.sexo = comando.getSexo();
		this.imagem_usuario = comando.getImagem_usuario();
		this.ativo = comando.getAtivo();
	}
}
