package br.hela.usuario.comandos;

import java.text.SimpleDateFormat;
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
		this.username = comandos.getNome_usuario();
		this.email = comandos.getEmail();
		this.senha = comandos.getSenha();
		this.nome = comandos.getNome_completo();
		Long dataLong = comandos.getData_nascimento().getTime(); //pega os milessegundos;
		this.dataNascimento = converterData(dataLong);
		this.imagem = comandos.getImagem_usuario();
	}
	
	private String converterData(Long data) {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		String dataFormatada = formato.format(data);
		return dataFormatada;
	}
	
	public BuscarUsuario() {
	}
}