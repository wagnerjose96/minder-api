package br.hela.medicamento.comandos;

import br.hela.medicamento.Medicamento;
import br.hela.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class BuscarMedicamento {
	private MedicamentoId idMedicamento;
	private String nomeMedicamento;
	private String composicao;
	public int ativo;
	
	public BuscarMedicamento(Medicamento comandos) {
		this.idMedicamento = comandos.getIdMedicamento();
		this.nomeMedicamento = comandos.getNomeMedicamento();
		this.composicao = comandos.getComposicao();
		this.ativo = comandos.getAtivo();
	}

	public BuscarMedicamento() {
	}
}