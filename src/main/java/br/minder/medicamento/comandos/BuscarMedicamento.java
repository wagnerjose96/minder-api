package br.minder.medicamento.comandos;

import br.minder.medicamento.Medicamento;
import br.minder.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class BuscarMedicamento {
	private MedicamentoId idMedicamento;
	private String nomeMedicamento;
	private String composicao;
	private int ativo;
	
	public BuscarMedicamento(Medicamento comandos) {
		this.idMedicamento = comandos.getIdMedicamento();
		this.nomeMedicamento = comandos.getNomeMedicamento();
		this.composicao = comandos.getComposicao();
		this.ativo = comandos.getAtivo();
	}

	public BuscarMedicamento() {
	}
}
