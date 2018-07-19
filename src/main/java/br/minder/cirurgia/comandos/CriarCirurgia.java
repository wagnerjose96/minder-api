package br.minder.cirurgia.comandos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.minder.medicamento.MedicamentoId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarCirurgia {
	private String tipoCirurgia;
	private Date dataCirurgia;
	private String clinicaResponsavel;
	private String medicoResponsavel;
	private List<MedicamentoId> idMedicamentos = new ArrayList<>();
}
