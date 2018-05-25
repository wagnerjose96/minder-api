package br.hela.medicamento.comandos;

import br.hela.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class BuscarMedicamento {
	private MedicamentoId idMedicamento;
	private String nomeMedicamento;
	private String composicao;
	public int ativo;

}
