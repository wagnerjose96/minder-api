package br.hela.usuario;

import java.sql.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.endereco.EnderecoId;
import br.hela.esqueci_senha.comandos.GerarSenha;
import br.hela.sangue.SangueId;
import br.hela.security.Criptografia;
import br.hela.sexo.SexoId;
import br.hela.telefone.TelefoneId;
import br.hela.usuario.comandos.CriarUsuario;
import br.hela.usuario.comandos.EditarUsuario;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "nomeCompleto", "nomeUsuario", "email", "senha", "idSangue", "idEndereco",
		"idTelefone", "dataNascimento", "idSexo", "imagemUsuario", "ativo" })
public class Usuario {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private UsuarioId id;
	@Column(name = "nome_completo")
	private String nomeCompleto;
	@Column(name = "nome_usuario")
	private String nomeUsuario;
	private String senha;
	private String email;
	@AttributeOverride(name = "value", column = @Column(name = "id_sangue"))
	private SangueId idSangue;
	@AttributeOverride(name = "value", column = @Column(name = "id_endereco"))
	private EnderecoId idEndereco;
	@AttributeOverride(name = "value", column = @Column(name = "id_telefone"))
	private TelefoneId idTelefone;
	@Column(name = "data_nascimento")
	private Date dataNascimento;
	@AttributeOverride(name = "value", column = @Column(name = "id_sexo"))
	private SexoId idSexo;
	@Column(name = "imagem_usuario")
	private String imagemUsuario;
	private int ativo;

	public Usuario() {
	}

	public Usuario(CriarUsuario comando) {
		this.id = new UsuarioId();
		this.nomeCompleto = comando.getNome();
		this.nomeUsuario = comando.getUsername();
		this.email = comando.getEmail();
		this.senha = Criptografia.criptografa(comando.getSenha());
		this.idSangue = comando.getIdSangue();
		this.dataNascimento = comando.getDataNascimento();
		this.idSexo = comando.getIdSexo();
		this.imagemUsuario = comando.getImagem();
		this.ativo = 1;
	}

	public void apply(EditarUsuario comando) {
		this.id = comando.getId();
		this.nomeCompleto = comando.getNome();
		this.senha = Criptografia.criptografa(comando.getSenha());
		this.imagemUsuario = comando.getImagem();
	}

	public void applySenha(GerarSenha comando) {
		this.email = comando.getEmail();
		this.senha = Criptografia.criptografa(comando.getSenha());
	}

}
