package br.minder.endereco.comandos;

import br.minder.endereco.Endereco;
import lombok.Data;

@Data
public class CriarEndereco {
	private String rua;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	private int numero;
	
	public CriarEndereco() {
	}
	
	public CriarEndereco(Endereco comando) {
		this.rua = comando.getRua();
		this.bairro = comando.getBairro();
		this.cidade = comando.getCidade();
		this.estado = comando.getEstado();
		this.complemento = comando.getComplemento();
		this.numero = comando.getNumero();
	}
}
