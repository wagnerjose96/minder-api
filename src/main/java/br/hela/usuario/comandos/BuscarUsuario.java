package br.hela.usuario.comandos;

import java.util.Date;
import br.hela.endereco.comandos.BuscarEndereco;
import br.hela.sangue.comandos.BuscarSangue;
import br.hela.sexo.comandos.BuscarSexo;
import br.hela.usuario.Usuario;
import br.hela.usuario.UsuarioId;
import lombok.Data;

@Data
public class BuscarUsuario {
	private UsuarioId id;
	private String username;
	private String email;
	private String senha;
	private String nome;
	private BuscarSangue sangue;
	private BuscarEndereco endereco;
	private int telefone;
	private Date dataNascimento;
	private BuscarSexo sexo;
	private String imagem;
	
	public BuscarUsuario(Usuario comandos) {
		this.id = comandos.getId();
		this.username = comandos.getNome_usuario();
		this.email = comandos.getEmail();
		this.senha = comandos.getSenha();
		this.nome = comandos.getNome_completo();
		this.telefone = comandos.getTelefone();
		this.dataNascimento = comandos.getData_nascimento();
		this.imagem = comandos.getImagem_usuario();
	}

	public BuscarUsuario() {
	}
}
