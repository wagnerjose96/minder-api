package br.minder.doenca.comandos;

import java.util.Date;
import java.util.Set;
import br.minder.doenca.DoencaId;
import br.minder.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class EditarDoenca {
	private DoencaId idDoenca;
	private String nomeDoenca;
	private Date dataDescoberta;
	private Set<MedicamentoId> idMedicamentos;
}
