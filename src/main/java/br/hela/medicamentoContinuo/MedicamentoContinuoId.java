package br.hela.medicamentoContinuo;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class MedicamentoContinuoId extends BaseId {
	private static final long serialVersionUID = 8965550305250511524L;
	
	public MedicamentoContinuoId(){
		super();
	}
	public MedicamentoContinuoId(String value) {
		super(value);
	}

}
