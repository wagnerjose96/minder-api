package br.minder.usuario.comandos;

import java.util.Date;

import br.minder.endereco.comandos.CriarEndereco;
import br.minder.genero.GeneroId;
import br.minder.sangue.SangueId;
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
	private GeneroId idGenero;
	private String imagem;
}
