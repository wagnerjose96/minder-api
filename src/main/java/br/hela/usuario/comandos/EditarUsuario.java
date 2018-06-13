package br.hela.usuario.comandos;

import java.util.Date;
import br.hela.usuario.UsuarioId;
import lombok.Data;

@Data
public class EditarUsuario {
	private UsuarioId id;
	private String senha;
	private String nome;
	private String tipoSanguineo;
	private String endereco;
	private int telefone;
	private Date dataNascimento;
	private String sexo;
	private String imagem;
}
