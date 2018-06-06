package br.hela.usuario.comandos;
import java.util.Date;
import lombok.Data;

@Data
public class CriarUsuario {
	private String nome_usuario;
	private String email;
	private String senha;
	private String nome_completo;
	private String tipo_sangue;
	private String endereco;
	private int telefone;
	private Date data_nascimento;
	private String sexo;
	private String imagem_usuario;
	private int ativo = 1;
}