package br.hela.alergia.comandos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.hela.alergia.Alergia;
import br.hela.alergia.AlergiaId;
import br.hela.medicamento.Medicamento;
import lombok.Data;

@Data
public class BuscarAlergia {
	private AlergiaId idAlergia;
	private String tipoAlergia;
	private String localAfetado;
	private Date dataDescoberta;
	private String efeitos;
	private List<Medicamento> medicamentos = new ArrayList<>();

	public BuscarAlergia(Alergia comando) {
		this.idAlergia = comando.getIdAlergia();
		this.tipoAlergia = comando.getTipoAlergia();
		this.localAfetado = comando.getLocalAfetado();
		this.dataDescoberta = comando.getDataDescoberta();
		this.efeitos = comando.getEfeitos();
	}
}
