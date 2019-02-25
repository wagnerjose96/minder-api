package br.minder.usuario.comandos;

import br.minder.endereco.comandos.EditarEndereco;
import br.minder.genero.GeneroId;
import br.minder.sangue.SangueId;
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
	private String nome;
	private SangueId idSangue;
	private EditarEndereco endereco;
	private EditarTelefone telefone;
	private String dataNascimento;
	private GeneroId idGenero;
	private String imagem;
}