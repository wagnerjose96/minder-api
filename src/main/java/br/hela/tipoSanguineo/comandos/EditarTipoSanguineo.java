package br.hela.tipoSanguineo.comandos;

import br.hela.tipoSanguineo.TipoSanguineoId;

public class EditarTipoSanguineo {
	private TipoSanguineoId tipoSanguineoId;
	private String tipoSangue;
	private String rh;
	
	public EditarTipoSanguineo() {
		super();
	}

	public String getTipoSangue() {
		return tipoSangue;
	}

	public void setTipoSangue(String tipoSangue) {
		this.tipoSangue = tipoSangue;
	}

	public String getRh() {
		return rh;
	}

	public void setRh(String rh) {
		this.rh = rh;
	}

	public TipoSanguineoId getTipoSanguineoId() {
		return tipoSanguineoId;
	}


}
