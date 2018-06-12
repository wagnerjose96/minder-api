package br.hela.medicamento.comandos;

import br.hela.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class EditarMedicamento {
	private MedicamentoId idMedicamento;
	private String nomeMedicamento;
	private String composicao;
}
