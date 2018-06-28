package br.hela.cirurgia.comandos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.hela.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class CriarCirurgia {
	private String tipoCirurgia;
	private Date dataCirurgia;
	private String clinicaResponsavel;
	private String medicoResponsavel;
	private List<MedicamentoId> idMedicamentos = new ArrayList<>();
}
