package br.hela.endereco.comandos;

import br.hela.endereco.Endereco;
import br.hela.endereco.EnderecoId;
import lombok.Data;

@Data
public class BuscarEndereco {
	private EnderecoId id;
	private String rua;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	private int numero;

	public BuscarEndereco(Endereco comandos) {
		super();
		this.id = comandos.getId();
		this.rua = comandos.getRua();
		this.bairro = comandos.getBairro();
		this.cidade = comandos.getCidade();
		this.estado = comandos.getEstado();
		this.complemento = comandos.getComplemento();
		this.numero = comandos.getNumero();
	}
}
