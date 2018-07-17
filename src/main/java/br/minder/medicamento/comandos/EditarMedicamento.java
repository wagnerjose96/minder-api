package br.minder.medicamento.comandos;

import br.minder.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class EditarMedicamento {
	private MedicamentoId idMedicamento;
	private String nomeMedicamento;
	private String composicao;
	private int ativo;
}
