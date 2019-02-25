package br.minder.usuario;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import br.minder.endereco.EnderecoId;
import br.minder.esqueci_senha.comandos.EditarSenha;
import br.minder.esqueci_senha.comandos.GerarSenha;
import br.minder.genero.GeneroId;
import br.minder.sangue.SangueId;
import br.minder.security.Criptografia;
import br.minder.telefone.TelefoneId;
import br.minder.usuario.comandos.CriarUsuario;
import br.minder.usuario.comandos.EditarUsuario;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Getter
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
	@Setter
	private EnderecoId idEndereco;
	@AttributeOverride(name = "value", column = @Column(name = "id_telefone"))
	@Setter
	private TelefoneId idTelefone;
	@Column(name = "data_nascimento")
	private Date dataNascimento;
	@AttributeOverride(name = "value", column = @Column(name = "id_genero"))
	private GeneroId idGenero;
	@Column(name = "imagem_usuario")
	private String imagemUsuario;
	@Setter
	private int ativo;

	public Usuario() {
	}

	public Usuario(CriarUsuario comando) throws NoSuchAlgorithmException {
		this.id = new UsuarioId();
		this.nomeCompleto = comando.getNome();
		this.nomeUsuario = comando.getUsername();
		this.email = comando.getEmail();
		this.senha = Criptografia.criptografa(comando.getSenha());
		this.idSangue = comando.getIdSangue();
		this.dataNascimento = comando.getDataNascimento();
		this.idGenero = comando.getIdGenero();
		this.imagemUsuario = comando.getImagem();
		this.ativo = 1;
	}

	public void apply(EditarUsuario comando) {
		this.id = comando.getId();
		this.nomeCompleto = comando.getNome();
		this.imagemUsuario = comando.getImagem();
	}

	public void applySenha(GerarSenha comando) throws NoSuchAlgorithmException {
		this.senha = Criptografia.criptografa(comando.getSenha());
	}
	
	public void applySenha(EditarSenha comando) throws NoSuchAlgorithmException {
		this.senha = Criptografia.criptografa(comando.getSenha());
	}

}
