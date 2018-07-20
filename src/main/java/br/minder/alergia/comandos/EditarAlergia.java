package br.minder.alergia.comandos;

import java.sql.Date;
import java.util.Set;
import br.minder.alergia.AlergiaId;
import br.minder.medicamento.MedicamentoId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarAlergia {
	private AlergiaId idAlergia;
	private String tipoAlergia;
	private String localAfetado;
	private Date dataDescoberta;
	private String efeitos;
	private Set<MedicamentoId> idMedicamentos;

}
