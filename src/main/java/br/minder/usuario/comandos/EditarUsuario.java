package br.minder.usuario.comandos;

import br.minder.endereco.comandos.EditarEndereco;
import br.minder.genero.comandos.EditarGenero;
import br.minder.sangue.comandos.EditarSangue;
import br.minder.telefone.comandos.EditarTelefone;
import br.minder.usuario.UsuarioId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarUsuario {
	private UsuarioId id;
	private String username;
	private String email;
	private String senha;
	private String nome;
	private EditarSangue sangue;
	private EditarEndereco endereco;
	private EditarTelefone telefone;
	private String dataNascimento;
	private EditarGenero genero;
	private String imagem;
}
