package br.minder.usuario.comandos;

import java.sql.Date;

import br.minder.endereco.comandos.CriarEndereco;
import br.minder.sangue.SangueId;
import br.minder.sexo.SexoId;
import br.minder.telefone.comandos.CriarTelefone;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
