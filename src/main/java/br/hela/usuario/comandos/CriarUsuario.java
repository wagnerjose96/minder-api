package br.hela.usuario.comandos;

import java.sql.Date;
import br.hela.endereco.comandos.CriarEndereco;
import br.hela.sangue.SangueId;
import br.hela.sexo.SexoId;
import br.hela.telefone.comandos.CriarTelefone;
import lombok.Data;

@Data
public class CriarUsuario {
	private String username;
	private String email;
	private String senha;
	private String nome;
	private SangueId idSangue;
	private CriarEndereco endereco;
	private CriarTelefone telefone;
	private Date dataNascimento;
	private SexoId idSexo;
	private String imagem;
}
