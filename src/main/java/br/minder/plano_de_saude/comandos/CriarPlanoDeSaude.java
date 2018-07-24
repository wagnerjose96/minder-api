package br.minder.plano_de_saude.comandos;

import java.math.BigInteger;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;

import br.minder.convenio.ConvenioId;
import lombok.Data;

@Data
public class CriarPlanoDeSaude {
	@AttributeOverride(name = "value", column = @Column(name = "id_convenio"))
	private ConvenioId idConvenio;
	private BigInteger numeroCartao;
	private String habitacao;
	private String territorio;
}
