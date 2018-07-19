package br.minder.doenca.comandos;

import java.util.Date;
import java.util.Set;
import br.minder.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class CriarDoenca {
	private String nomeDoenca;
	private Date dataDescoberta;
	private Set<MedicamentoId> idMedicamentos;
}
