package br.hela.usuario.comandos;

import java.util.Date;
import br.hela.usuario.UsuarioId;
import lombok.Data;

@Data
public class BuscarUsuario {
	private UsuarioId id;
	private String nome_completo;
	private String senha;
	private String tipo_sangue;
	private String endereco;
	private int telefone;
	private Date data_nascimento;
	private String sexo;
	private String imagem_usuario;
	private int ativo;
}
