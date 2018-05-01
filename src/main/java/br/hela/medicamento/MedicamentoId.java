package br.hela.medicamento;

import javax.persistence.Embeddable;
import br.hela.BaseId;

@Embeddable
public class MedicamentoId extends BaseId{
	private static final long serialVersionUID = 8965550305250511524L;
	
	
	public MedicamentoId(){
		super();
	}
	public MedicamentoId(String value) {
		super(value);
	}
	
	

}
