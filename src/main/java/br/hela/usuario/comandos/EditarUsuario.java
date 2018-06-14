package br.hela.usuario.comandos;

import br.hela.usuario.UsuarioId;
import lombok.Data;

@Data
public class EditarUsuario {
	private UsuarioId id;
	private String senha;
	private String nome;
	private String endereco;
	private int telefone;
	private String imagem;
}
