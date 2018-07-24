package br.minder.endereco.comandos;

import br.minder.endereco.Endereco;
import br.minder.endereco.EnderecoId;
import lombok.Getter;

@Getter
public class BuscarEndereco {
	private EnderecoId id;
	private String rua;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	private String numero;

	public BuscarEndereco(Endereco comandos) {
		this.id = comandos.getId();
		this.rua = comandos.getRua();
		this.bairro = comandos.getBairro();
		this.cidade = comandos.getCidade();
		this.estado = comandos.getEstado();
		this.complemento = comandos.getComplemento();
		this.numero = comandos.getNumero();
	}
}
