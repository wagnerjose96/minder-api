package br.minder.doenca.comandos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.minder.medicamento.MedicamentoId;
import lombok.Data;

@Data
public class CriarDoenca {
	private String nomeDoenca;
	private Date dataDescoberta;
	private List<MedicamentoId> idMedicamentos = new ArrayList<>();
}
