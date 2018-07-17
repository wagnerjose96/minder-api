package br.minder.usuario.comandos;

import br.minder.endereco.comandos.EditarEndereco;
import br.minder.telefone.comandos.EditarTelefone;
import br.minder.usuario.UsuarioId;
import lombok.Data;

@Data
public class EditarUsuario {
	private UsuarioId id;
	private String senha;
	private String nome;
	private EditarEndereco endereco;
	private EditarTelefone telefone;
	private String imagem;
}
