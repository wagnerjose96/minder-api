package br.hela;

import java.util.List;
import org.springframework.stereotype.Component;
import br.hela.medicamento.MedicamentoId;

@Component
public class VerificarMedicamento {

	public static boolean verificarMedicamento(MedicamentoId idMedicamento, List<MedicamentoId> list) {
		for (MedicamentoId medicamentoId : list) {
			if (medicamentoId.equals(idMedicamento)) {
				return false;
			}
		}
		return true;
	}

	private VerificarMedicamento() {

	}

}
