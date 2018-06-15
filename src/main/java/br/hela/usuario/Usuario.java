package br.hela.usuario;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.hela.endereco.EnderecoId;
import br.hela.esqueciSenha.comandos.GerarSenha;
import br.hela.sangue.SangueId;
import br.hela.security.Criptografia;
import br.hela.sexo.SexoId;
import br.hela.usuario.comandos.CriarUsuario;
import br.hela.usuario.comandos.EditarUsuario;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Entity
@Audited
@Data
@EqualsAndHashCode(exclude = { "nome_completo", "nome_usuario", "email", "senha", "idSangue", "idEndereco", "telefone",
		"data_nascimento", "idSexo", "imagem_usuario", "ativo" })
public class Usuario {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	@Setter(AccessLevel.NONE)
	private UsuarioId id;
	private String nome_completo;
	private String nome_usuario;
	private String senha;
	private String email;
	@AttributeOverride(name = "value", column = @Column(name = "id_sangue"))
	private SangueId idSangue;
	@AttributeOverride(name = "value", column = @Column(name = "id_endereco"))
	private EnderecoId idEndereco;
	private int telefone;
	private Date data_nascimento;
	@AttributeOverride(name = "value", column = @Column(name = "id_sexo"))
	private SexoId idSexo;
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
		this.idSangue = comando.getIdSangue();
		this.telefone = comando.getTelefone();
		this.data_nascimento = comando.getDataNascimento();
		this.idSexo = comando.getIdSexo();
		this.imagem_usuario = comando.getImagem();
		this.ativo = 1;
	}

	public void apply(EditarUsuario comando) {
		this.id = comando.getId();
		this.nome_completo = comando.getNome();
		this.senha = Criptografia.criptografa(comando.getSenha());
		this.telefone = comando.getTelefone();
		this.imagem_usuario = comando.getImagem();
	}

	public void applySenha(GerarSenha comando) {
		this.email = comando.getEmail();
		this.senha = Criptografia.criptografa(comando.getSenha());
	}

}
