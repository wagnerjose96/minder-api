package br.hela.usuario.comandos;

import java.util.Date;
import lombok.Data;

@Data
public class CriarUsuario {
	private String username;
	private String email;
	private String senha;
	private String nome;
	private String tipoSanguineo;
	private String endereco;
	private int telefone;
	private Date dataNascimento;
	private String sexo;
	private String imagem;
}