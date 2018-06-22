package br.hela.alergia.comandos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.hela.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class CriarAlergia {
	private String tipoAlergia;
	private String localAfetado;
	private Date dataDescoberta;
	private String efeitos;
	private List<MedicamentoId> id_medicamentos = new ArrayList<>();

}
