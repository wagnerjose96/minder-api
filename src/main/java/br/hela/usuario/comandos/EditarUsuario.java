package br.hela.usuario.comandos;

import br.hela.endereco.comandos.EditarEndereco;
import br.hela.usuario.UsuarioId;
import lombok.Data;

@Data
public class EditarUsuario {
	private UsuarioId id;
	private String senha;
	private String nome;
	private EditarEndereco endereco;
	private int telefone;
	private String imagem;
}
