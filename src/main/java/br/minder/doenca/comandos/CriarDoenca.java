package br.minder.doenca.comandos;

import java.util.Date;
import java.util.Set;
import br.minder.medicamento.MedicamentoId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarDoenca {
	private String nomeDoenca;
	private Date dataDescoberta;
	private Set<MedicamentoId> idMedicamentos;
}
