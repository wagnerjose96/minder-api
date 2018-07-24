package br.minder.endereco.comandos;

import br.minder.endereco.Endereco;
import br.minder.endereco.EnderecoId;
import lombok.Data;

@Data
public class EditarEndereco {
	private EnderecoId id;
	private String rua;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	private int numero;
	
	public EditarEndereco() {
	}
	
	public EditarEndereco(Endereco comando) {
		this.rua = comando.getRua();
		this.bairro = comando.getBairro();
		this.cidade = comando.getCidade();
		this.estado = comando.getEstado();
		this.complemento = comando.getComplemento();
		this.numero = comando.getNumero();
	}
}
