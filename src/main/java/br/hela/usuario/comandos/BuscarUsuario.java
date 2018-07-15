package br.hela.usuario.comandos;

import br.hela.conversor.ConverterData;
import br.hela.endereco.comandos.BuscarEndereco;
import br.hela.sangue.comandos.BuscarSangue;
import br.hela.sexo.comandos.BuscarSexo;
import br.hela.telefone.comandos.BuscarTelefone;
import br.hela.usuario.Usuario;
import br.hela.usuario.UsuarioId;
import lombok.Data;

@Data
public class BuscarUsuario {
	private UsuarioId id;
	private String username;
	private String email;
	private String senha;
	private String nome;
	private BuscarSangue sangue;
	private BuscarEndereco endereco;
	private BuscarTelefone telefone;
	private String dataNascimento;
	private BuscarSexo sexo;
	private String imagem;

	public BuscarUsuario(Usuario comandos) {
		this.id = comandos.getId();
		this.username = comandos.getNomeUsuario();
		this.email = comandos.getEmail();
		this.senha = comandos.getSenha();
		this.nome = comandos.getNomeCompleto();
		Long dataLong = comandos.getDataNascimento().getTime();
		this.dataNascimento = ConverterData.converterData(dataLong);
		this.imagem = comandos.getImagemUsuario();
	}

	public BuscarUsuario() {
	}
}
