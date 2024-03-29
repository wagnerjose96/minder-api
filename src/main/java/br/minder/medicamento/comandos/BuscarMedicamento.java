package br.minder.medicamento.comandos;

import br.minder.medicamento.Medicamento;
import br.minder.medicamento.MedicamentoId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuscarMedicamento {
	private MedicamentoId idMedicamento;
	private String nomeMedicamento;
	private String composicao;
	
	public BuscarMedicamento(Medicamento comandos) {
		this.idMedicamento = comandos.getIdMedicamento();
		this.nomeMedicamento = comandos.getNomeMedicamento();
		this.composicao = comandos.getComposicao();
	}

	public BuscarMedicamento() {
	}
}
