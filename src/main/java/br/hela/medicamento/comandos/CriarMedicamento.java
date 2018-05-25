package br.hela.medicamento.comandos;

import lombok.Data;

@Data
public class CriarMedicamento {
	private String nomeMedicamento;
	private String composicao;
	private int ativo;
}
