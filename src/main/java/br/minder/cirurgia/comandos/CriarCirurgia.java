package br.minder.cirurgia.comandos;

import java.util.Date;
import java.util.Set;

import br.minder.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class CriarCirurgia {
	private String tipoCirurgia;
	private Date dataCirurgia;
	private String clinicaResponsavel;
	private String medicoResponsavel;
	private Set<MedicamentoId> idMedicamentos;
}
