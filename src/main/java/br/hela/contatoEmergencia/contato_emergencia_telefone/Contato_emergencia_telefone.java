package br.hela.contatoEmergencia.contato_emergencia_telefone;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.hela.contatoEmergencia.ContatoEmergenciaId;
import br.hela.telefone.TelefoneId;

@Entity
public class Contato_emergencia_telefone {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private Contato_emergencia_telefone_Id id;
	@AttributeOverride(name = "value", column = @Column(name = "id_contato_emergencia"))
	private ContatoEmergenciaId idContatoEmergencia;
	@AttributeOverride(name = "value", column = @Column(name = "id_telefone"))
	private TelefoneId idTelefone;

	public Contato_emergencia_telefone() {
		this.id = new Contato_emergencia_telefone_Id();
	}
	/*
	public Alergia_Medicamento(MedicamentoId medicamentos, AlergiaId idAlergia) {
		this.id = new Alergia_Medicamento_Id();
		this.idAlergia = idAlergia;
		this.idMedicamento = medicamentos;
	}*/

	public Contato_emergencia_telefone_Id getId() {
		return id;
	}

	public ContatoEmergenciaId getIdContatoEmergencia() {
		return idContatoEmergencia;
	}

	public void setIdContatoEmergencia(ContatoEmergenciaId idContatoEmergencia) {
		this.idContatoEmergencia = idContatoEmergencia;
	}

	public TelefoneId getIdTelefone() {
		return idTelefone;
	}

	public void setIdTelefone(TelefoneId idTelefone) {
		this.idTelefone = idTelefone;
	}
}
