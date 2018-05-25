package br.hela.doenca.comandos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.hela.doenca.Doenca;
import br.hela.doenca.DoencaId;
import br.hela.medicamento.Medicamento;
import lombok.Data;

@Data
public class BuscarDoenca {
	private DoencaId idDoenca;
	private String nomeDoenca;
	private Date dataDescoberta;
	private List<Medicamento> medicamentos = new ArrayList<Medicamento>();

	public BuscarDoenca(Doenca comandos) {
		this.idDoenca = comandos.getIdDoenca();
		this.nomeDoenca = comandos.getNomeDoenca();
		this.dataDescoberta = comandos.getDataDescoberta();
	}

}