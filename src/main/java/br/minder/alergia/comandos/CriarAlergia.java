package br.minder.alergia.comandos;

import java.util.Date;
import java.util.Set;

import br.minder.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class CriarAlergia {
	private String tipoAlergia;
	private String localAfetado;
	private Date dataDescoberta;
	private String efeitos;
	private Set<MedicamentoId> idMedicamentos;

}
